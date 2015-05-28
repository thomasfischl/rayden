package com.github.thomasfischl.rayden.api;

public interface RaydenReporter {

  void log(String msg);

  void error(String msg);

  void error(Throwable e);

}
