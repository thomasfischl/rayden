package com.github.thomasfischl.rayden.runtime;

public class RaydenExpressionVariable {

  private final String name;

  public RaydenExpressionVariable(String name) {
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
