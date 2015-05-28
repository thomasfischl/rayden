package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.RaydenExpressionVariable;
import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class ForKeyword implements ScriptedCompoundKeyword {

  private String variable;
  private int from;
  private int to;
  private KeywordScope scope;

  @Override
  public void handleError(Throwable e) {
    throw new IllegalStateException("not implemented");
  }

  @Override
  public void initializeKeyword(String keyword, KeywordScope scope, RaydenReporter reporter) {
    this.scope = scope;

    variable = scope.getVariable("var", RaydenExpressionVariable.class).getName();
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
