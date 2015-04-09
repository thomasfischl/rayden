package com.github.thomasfischl.rayden.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.EObject;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordType;
import com.github.thomasfischl.rayden.raydenDSL.Model;
import com.github.thomasfischl.rayden.runtime.RaydenRuntime;

public class RaydenModelUtils {

  public static List<KeywordDecl> getAllKeywords(EObject obj) {
    return getAllKeywords(getRoot(obj));
  }

  public static List<KeywordDecl> getAllKeywords(Model root) {
    List<KeywordDecl> keywords = new ArrayList<>();
    if (root != null) {
      keywords.addAll(loadImportedKeywords(root));
      for (KeywordDecl keyword : root.getKeywords()) {
        keywords.add(keyword);
      }
    }
    return keywords;
  }

  public static Model getRoot(EObject obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof Model) {
      return (Model) obj;
    }

    return getRoot(obj.eContainer());
  }

  public static String normalizeKeyword(String name) {
    if (name == null) {
      return null;
    }
    name = name.trim();

    if (name.endsWith("!") || name.endsWith(".") || name.endsWith("?")) {
      name = name.substring(0, name.length() - 1);
    }

    return name.trim();
  }

  public static KeywordDecl getKeywordDecl(EObject obj) {
    if (obj == null) {
      return null;
    }
    if (obj instanceof KeywordDecl) {
      return (KeywordDecl) obj;
    }

    return getKeywordDecl(obj.eContainer());
  }

  public static boolean isInlineKeyword(KeywordCall keyword) {
    return keyword.getKeywordList() != null && keyword.getParameters() == null;
  }

  public static boolean isScriptedKeyword(KeywordDecl keyword) {
    return keyword.getScript() != null;
  }

  public static boolean isTestSuiteKeyword(KeywordDecl keyword) {
    return keyword.getType() == KeywordType.TESTSUITE;
  }

  public static Collection<KeywordDecl> loadImportedKeywords(Model model) {
    if (model.eResource() != null) {
      try {
        RaydenRuntime runtime = RaydenRuntime.createRuntime();
        File workingFolder = new File(".");

        // define special workspace for eclipse
        IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(model.eResource().getURI().toPlatformString(true));
        if (resource != null && resource.getProject() != null) {
          IProject project = resource.getProject();
          workingFolder = new File(project.getLocationURI());
        }

        System.out.println("Set working folder: " + workingFolder);
        runtime.setWorkingFolder(workingFolder);
        runtime.loadRaydenFile(new FileReader(new File(workingFolder, resource.getName())));
        return runtime.getDefinedImportedKeywords().values();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
    return new ArrayList<KeywordDecl>();
  }

}
