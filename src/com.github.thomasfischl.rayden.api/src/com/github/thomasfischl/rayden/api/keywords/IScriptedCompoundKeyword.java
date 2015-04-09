package com.github.thomasfischl.rayden.api.keywords;

import com.github.thomasfischl.rayden.api.IRaydenReporter;

public interface IScriptedCompoundKeyword {

  void initializeKeyword(String keyword, IKeywordScope scope, IRaydenReporter reporter);

  boolean executeBefore();

  boolean executeAfter();

  KeywordResult finalizeKeyword();

  void handleError(Throwable e);

}
