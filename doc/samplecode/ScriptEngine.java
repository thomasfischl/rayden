public class RaydenScriptEngine extends AbstractScriptEngine {

  ...
  
  @Override
  public Object eval(Reader reader, ScriptContext context) 
    throws ScriptException {
    
    RaydenRuntime runtime = RaydenRuntime.createRuntime();
    if (reporter != null) {
      runtime.setReporter(reporter);
    }

    if (context.getAttribute(WORKING_FOLDER, ScriptContext.ENGINE_SCOPE)
        != null) {
      runtime.setWorkingFolder(new File(String.valueOf(context.
        getAttribute(WORKING_FOLDER, ScriptContext.ENGINE_SCOPE))));
    }

    runtime.loadRaydenFile(reader);
    RaydenScriptResult result = runtime.executeAllTestSuites();
    getContext().setAttribute(TEST_RESULT, result, 
      ScriptContext.ENGINE_SCOPE);
    return result;
  } //eval

  ...
} //RaydenScriptEngine
