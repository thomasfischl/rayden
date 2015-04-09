package com.github.thomasfischl.rayden.test.keywords;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class PrintKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    reporter.log(scope.getVariable("text").toString());
    return new KeywordResult(true);
  }
}
