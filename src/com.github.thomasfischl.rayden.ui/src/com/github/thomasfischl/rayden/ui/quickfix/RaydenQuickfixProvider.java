package com.github.thomasfischl.rayden.ui.quickfix;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext;
import org.eclipse.xtext.ui.editor.model.edit.ISemanticModification;
import org.eclipse.xtext.ui.editor.quickfix.DefaultQuickfixProvider;
import org.eclipse.xtext.ui.editor.quickfix.Fix;
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor;
import org.eclipse.xtext.validation.Issue;

import com.github.thomasfischl.rayden.raydenDSL.Description;
import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordType;
import com.github.thomasfischl.rayden.raydenDSL.Model;
import com.github.thomasfischl.rayden.raydenDSL.ParameterDecl;
import com.github.thomasfischl.rayden.raydenDSL.RaydenDSLFactory;
import com.github.thomasfischl.rayden.raydenDSL.ScriptType;
import com.github.thomasfischl.rayden.util.RaydenModelUtils;
import com.github.thomasfischl.rayden.validation.RaydenDSLJavaValidator;

/**
 * Custom quickfixes.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#quickfixes
 */
public class RaydenQuickfixProvider extends DefaultQuickfixProvider {

  private final class CreateKeywordHandler implements ISemanticModification {
    @Override
    public void apply(EObject keyword, IModificationContext context) {
      if (keyword instanceof KeywordCall) {
        KeywordCall keywordCall = (KeywordCall) keyword;

        KeywordDecl keywordImpl = RaydenDSLFactory.eINSTANCE.createKeywordDecl();
        keywordImpl.setName(RaydenModelUtils.normalizeKeyword(keywordCall.getName()));
        keywordImpl.setType(KeywordType.KEYWORD);
        keywordImpl.setScript(RaydenDSLFactory.eINSTANCE.createKeywordScript());
        keywordImpl.getScript().setScriptType(ScriptType.JAVA);
        keywordImpl.getScript().setClass("com.github.thomasfischl.rayden.runtime.keywords.impl.UnimplementedKeyword");

        Description description = RaydenDSLFactory.eINSTANCE.createDescription();
        description.setText("'''TODO: Implement Keyword'''");
        keywordImpl.setDesc(description);

        defineParameters(keywordCall, keywordImpl);

        Model root = RaydenModelUtils.getRoot(keyword);
        root.getKeywords().add(keywordImpl);
      }
    }
  }

  private final class CreateUserKeywordHandler implements ISemanticModification {
    @Override
    public void apply(EObject keyword, IModificationContext context) {
      if (keyword instanceof KeywordCall) {
        KeywordCall keywordCall = (KeywordCall) keyword;

        KeywordDecl keywordImpl = RaydenDSLFactory.eINSTANCE.createKeywordDecl();
        keywordImpl.setName(keywordCall.getName());
        keywordImpl.setType(KeywordType.KEYWORD);

        Description description = RaydenDSLFactory.eINSTANCE.createDescription();
        description.setText("'''TODO: Implement'''");
        keywordImpl.setDesc(description);

        defineParameters(keywordCall, keywordImpl);

        Model root = RaydenModelUtils.getRoot(keyword);
        root.getKeywords().add(keywordImpl);
      }
    }

  }

  private void defineParameters(KeywordCall keywordCall, KeywordDecl keywordImpl) {
    if (keywordCall.getParameters() != null && keywordCall.getParameters().getParameters() != null) {
      for (int i = 0; i < keywordCall.getParameters().getParameters().size(); i++) {
        ParameterDecl prop = RaydenDSLFactory.eINSTANCE.createParameterDecl();
        prop.setName("parameter" + (i + 1));
        keywordImpl.getParameters().add(prop);
      }
    }
  }

  @Fix(RaydenDSLJavaValidator.KEYWORD_NOT_EXISTS)
  public void fixName(final Issue issue, IssueResolutionAcceptor acceptor) {
    acceptor.accept(issue, "Create scripted keyword", "Create a new scripted keyword.", "upcase.png", new CreateKeywordHandler());
    acceptor.accept(issue, "Create user keyword", "Create a new user keyword.", "upcase.png", new CreateUserKeywordHandler());
  }

}
