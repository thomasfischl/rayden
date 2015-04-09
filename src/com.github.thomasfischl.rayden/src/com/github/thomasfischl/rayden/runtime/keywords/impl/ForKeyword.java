package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class ForKeyword implements IScriptedCompoundKeyword {

  private String variable;
  private int from;
  private int to;
  private IRaydenReporter reporter;
  private IKeywordScope scope;
  private String keyword;

  @Override
  public void handleError(Throwable e) {
    throw new IllegalStateException("not implemented");
  }

  @Override
  public void initializeKeyword(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    this.keyword = keyword;
    this.scope = scope;
    this.reporter = reporter;

    reporter.reportKeywordBegin(keyword + "(scripted compound)");
    variable = scope.getVariableAsString("variable");
    from = scope.getVariableAsInteger("from");
    to = scope.getVariableAsInteger("to");

    scope.setVariable(variable, from);
  }

  @Override
  public KeywordResult finalizeKeyword() {
    reporter.reportKeywordEnd(keyword + "(scripted compound)");
    return new KeywordResult(true);
  }

  @Override
  public boolean executeBefore() {
    return scope.getVariableAsInteger(variable) < to;
  }

  @Override
  public boolean executeAfter() {
    int index = scope.getVariableAsInteger(variable);
    scope.setVariable(variable, index + 1);
    return true;
  }
}
