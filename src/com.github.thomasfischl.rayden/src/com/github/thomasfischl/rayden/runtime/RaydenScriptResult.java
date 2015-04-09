package com.github.thomasfischl.rayden.runtime;

import java.util.ArrayList;
import java.util.List;

public class RaydenScriptResult {

  private int successTestCount;
  private int failedTestCount;
  private int fatalFailedTestCount;
  private List<String> errorMessages = new ArrayList<>();

  public void setSuccessTestCount(int successTestCount) {
    this.successTestCount = successTestCount;
  }

  public void setFailedTestCount(int failedTestCount) {
    this.failedTestCount = failedTestCount;
  }

  public void setFatalFailedTestCount(int fatalFailedTestCount) {
    this.fatalFailedTestCount = fatalFailedTestCount;
  }

  public int getFailedTestCount() {
    return failedTestCount;
  }

  public int getFatalFailedTestCount() {
    return fatalFailedTestCount;
  }

  public int getSuccessTestCount() {
    return successTestCount;
  }

  public void addErrorMessage(String message) {
    errorMessages.add(message);
  }

  public List<String> getErrorMessages() {
    return errorMessages;
  }

}
