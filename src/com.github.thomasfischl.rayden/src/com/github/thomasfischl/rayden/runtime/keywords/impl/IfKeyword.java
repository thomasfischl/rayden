package com.github.thomasfischl.rayden.runtime.keywords.impl;

import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class IfKeyword implements IScriptedCompoundKeyword {

  private IRaydenReporter reporter;
  private IKeywordScope scope;
  private String keyword;

  @Override
  public void handleError(Throwable e) {
    throw new IllegalStateException("not implemented");
  }

  @Override
  public void initializeKeyword(String keyword, IKeywordScope scope, IRaydenReporter reporter) {
    this.keyword = keyword;
    this.scope = scope;
    this.reporter = reporter;

    reporter.reportKeywordBegin(keyword + "(scripted compound)");
  }

  @Override
  public KeywordResult finalizeKeyword() {
    reporter.reportKeywordEnd(keyword + "(scripted compound)");
    return new KeywordResult(true);
  }

  @Override
  public boolean executeBefore() {
    return scope.getVariableAsBoolean("condition");
  }

  @Override
  public boolean executeAfter() {
    reporter.reportKeywordEnd(keyword + "(scripted compound)");
    return false;
  }
}
