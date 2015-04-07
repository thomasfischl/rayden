package com.github.thomasfischl.rayden.validation;

import java.util.List;

import org.eclipse.xtext.validation.Check;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.RaydenDSLPackage;
import com.github.thomasfischl.rayden.util.RaydenModelUtils;

/**
 * Custom validation rules.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
public class RaydenDSLJavaValidator extends AbstractRaydenDSLJavaValidator {

  public static final String KEYWORD_NOT_EXISTS = "KEYWORD_NOT_EXISTS";

  @Check
  public void checkKeywordCallExists(KeywordCall keyword) {

    // check if this instance is a inline keyword
    if (RaydenModelUtils.isInlineKeyword(keyword)) {
      return;
    }

    List<KeywordDecl> keywords = RaydenModelUtils.getAllKeywords(keyword);
    boolean keywordExists = false;
    for (KeywordDecl keywordDecl : keywords) {
      String name1 = RaydenModelUtils.normalizeKeyword(keyword.getName());
      String name2 = RaydenModelUtils.normalizeKeyword(keywordDecl.getName());
      if (name1.equals(name2)) {
        keywordExists = true;
      }
    }

    if (!keywordExists) {
      warning("Keyword does not exists", RaydenDSLPackage.Literals.KEYWORD_CALL__NAME, KEYWORD_NOT_EXISTS);
    }
  }

}
