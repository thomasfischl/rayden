package com.github.thomasfischl.rayden.runtime;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;

public class RaydenScriptEngine extends AbstractScriptEngine {

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
    runtime.loadLibrary(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("control-structure.rlg")));
    runtime.loadLibrary(reader);
    runtime.executeAllTestcases();
    return null;
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
