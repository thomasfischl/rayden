package com.github.thomasfischl.rayden.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiReporter implements RaydenExtReporter {

  private List<RaydenExtReporter> reporters = new ArrayList<>();

  public MultiReporter(RaydenExtReporter... reporter) {
    reporters = Arrays.asList(reporter);
  }

  @Override
  public void log(String msg) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.log(msg);
    }
  }

  @Override
  public void error(String msg) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.error(msg);
    }
  }

  @Override
  public void error(Throwable e) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.error(e);
    }
  }

  @Override
  public void reportKeywordBegin(String name) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.reportKeywordBegin(name);
    }
  }

  @Override
  public void reportKeywordEnd(String name) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.reportKeywordEnd(name);
    }
  }

  @Override
  public void reportUnkownKeyword(String name) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.reportUnkownKeyword(name);
    }
  }

  @Override
  public void reportTestCaseStart(String keywordName) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.reportTestCaseStart(keywordName);
    }
  }

  @Override
  public void reportTestCaseEnd(String keywordName) {
    for (RaydenExtReporter reporter : reporters) {
      reporter.reportTestCaseEnd(keywordName);
    }
  }

  @Override
  public void start() {
    for (RaydenExtReporter reporter : reporters) {
      reporter.start();
    }
  }

  @Override
  public void stop() {
    for (RaydenExtReporter reporter : reporters) {
      reporter.stop();
    }
  }

}
