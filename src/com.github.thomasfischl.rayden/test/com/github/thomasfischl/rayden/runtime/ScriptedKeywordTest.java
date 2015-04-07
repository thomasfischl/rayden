package com.github.thomasfischl.rayden.runtime;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.Test;

import com.github.thomasfischl.rayden.runtime.RaydenScriptEngineFactory;

public class ScriptedKeywordTest {

  @Test
  public void scriptKeywordTest() throws ScriptException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("scriptKeywordTest.rlg")));
  }

}
