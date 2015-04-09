package com.github.thomasfischl.rayden.api;

import java.util.ArrayList;
import java.util.List;

public class MultiReporter implements IRaydenExtReporter {

  private List<IRaydenExtReporter> reporters = new ArrayList<>();
  
  public MultiReporter(IRaydenExtReporter reporter) {
    reporters.add(reporter);
  }
  
  public void addReporter(IRaydenExtReporter reporter){
    reporters.add(reporter);
  }
  
  @Override
  public void log(String msg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void error(String msg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void error(Throwable e) {
    // TODO Auto-generated method stub

  }

  @Override
  public void reportKeywordBegin(String name) {
    // TODO Auto-generated method stub

  }

  @Override
  public void reportKeywordEnd(String name) {
    // TODO Auto-generated method stub

  }

  @Override
  public void reportUnkownKeyword(String name) {
    // TODO Auto-generated method stub

  }

  @Override
  public void reportTestCaseStart(String keywordName) {
    // TODO Auto-generated method stub

  }

  @Override
  public void reportTestCaseEnd(String keywordName) {
    // TODO Auto-generated method stub

  }

  @Override
  public void start() {
    // TODO Auto-generated method stub

  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub

  }

}
