package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.runtime.RaydenReporter;
import com.github.thomasfischl.rayden.runtime.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.runtime.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.runtime.keywords.KeywordResult;

public class UnimplementedKeyword implements IScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, IKeywordScope scope, RaydenReporter reporter) {
    reporter.reportKeywordBegin(keyword + "(scripted)");
    reporter.log("Unimplemented Keyword");
    reporter.reportKeywordEnd(keyword + "(scripted)");
    return new KeywordResult(false);
  }
}
