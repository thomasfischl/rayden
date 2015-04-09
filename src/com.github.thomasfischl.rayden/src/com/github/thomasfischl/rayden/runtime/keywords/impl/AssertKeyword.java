package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class AssertKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    return new KeywordResult(scope.getVariableAsBoolean("assert"));
  }
}
