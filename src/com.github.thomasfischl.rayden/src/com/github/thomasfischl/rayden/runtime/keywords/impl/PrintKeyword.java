package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.runtime.RaydenReporter;
import com.github.thomasfischl.rayden.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.runtime.keywords.KeywordResult;

public class PrintKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, RaydenReporter reporter) {
    reporter.log(scope.getVariable("text").toString());
    return new KeywordResult(true);
  }
}
