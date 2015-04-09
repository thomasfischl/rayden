package com.github.thomasfischl.rayden.runtime;

import com.github.thomasfischl.rayden.api.keywords.IRaydenReporter;

public class RaydenReporter implements IRaydenReporter{

  private int indentation;

  public void reportKeywordBegin(String name) {
    report("--> begin keyword: " + name);
    indentation++;
  }

  public void reportKeywordEnd(String name) {
    indentation--;
    report("<-- end keyword  : " + name);
  }

  public void reportUnkownKeyword(String name) {
    report("??? unkown keyword: " + name);
  }

  public void log(String msg) {
    report(msg);
  }

  public void error(String msg) {
    report(msg);
  }

  public void error(Throwable e) {
    // TODO improve this implementation
    e.printStackTrace();
  }

  public void reportTestCaseStart(String keywordName) {
    // Nothing to do
  }

  public void reportTestCaseEnd(String keywordName) {
    indentation = 0;
  }

  protected void report(String msg) {
    System.out.println(formatMessae(msg));
  }

  protected String formatMessae(String msg) {
    return "                                                                       ".substring(0, indentation * 2) + msg;
  }

}
