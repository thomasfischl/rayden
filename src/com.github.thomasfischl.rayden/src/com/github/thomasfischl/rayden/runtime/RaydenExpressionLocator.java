package com.github.thomasfischl.rayden.runtime;

public class RaydenExpressionLocator {

  private final String orgLocator;
  private final String evalLocator;

  public RaydenExpressionLocator(String orgLocator, String evalLocator) {
    this.orgLocator = orgLocator;
    this.evalLocator = evalLocator;
  }

  public String getOrgLocator() {
    return orgLocator;
  }

  public String getEvalLocator() {
    return evalLocator;
  }

  @Override
  public String toString() {
    if (evalLocator != null) {
      return orgLocator + "[" + evalLocator + "]";
    } else {
      return orgLocator;
    }
  }
}
