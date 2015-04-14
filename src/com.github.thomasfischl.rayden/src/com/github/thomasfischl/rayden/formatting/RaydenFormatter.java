package com.github.thomasfischl.rayden.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

import com.github.thomasfischl.rayden.services.RaydenDSLGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#formatting on how and when to use it
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class RaydenFormatter extends AbstractDeclarativeFormatter {

  @Override
  protected void configureFormatting(final FormattingConfig c) {
    RaydenDSLGrammarAccess f = (RaydenDSLGrammarAccess) getGrammarAccess();

    c.setAutoLinewrap(140);

    // KeywordDecl
    c.setLinewrap().after(f.getKeywordDeclRule());

    // c.setNoSpace().before(f.getKeywordDeclAccess().getLessThanSignKeyword_1_0());
    // c.setNoSpace().after(f.getKeywordDeclAccess().getLessThanSignKeyword_1_0());
    // c.setNoSpace().before(f.getKeywordDeclAccess().getGreaterThanSignKeyword_1_2());
    // c.setSpace(" ").after(f.getKeywordDeclAccess().getGreaterThanSignKeyword_1_2());
    // c.setNoSpace().after(f.getKeywordDeclAccess().getNameIDEXTTerminalRuleCall_2_0());

    c.setLinewrap(2).after(f.getKeywordDeclAccess().getParametersParameterDeclParserRuleCall_4_0());
    c.setLinewrap(2).after(f.getKeywordDeclAccess().getDescDescriptionParserRuleCall_3_0());

    c.setLinewrap().after(f.getKeywordDeclAccess().getLeftCurlyBracketKeyword_2());
    c.setLinewrap(2).after(f.getKeywordDeclAccess().getRightCurlyBracketKeyword_6());

    c.setIndentationIncrement().after(f.getKeywordDeclAccess().getLeftCurlyBracketKeyword_2());
    c.setIndentationDecrement().before(f.getKeywordDeclAccess().getRightCurlyBracketKeyword_6());

    c.setLinewrap().after(f.getKeywordDeclAccess().getScriptAssignment_5_0());

    // ParameterDecl
    c.setLinewrap().after(f.getParameterDeclRule());

    // KeywordCall
    c.setLinewrap().after(f.getKeywordCallRule());
    c.setIndentationIncrement().after(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setIndentationDecrement().before(f.getKeywordCallKeywordListAccess().getRightCurlyBracketKeyword_2());

    c.setNoSpace().before(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setLinewrap().after(f.getKeywordCallKeywordListAccess().getLeftCurlyBracketKeyword_0());
    c.setLinewrap().after(f.getKeywordCallKeywordListAccess().getRightCurlyBracketKeyword_2());

    // ImportDecl
    c.setLinewrap().after(f.getImportDeclRule());
    // c.setLinewrap(2).after(f.getModelAccess().getImportsImportDeclParserRuleCall_0_0());

  }
}
