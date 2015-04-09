package com.github.thomasfischl.rayden.api;

public interface IRaydenExtReporter extends IRaydenReporter {

  void reportKeywordBegin(String name);

  void reportKeywordEnd(String name);

  void reportUnkownKeyword(String name);

  void reportTestCaseStart(String keywordName);

  void reportTestCaseEnd(String keywordName);

  void start();

  void stop();

}
