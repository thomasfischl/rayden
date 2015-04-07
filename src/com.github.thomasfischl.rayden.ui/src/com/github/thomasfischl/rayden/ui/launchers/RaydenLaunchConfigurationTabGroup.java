package com.github.thomasfischl.rayden.ui.launchers;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class RaydenLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {
  @Override
  public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
    ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] { new RaydenTab(),
    // new CommonTab()
    };

    setTabs(tabs);
  } // createTabs
} // PascalLaunchConfigurationTabGroup
