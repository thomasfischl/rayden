package com.github.thomasfischl.rayden.runtime;

import java.util.List;

import javax.script.ScriptEngine;

import org.junit.Assert;

public class RaydenTestUtil {

  public static void verifyResult(ScriptEngine engine, int successTests, int failedTests, int fatalFailedTests, String... messages) {
    Object result = engine.getContext().getAttribute(RaydenScriptEngine.TEST_RESULT);
    if (result instanceof RaydenScriptResult) {
      RaydenScriptResult resultObj = (RaydenScriptResult) result;

      Assert.assertEquals(successTests, resultObj.getSuccessTestCount());
      Assert.assertEquals(failedTests, resultObj.getFailedTestCount());
      Assert.assertEquals(fatalFailedTests, resultObj.getFatalFailedTestCount());

      if (messages != null) {
        List<String> errorMessages = resultObj.getErrorMessages();
        for (int i = 0; i < messages.length; i++) {
          Assert.assertEquals(messages[i], errorMessages.get(i));
        }
      }

    } else {
      throw new IllegalStateException("No result found. Object '" + result + "'");
    }
  }

}
