package com.github.thomasfischl.rayden.api.keywords;

public interface IScriptedCompoundKeyword {

  void initializeKeyword(String keyword, IKeywordScope scope, IRaydenReporter reporter);

  boolean executeBefore();

  boolean executeAfter();

  KeywordResult finalizeKeyword();

  void handleError(Throwable e);

}
