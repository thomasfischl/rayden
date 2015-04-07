package com.github.thomasfischl.rayden.runtime.keywords;

public class KeywordResult {

  private final boolean success;
  
  public KeywordResult(boolean success) {
    this.success = success;
  }
  
  public boolean isSuccess() {
    return success;
  }
  
}
