package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class TestKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    reporter.reportKeywordBegin(keyword + "(scripted)");

    for (String name : scope.getVariableNames()) {
      reporter.log("Property: " + name + " -> " + scope.getVariable(name));
    }

    reporter.log("It works!!!");
    reporter.reportKeywordEnd(keyword + "(scripted)");
    return new KeywordResult(true);
  }
}
