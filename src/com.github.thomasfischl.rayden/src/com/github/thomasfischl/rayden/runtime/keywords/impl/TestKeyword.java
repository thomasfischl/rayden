package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class TestKeyword implements ScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter) {
    for (String name : scope.getVariableNames()) {
      reporter.log("Property: " + name + " -> " + scope.getVariable(name));
    }
    reporter.log("It works!!!");
    return new KeywordResult(true);
  }
}
