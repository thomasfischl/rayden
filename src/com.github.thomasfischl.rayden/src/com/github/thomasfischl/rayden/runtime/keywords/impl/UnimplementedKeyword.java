package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class UnimplementedKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    reporter.reportKeywordBegin(keyword + "(scripted)");
    reporter.log("Unimplemented Keyword");
    reporter.reportKeywordEnd(keyword + "(scripted)");
    return new KeywordResult(false);
  }
}
