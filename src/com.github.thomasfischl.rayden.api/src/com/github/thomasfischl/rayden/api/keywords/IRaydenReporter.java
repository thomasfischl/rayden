package com.github.thomasfischl.rayden.api.keywords;

public interface IRaydenReporter {

  void reportKeywordBegin(String name);

  void reportKeywordEnd(String name);

  void reportUnkownKeyword(String name);

  void log(String msg);

  void error(String msg);

  void error(Throwable e);

  void reportTestCaseStart(String keywordName);

  void reportTestCaseEnd(String keywordName);

}
