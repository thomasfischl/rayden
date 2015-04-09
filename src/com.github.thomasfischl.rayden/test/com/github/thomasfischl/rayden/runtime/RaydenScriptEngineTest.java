package com.github.thomasfischl.rayden.runtime;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import org.junit.Test;

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
    RaydenTestUtil.verifyResult(engine, 0, 0, 1, "No keyword found with name 'Login with default account              '");
  }

  @Test
  public void simpleFileTest() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/simple-test.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 0, 0);
  }

  @Test
  public void demoTest() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/demo.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 0, 0);
  }

  @Test
  public void scriptKeywordTest() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/scriptKeywordTest.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 1, 0, "Scripted Keyword 'Unimplemented Keyword' failed!");
  }

  @Test
  public void testSimpleImportLibrary() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();

    SimpleScriptContext context = new SimpleScriptContext();
    context.setAttribute(RaydenScriptEngine.WORKING_FOLDER, "./test", SimpleScriptContext.ENGINE_SCOPE);
    engine.setContext(context);

    engine.eval(new FileReader("./test/importTest.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 0, 0);
  }

  @Test
  public void testRaydenExpressions() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/expression.rlg"));
    RaydenTestUtil.verifyResult(engine, 4, 0, 0);
  }

  @Test
  public void testScriptedKeyword() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/scriptKeywordTest.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 1, 0, "Scripted Keyword 'Unimplemented Keyword' failed!");
  }

  @Test
  public void testKeywordTypes() throws ScriptException, FileNotFoundException {
    RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
    ScriptEngine engine = factory.getScriptEngine();
    engine.eval(new FileReader("./test/keyword-types.rlg"));
    RaydenTestUtil.verifyResult(engine, 1, 0, 0);
  }
}
