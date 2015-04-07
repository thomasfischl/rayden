package com.github.thomasfischl.rayden.runtime;

public class RaydenExpressionSymbol {

  private final String name;

  public RaydenExpressionSymbol(String name) {
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
