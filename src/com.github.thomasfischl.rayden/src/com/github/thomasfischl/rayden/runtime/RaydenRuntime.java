package com.github.thomasfischl.rayden.runtime;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.IParser;
import org.eclipse.xtext.parser.ParseException;

import com.github.thomasfischl.rayden.RaydenDSLStandaloneSetupGenerated;
import com.github.thomasfischl.rayden.api.IRaydenExtReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.raydenDSL.Expr;
import com.github.thomasfischl.rayden.raydenDSL.ImportDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordType;
import com.github.thomasfischl.rayden.raydenDSL.Model;
import com.github.thomasfischl.rayden.raydenDSL.PropertyDecl;
import com.github.thomasfischl.rayden.raydenDSL.RaydenDSLFactory;
import com.github.thomasfischl.rayden.raydenDSL.ScriptType;
import com.github.thomasfischl.rayden.util.RaydenModelUtils;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class RaydenRuntime {

  @Inject
  private IParser parser;

  private IRaydenExtReporter reporter;

  private final Map<String, KeywordDecl> definedKeywords = new HashMap<>();

  private final Map<String, KeywordDecl> definedImportedKeywords = new HashMap<>();

  private final Stack<RaydenScriptScope> stack = new Stack<>();

  private File workingFolder;

  private ClassLoader classLoader;

  protected RaydenRuntime() {
    reporter = new RaydenReporter();
    setWorkingFolder(new File("."));
  }

  public void setReporter(IRaydenExtReporter reporter) {
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

    int successTests = 0;
    int failedTests = 0;
    int errorTests = 0;

    RaydenScriptResult result = new RaydenScriptResult();

    reporter.start();

    for (KeywordDecl keyword : definedKeywords.values()) {
      if (RaydenModelUtils.isTestSuiteKeyword(keyword)) {
        try {
          reporter.log("----------------------------------------------------------------");
          reporter.log("Execute Testsuite '" + keyword.getName() + "'");
          reporter.log("----------------------------------------------------------------");
          executeKeyword(keyword.getName());
          successTests++;
        } catch (RaydenScriptException e) {
          e.printStackTrace();
          errorTests++;
          reporter.error(e.getMessage());
          result.addErrorMessage(e.getMessage());
        } catch (RaydenScriptFailedException e) {
          e.printStackTrace();
          failedTests++;
          reporter.error(e.getMessage());
          result.addErrorMessage(e.getMessage());
        } catch (Exception e) {
          e.printStackTrace();
          errorTests++;
          reporter.error(e.getMessage());
          result.addErrorMessage(e.getMessage());
        } finally {
          reporter.log("----------------------------------------------------------------");
        }
      }
    }

    reporter.stop();

    reporter.log("  Results:");
    reporter.log("  Success Tests     : " + successTests);
    reporter.log("  Failed Tests      : " + failedTests);
    reporter.log("  Fatal Failed Tests: " + errorTests);
    reporter.log("----------------------------------------------------------------");

    result.setSuccessTestCount(successTests);
    result.setFailedTestCount(failedTests);
    result.setFatalFailedTestCount(errorTests);

    return result;
  }

  private void executeKeyword(String keywordName) {
    stack.clear();

    try {
      reporter.reportTestCaseStart(keywordName);
      // create initial scope
      KeywordCall keywordCall = RaydenDSLFactory.eINSTANCE.createKeywordCall();
      keywordCall.setName(keywordName);
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
      reporter.reportTestCaseEnd(keywordName);
    }
  }

  private void executeScriptedCompoundKeywordDeclBegin(KeywordDecl keyword, RaydenScriptScope currScope) {
    reporter.reportKeywordBegin(keyword.getName());

    String name = RaydenModelUtils.normalizeKeyword(keyword.getName());
    IScriptedCompoundKeyword keywordImpl = loadKeywordImplementation(keyword, IScriptedCompoundKeyword.class);

    keywordImpl.initializeKeyword(name, currScope, reporter);

    boolean execute = keywordImpl.executeBefore();
    if (execute) {
      stack.push(new RaydenScriptScope(currScope, currScope.getKeywordCall().getKeywordList().getKeywordlist().getChildren()));
      currScope.setScriptedCompoundKeyword(keywordImpl);
    }
  }

  private void executeScriptedCompoundKeywordDeclEnd(KeywordDecl keyword, RaydenScriptScope currScope) {

    IScriptedCompoundKeyword keywordImpl = currScope.getScriptedCompoundKeyword();
    if (keywordImpl != null) {
      boolean repeat = keywordImpl.executeAfter();

      if (repeat) {
        reporter.reportKeywordEnd(keyword.getName());

        boolean execute = keywordImpl.executeBefore();
        if (execute) {
          stack.push(new RaydenScriptScope(currScope, currScope.getKeywordCall().getKeywordList().getKeywordlist().getChildren()));
          currScope.resetCursor();
        }
      } else {
        KeywordResult result = keywordImpl.finalizeKeyword();
        if (!result.isSuccess()) {
          // TODO improve this implementation
          throw new RaydenScriptFailedException("Scripted Keyword '" + keyword.getName() + "' failed!");
        }
        reporter.reportKeywordEnd(keyword.getName());
      }

    } else {
      reporter.reportKeywordEnd(keyword.getName());
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
  }

  private void executeKeywordDeclBegin(KeywordDecl keyword, RaydenScriptScope currScope) {
    reporter.reportKeywordBegin(keyword.getName());

    if (RaydenModelUtils.isScriptedKeyword(keyword)) {
      // Execute a scripted keyword
      String name = RaydenModelUtils.normalizeKeyword(keyword.getName());
      IScriptedKeyword keywordImpl = loadKeywordImplementation(keyword, IScriptedKeyword.class);
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
      // Create a temporary keyword implementation for the inline keyword and push it on the execution stack
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
    EList<PropertyDecl> properties = new BasicEList<>();

    if (keyword.getParameters() != null) {
      callParameters = keyword.getParameters().getParameters();
    }

    if (keywordImpl.getProperties() != null) {
      properties = keywordImpl.getProperties();
    }

    if (callParameters.size() == properties.size()) {

      RaydenExpressionEvaluator exprEval = new RaydenExpressionEvaluator(currScope);

      // TODO check parameter type (in, out, inout)
      for (int i = 0; i < callParameters.size(); i++) {
        scope.setVariable(properties.get(i).getName(), exprEval.eval(callParameters.get(i)));
      }

    } else {
      throw new RaydenScriptException("Invalid number of arguments for keyword '" + keyword.getName() + "'.");
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
    classLoader = new RaydenClassLoader(getClass().getClassLoader(), workingFolder);
  }

  public Map<String, KeywordDecl> getDefinedImportedKeywords() {
    return definedImportedKeywords;
  }

  public Map<String, KeywordDecl> getDefinedKeywords() {
    return definedKeywords;
  }
}
