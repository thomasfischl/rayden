package com.github.thomasfischl.rayden.runtime;

import java.io.InputStreamReader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.junit.Test;

import com.github.thomasfischl.rayden.runtime.RaydenScriptEngineFactory;

public class RaydenScriptEngineTest {

  @Test
  public void simpleStringTest() throws ScriptException {

    String source = "";

    source += "keyword<testcase> HelloWorld { \n  Keyword 1                  \n Keyword 2 \n } \n";
    source += "keyword           Keyword 1  { \n  Login with default account              \n } \n";
    source += "keyword           Keyword 2  { \n  Action1                                 \n } \n";

    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(source);
  }

  @Test
  public void simpleFileTest() throws ScriptException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("simple-test.rlg")));
  }

  @Test
  public void demoTest() throws ScriptException {
	  RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
	  ScriptEngine engine = factory.getScriptEngine();
	  engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("demo.rlg")));
  }

  @Test
  public void scriptKeywordTest() throws ScriptException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("scriptKeywordTest.rlg")));
  }
  
  

}
