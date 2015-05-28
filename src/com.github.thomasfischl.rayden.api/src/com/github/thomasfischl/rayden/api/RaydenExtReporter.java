package com.github.thomasfischl.rayden.api;

public interface RaydenExtReporter extends RaydenReporter {

  void reportKeywordBegin(String name);

  void reportKeywordEnd(String name);

  void reportUnkownKeyword(String name);

  void reportTestCaseStart(String keywordName);

  void reportTestCaseEnd(String keywordName);

  void start();

  void stop();

}
