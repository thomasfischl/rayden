package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class UnimplementedKeyword implements ScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter) {
    reporter.log("Unimplemented Keyword");
    return new KeywordResult(false);
  }
}
