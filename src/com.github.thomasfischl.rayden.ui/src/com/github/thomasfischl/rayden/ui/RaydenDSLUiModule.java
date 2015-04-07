/*
 * generated by Xtext
 */
package com.github.thomasfischl.rayden.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.github.thomasfischl.rayden.ui.outline.RaydenOutlineTreeProvider;
import com.github.thomasfischl.rayden.ui.quickfix.RaydenQuickfixProvider;

/**
 * Use this class to register components to be used within the IDE.
 */
public class RaydenDSLUiModule extends com.github.thomasfischl.rayden.ui.AbstractRaydenDSLUiModule {
  public RaydenDSLUiModule(AbstractUIPlugin plugin) {
    super(plugin);
  }

  public Class<? extends org.eclipse.xtext.ui.editor.outline.IOutlineTreeProvider> bindIOutlineTreeProvider() {
    return RaydenOutlineTreeProvider.class;
  }

  public Class<? extends org.eclipse.xtext.ui.editor.outline.impl.IOutlineTreeStructureProvider> bindIOutlineTreeStructureProvider() {
    return RaydenOutlineTreeProvider.class;
  }

  @Override
  public Class<? extends org.eclipse.xtext.ui.editor.quickfix.IssueResolutionProvider> bindIssueResolutionProvider() {
    return RaydenQuickfixProvider.class;
  }

  // public Class<? extends IHighlightingConfiguration> bindIHighlightingConfiguration() {
  // return TestLangHighlightingConfiguration.class;
  // }
  //
  // public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
  // return TestLangHighlightingConfiguration.class;
  // }
}
