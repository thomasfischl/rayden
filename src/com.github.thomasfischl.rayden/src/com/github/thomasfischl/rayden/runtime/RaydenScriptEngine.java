package com.github.thomasfischl.rayden.runtime;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class RaydenScriptEngine extends AbstractScriptEngine {

  public final static String WORKING_FOLDER = "RAYDEN_WORKING_FOLDER";

  public final static String TEST_RESULT = "RAYDEN_TEST_RESULT";

  private ScriptEngineFactory factory;

  private RaydenReporter reporter;

  public RaydenScriptEngine(ScriptEngineFactory factory) {
    this.factory = factory;
  }

  public void setReporter(RaydenReporter reporter) {
    this.reporter = reporter;
  }

  @Override
  public Object eval(String script, ScriptContext context) throws ScriptException {
    return eval(new StringReader(script), context);
  }

  @Override
  public Object eval(Reader reader, ScriptContext context) throws ScriptException {
    RaydenRuntime runtime = RaydenRuntime.createRuntime();
    if (reporter != null) {
      runtime.setReporter(reporter);
    }

    if (context.getAttribute(WORKING_FOLDER, ScriptContext.ENGINE_SCOPE) != null) {
      runtime.setWorkingFolder(new File(String.valueOf(context.getAttribute(WORKING_FOLDER, ScriptContext.ENGINE_SCOPE))));
    }

    runtime.loadRaydenFile(reader);
    RaydenScriptResult result = runtime.executeAllTestSuites();
    getContext().setAttribute(TEST_RESULT, result, ScriptContext.ENGINE_SCOPE);
    return result;
  }

  @Override
  public Bindings createBindings() {
    throw new IllegalStateException("Not implemented");
  }

  @Override
  public ScriptEngineFactory getFactory() {
    return factory;
  }

}
