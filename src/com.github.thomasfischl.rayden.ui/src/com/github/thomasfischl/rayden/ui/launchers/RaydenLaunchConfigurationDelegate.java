package com.github.thomasfischl.rayden.ui.launchers;

import java.io.InputStreamReader;

import javax.script.ScriptException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

import com.github.thomasfischl.rayden.runtime.RaydenReporter;
import com.github.thomasfischl.rayden.runtime.RaydenScriptEngine;
import com.github.thomasfischl.rayden.runtime.RaydenScriptEngineFactory;
import com.github.thomasfischl.rayden.ui.helpers.ConsoleDisplayManager;
import com.github.thomasfischl.rayden.ui.helpers.LaunchHelper;
import com.github.thomasfischl.rayden.ui.helpers.UiHelpers;

public class RaydenLaunchConfigurationDelegate extends LaunchConfigurationDelegate {

  @Override
  public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
    // clear problem view
    UiHelpers.removeMarkers();

    // boolean isTest = false;
    // String commandline = "";
    // String srcpath = "/src/";
    String projectname = configuration.getAttribute(RaydenLaunchConfiguration.RESOURCE_PROJECT_NAME, new String());
    // String projectpath = configuration.getAttribute(KyLangLaunchConfiguration.RESOURCE_PROJECT_PATH, new String());
    String programpath = configuration.getAttribute(RaydenLaunchConfiguration.TEST_FILE, new String());
    // String programname = programpath.substring(0, programpath.lastIndexOf("."));

    IResource resource = LaunchHelper.GetResourceFromFile(ResourcesPlugin.getWorkspace().getRoot().getProject(projectname), programpath,
        false);
    if (resource instanceof IFile) {

      RaydenReporterExtension reporter = new RaydenReporterExtension();
      try {
        RaydenScriptEngineFactory factory = new RaydenScriptEngineFactory();
        RaydenScriptEngine engine = (RaydenScriptEngine) factory.getScriptEngine();
        engine.setReporter(reporter);
        engine.eval(new InputStreamReader(((IFile) resource).getContents()));
      } catch (ScriptException e) {
        reporter.error(e);
        UiHelpers.reportError(ResourcesPlugin.getWorkspace().getRoot(), 0, 0, "Script Error: " + e.getMessage());
      }
    } else {
      UiHelpers.reportError(ResourcesPlugin.getWorkspace().getRoot(), 0, 0, "File '" + programpath + "' does not exists!");
    }
  }

  private final class RaydenReporterExtension extends RaydenReporter {

    private ConsoleDisplayManager cdm;

    public RaydenReporterExtension() {
      cdm = UiHelpers.getConsoleDisplayManager();
      cdm.clear();
    }

    @Override
    protected void report(String msg) {
      cdm.println(formatMessae(msg));
    }
  }
}
