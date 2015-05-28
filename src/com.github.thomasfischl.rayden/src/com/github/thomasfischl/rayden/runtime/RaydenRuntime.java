package com.github.thomasfischl.rayden.runtime;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Stack;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.parser.ParseException;

import com.github.thomasfischl.rayden.RaydenDSLStandaloneSetupGenerated;
import com.github.thomasfischl.rayden.api.RaydenExtReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.api.keywords.ScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;
import com.github.thomasfischl.rayden.raydenDSL.AndExpr;
import com.github.thomasfischl.rayden.raydenDSL.Expr;
import com.github.thomasfischl.rayden.raydenDSL.Fact;
import com.github.thomasfischl.rayden.raydenDSL.ImportDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordType;
import com.github.thomasfischl.rayden.raydenDSL.Model;
import com.github.thomasfischl.rayden.raydenDSL.NotFact;
import com.github.thomasfischl.rayden.raydenDSL.OrExpr;
import com.github.thomasfischl.rayden.raydenDSL.ParameterDecl;
import com.github.thomasfischl.rayden.raydenDSL.RaydenDSLFactory;
import com.github.thomasfischl.rayden.raydenDSL.RelExpr;
import com.github.thomasfischl.rayden.raydenDSL.ScriptType;
import com.github.thomasfischl.rayden.raydenDSL.SimpleExpr;
import com.github.thomasfischl.rayden.raydenDSL.Term;
import com.github.thomasfischl.rayden.util.RaydenModelUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class RaydenRuntime {

  @Inject
  private IParser parser;

  private RaydenExtReporter reporter;

  private final Map<String, KeywordDecl> definedKeywords = new HashMap<>();

  private final Map<String, KeywordDecl> definedImportedKeywords = new HashMap<>();

  private final Stack<RaydenScriptScope> stack = new Stack<>();

  private File workingFolder;

  private ClassLoader classLoader;

  protected RaydenRuntime() {
    reporter = new RaydenReporter();
    setWorkingFolder(new File("."));
  }

  public void setReporter(RaydenExtReporter reporter) {
    this.reporter = reporter;
  }

  public void loadRaydenFile(Reader reader) {
    loadFile(reader, definedKeywords);
  }

  public void loadLibraryFile(Reader reader) {
    loadFile(reader, definedImportedKeywords);
  }

  private void loadFile(Reader reader, Map<String, KeywordDecl> keywordStore) {
    IParseResult result = parser.parse(reader);
    if (result.hasSyntaxErrors()) {
      for (INode error : result.getSyntaxErrors()) {
        reporter.error(error.getSyntaxErrorMessage().toString());
      }
      throw new ParseException("Provided input contains syntax errors.");
    }

    if (result.getRootASTElement() instanceof Model) {
      reporter.log("Successful loaded model.");
      Model model = (Model) result.getRootASTElement();

      EList<KeywordDecl> keywords = model.getKeywords();
      reporter.log("Loading " + keywords.size() + " keywords from library");
      List<String> keywordNames = new ArrayList<>();
      for (KeywordDecl keyword : keywords) {
        keywordNames.add("\"" + RaydenModelUtils.normalizeKeyword(keyword.getName()) + "\"");
        keywordStore.put(RaydenModelUtils.normalizeKeyword(keyword.getName()), keyword);
      }
      reporter.log("Loaded Keywords (" + Joiner.on(",").join(keywordNames) + ")");

      EList<ImportDecl> imports = model.getImports();
      for (ImportDecl importDecl : imports) {
        try {
          loadLibraryFile(new FileReader(new File(workingFolder, importDecl.getImportLibrary())));
        } catch (Exception e) {
          reporter.error("Error during loading library '" + importDecl.getImportLibrary() + "'");
          reporter.error(e);
        }
      }
    }
  }

  public RaydenScriptResult executeAllTestSuites() {

    RaydenScriptResult result = new RaydenScriptResult();

    reporter.start();

    for (KeywordDecl keyword : definedKeywords.values()) {
      if (RaydenModelUtils.isTestSuiteKeyword(keyword)) {
        for (KeywordCall keywordCall : keyword.getKeywordlist().getChildren()) {
          runKeyword(result, keywordCall);
        }
      }
    }

    reporter.stop();

    reporter.log("  Results:");
    reporter.log("  Success Tests     : " + result.getSuccessTestCount());
    reporter.log("  Failed Tests      : " + result.getFailedTestCount());
    reporter.log("  Fatal Failed Tests: " + result.getFatalFailedTestCount());
    reporter.log("----------------------------------------------------------------");

    return result;
  }

  public void runKeyword(RaydenScriptResult result, KeywordCall keywordCall) {
    try {
      reporter.log("----------------------------------------------------------------");
      reporter.log("Execute Testsuite '" + keywordCall + "'");
      reporter.log("----------------------------------------------------------------");
      executeKeyword(keywordCall);
      result.incSuccessTestCount();
    } catch (RaydenScriptException e) {
      e.printStackTrace();
      result.incFailedTestCount();
      reporter.error(e.getMessage());
      result.addErrorMessage(e.getMessage());
    } catch (RaydenScriptFailedException e) {
      e.printStackTrace();
      result.incFatalFailedTestCount();
      reporter.error(e.getMessage());
      result.addErrorMessage(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      result.incFailedTestCount();
      reporter.error(e.getMessage());
      result.addErrorMessage(e.getMessage());
    } finally {
      reporter.log("----------------------------------------------------------------");
    }
  }

  private void executeKeyword(KeywordCall keywordCall) {
    stack.clear();

    try {
      reporter.reportTestCaseStart(keywordCall.getName());
      // create initial scope
      stack.push(new RaydenScriptScope(null, Lists.newArrayList(keywordCall)));

      Object currKeyword = null;
      RaydenScriptScope currScope = null;
      while (!stack.isEmpty()) {

        currScope = stack.peek();
        currKeyword = currScope.getNextOpt();
        if (currKeyword == null) {
          stack.pop();
          continue;
        }

        if (currKeyword instanceof KeywordCall) {
          KeywordCall keyword = (KeywordCall) currKeyword;
          executeKeywordCall(keyword, currScope);
        }

        if (currKeyword instanceof KeywordDecl) {
          KeywordDecl keyword = (KeywordDecl) currKeyword;

          if (currScope.getKeywordCall().getKeywordList() != null && currScope.getKeywordCall().getParameters() != null) {
            // System.out.println("Scripted Compound Keyword");

            if (currScope.isKeywordBegin()) {
              executeScriptedCompoundKeywordDeclBegin(keyword, currScope);
            } else if (currScope.isKeywordEnd()) {
              executeScriptedCompoundKeywordDeclEnd(keyword, currScope);
            } else {
              // TODO invalid state
              throw new IllegalStateException("The runtime has an invalid state.");
            }
          } else {
            executeKeywordDecl(currScope, keyword);
          }
        }
      }
    } finally {
      reporter.reportTestCaseEnd(keywordCall.getName());
    }
  }

  private void executeScriptedCompoundKeywordDeclBegin(KeywordDecl keyword, RaydenScriptScope currScope) {
    reporter.reportKeywordBegin(RaydenModelUtils.normalizeKeyword(keyword.getName()));

    String name = RaydenModelUtils.normalizeKeyword(keyword.getName());
    ScriptedCompoundKeyword keywordImpl = loadKeywordImplementation(keyword, ScriptedCompoundKeyword.class);

    keywordImpl.initializeKeyword(name, currScope, reporter);

    boolean execute = keywordImpl.executeBefore();
    if (execute) {
      stack.push(new RaydenScriptScope(currScope, currScope.getKeywordCall().getKeywordList().getKeywordlist().getChildren()));
      currScope.setScriptedCompoundKeyword(keywordImpl);
    }
  }

  private void executeScriptedCompoundKeywordDeclEnd(KeywordDecl keyword, RaydenScriptScope currScope) {

    ScriptedCompoundKeyword keywordImpl = currScope.getScriptedCompoundKeyword();
    if (keywordImpl != null) {
      boolean repeat = keywordImpl.executeAfter();

      if (repeat) {
        reporter.reportKeywordEnd(RaydenModelUtils.normalizeKeyword(keyword.getName()));

        boolean execute = keywordImpl.executeBefore();
        if (execute) {
          reporter.reportKeywordBegin(RaydenModelUtils.normalizeKeyword(keyword.getName()));
          stack.push(new RaydenScriptScope(currScope, currScope.getKeywordCall().getKeywordList().getKeywordlist().getChildren()));
          currScope.resetCursor();
        }
      } else {
        KeywordResult result = keywordImpl.finalizeKeyword();
        if (!result.isSuccess()) {
          // TODO improve this implementation
          throw new RaydenScriptFailedException("Scripted Keyword '" + keyword.getName() + "' failed!");
        }
        reporter.reportKeywordEnd(RaydenModelUtils.normalizeKeyword(keyword.getName()));
      }

    } else {
      reporter.reportKeywordEnd(RaydenModelUtils.normalizeKeyword(keyword.getName()));
    }

    finishKeyword(keyword, currScope);

  }

  private void finishKeyword(KeywordDecl keyword, RaydenScriptScope currScope) {
    // transfer parameter from child scope to parent scope
    EList<ParameterDecl> parameters = keyword.getParameters();
    for (ParameterDecl parameter : parameters) {
      if (parameter.getDir() != null && !parameter.getDir().equals("in")) {
        currScope.getParent().setVariable(parameter.getName(), currScope.getVariable(parameter.getName()));
      }
    }
  }

  private void executeKeywordDecl(RaydenScriptScope currScope, KeywordDecl keyword) {
    if (currScope.isKeywordBegin()) {
      executeKeywordDeclBegin(keyword, currScope);
    } else if (currScope.isKeywordEnd()) {
      executeKeywordDeclEnd(keyword, currScope);
    } else {
      // TODO invalid state
      throw new IllegalStateException("The runtime has an invalid state.");
    }
  }

  private void executeKeywordDeclEnd(KeywordDecl keyword, RaydenScriptScope currScope) {
    reporter.reportKeywordEnd(keyword.getName());
    finishKeyword(keyword, currScope);
  }

  private void executeKeywordDeclBegin(KeywordDecl keyword, RaydenScriptScope currScope) {
    reporter.reportKeywordBegin(RaydenModelUtils.normalizeKeyword(keyword.getName()));

    if (RaydenModelUtils.isScriptedKeyword(keyword)) {
      // Execute a scripted keyword
      String name = RaydenModelUtils.normalizeKeyword(keyword.getName());
      ScriptedKeyword keywordImpl = loadKeywordImplementation(keyword, ScriptedKeyword.class);
      KeywordResult result = keywordImpl.execute(name, currScope, reporter);
      if (!result.isSuccess()) {
        // TODO improve this implementation
        throw new RaydenScriptFailedException("Scripted Keyword '" + name + "' failed!");
      }
    } else {
      // Execute a compound keyword
      if (keyword.getKeywordlist() != null) {
        stack.push(new RaydenScriptScope(currScope, keyword.getKeywordlist().getChildren()));
      }
    }
  }

  private <T> T loadKeywordImplementation(KeywordDecl keyword, Class<T> clazz) {
    if (keyword.getScript() == null) {
      throw new RaydenScriptException("Keyword '" + keyword.getName() + "': Missing script configuraiton.");
    }
    if (ScriptType.JAVA == keyword.getScript().getScriptType()) {
      String keywordClass = keyword.getScript().getClass_();
      try {

        if (classLoader == null) {
          classLoader = new RaydenClassLoader(getClass().getClassLoader(), workingFolder);
        }

        Class<?> keywordClassObject = classLoader.loadClass(keywordClass);
        Object keywordObj = keywordClassObject.newInstance();

        if (clazz.isAssignableFrom(keywordObj.getClass())) {
          return clazz.cast(keywordObj);
        } else {
          throw new RaydenScriptException("Class '" + keywordObj.getClass().getSimpleName() + "' does not implement the '"
              + clazz.getSimpleName() + "'.");
        }

      } catch (ClassNotFoundException e) {
        reporter.error(e);
        throw new RaydenScriptException("Keyword class '" + keywordClass + "' can't be found.");
      } catch (InstantiationException | IllegalAccessException e) {
        reporter.error(e);
        throw new RaydenScriptException("An error occurs during creating the scripted keyword object. Message: " + e.getMessage());
      }
    } else {
      throw new RaydenScriptException("Unkown script keyword type '" + keyword.getScript().getScriptType() + "'.");
    }
  }

  private void executeKeywordCall(KeywordCall keyword, RaydenScriptScope currScope) {
    if (RaydenModelUtils.isInlineKeyword(keyword)) {
      // Execute the keyword as inline keyword
      // Create a temporary keyword implementation for the inline keyword and
      // push it on the execution stack
      KeywordDecl keywordImpl = RaydenDSLFactory.eINSTANCE.createKeywordDecl();
      keywordImpl.setName(keyword.getName());
      keywordImpl.setType(KeywordType.KEYWORD);
      keywordImpl.setKeywordlist(EcoreUtil.copy(keyword.getKeywordList().getKeywordlist()));

      stack.push(new RaydenScriptScope(currScope, keyword, keywordImpl));
    } else {
      KeywordDecl keywordImpl = getKeywordDefinition(keyword.getName());
      if (keywordImpl != null) {
        RaydenScriptScope scope = createCallScope(keyword, currScope, keywordImpl);
        stack.push(scope);
      } else {
        reporter.reportUnkownKeyword(keyword.getName());
        throw new RaydenScriptException("No keyword found with name '" + keyword.getName() + "'");
      }
    }
  }

  private RaydenScriptScope createCallScope(KeywordCall keyword, RaydenScriptScope currScope, KeywordDecl keywordImpl) {
    RaydenScriptScope scope = new RaydenScriptScope(currScope, keyword, keywordImpl);

    EList<Expr> callParameters = new BasicEList<>();
    EList<ParameterDecl> properties = new BasicEList<>();

    if (keyword.getParameters() != null) {
      callParameters = keyword.getParameters().getParameters();
    }

    if (keyword.getLocator() != null) {
      Fact firstParameter = RaydenDSLFactory.eINSTANCE.createFact();
      firstParameter.setLocator(keyword.getLocator());

      NotFact notFact = RaydenDSLFactory.eINSTANCE.createNotFact();
      notFact.setExpr(firstParameter);

      Term term = RaydenDSLFactory.eINSTANCE.createTerm();
      term.getExpr().add(notFact);

      SimpleExpr simpleExpr = RaydenDSLFactory.eINSTANCE.createSimpleExpr();
      simpleExpr.getExpr().add(term);

      RelExpr relExpr = RaydenDSLFactory.eINSTANCE.createRelExpr();
      relExpr.getExpr().add(simpleExpr);

      AndExpr andExpr = RaydenDSLFactory.eINSTANCE.createAndExpr();
      andExpr.getExpr().add(relExpr);

      OrExpr orExpr = RaydenDSLFactory.eINSTANCE.createOrExpr();
      orExpr.getExpr().add(andExpr);

      Expr expr = RaydenDSLFactory.eINSTANCE.createExpr();
      expr.setExpr(orExpr);

      callParameters.add(0, expr);
    }

    if (keywordImpl.getParameters() != null) {
      properties = keywordImpl.getParameters();
    }

    try {
      RaydenExpressionEvaluator exprEval = new RaydenExpressionEvaluator(currScope, RaydenModelUtils.getRoot(keyword));

      Iterator<ParameterDecl> it = properties.iterator();

      // TODO check parameter type (in, out, inout)
      for (int i = 0; i < callParameters.size(); i++) {
        ParameterDecl parameter = it.next();
        while (parameter.getDir() != null && parameter.getDir().equals("out")) {
          parameter = it.next();
        }

        scope.setVariable(parameter.getName(), exprEval.eval(callParameters.get(i), parameter.getType()));
      }
    } catch (NoSuchElementException e) {
      throw new RaydenScriptException("Invalid number of arguments for keyword '" + keyword.getName() + "'.", e);
    }

    return scope;
  }

  private KeywordDecl getKeywordDefinition(String keywordName) {
    String normalizeKeywordName = RaydenModelUtils.normalizeKeyword(keywordName);
    KeywordDecl keywordDecl = definedKeywords.get(normalizeKeywordName);
    if (keywordDecl == null) {
      keywordDecl = definedImportedKeywords.get(normalizeKeywordName);
    }
    return keywordDecl;
  }

  public static RaydenRuntime createRuntime() {
    Injector injector = new RaydenDSLStandaloneSetupGenerated().createInjectorAndDoEMFRegistration();
    return injector.getInstance(RaydenRuntime.class);
  }

  public void setWorkingFolder(File workingFolder) {
    this.workingFolder = workingFolder;
  }

  public Map<String, KeywordDecl> getDefinedImportedKeywords() {
    return definedImportedKeywords;
  }

  public Map<String, KeywordDecl> getDefinedKeywords() {
    return definedKeywords;
  }
}
