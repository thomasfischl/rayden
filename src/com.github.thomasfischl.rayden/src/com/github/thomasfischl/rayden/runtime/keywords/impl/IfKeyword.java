package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class IfKeyword implements ScriptedCompoundKeyword {

  private KeywordScope scope;

  @Override
  public void handleError(Throwable e) {
    throw new IllegalStateException("not implemented");
  }

  @Override
  public void initializeKeyword(String keyword, KeywordScope scope, RaydenReporter reporter) {
    this.scope = scope;
  }

  @Override
  public KeywordResult finalizeKeyword() {
    return new KeywordResult(true);
  }

  @Override
  public boolean executeBefore() {
    return scope.getVariableAsBoolean("condition");
  }

  @Override
  public boolean executeAfter() {
    return false;
  }
}
