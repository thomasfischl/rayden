package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class ForKeyword implements IScriptedCompoundKeyword {

  private String variable;
  private int from;
  private int to;
  private IKeywordScope scope;

  @Override
  public void handleError(Throwable e) {
    throw new IllegalStateException("not implemented");
  }

  @Override
  public void initializeKeyword(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    this.scope = scope;

    variable = scope.getVariableAsString("variable");
    from = scope.getVariableAsInteger("from");
    to = scope.getVariableAsInteger("to");

    scope.setVariable(variable, from);
  }

  @Override
  public KeywordResult finalizeKeyword() {
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
