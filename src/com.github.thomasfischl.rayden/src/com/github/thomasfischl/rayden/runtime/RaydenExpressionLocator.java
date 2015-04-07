package com.github.thomasfischl.rayden.runtime;

public class RaydenExpressionLocator {

  private final String name;

  public RaydenExpressionLocator(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
