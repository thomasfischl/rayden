package com.github.thomasfischl.rayden.api.keywords;

import com.github.thomasfischl.rayden.api.RaydenReporter;

public interface ScriptedCompoundKeyword {

  void initializeKeyword(String keyword, KeywordScope scope, RaydenReporter reporter);

  boolean executeBefore();

  boolean executeAfter();

  KeywordResult finalizeKeyword();

  void handleError(Throwable e);

}
