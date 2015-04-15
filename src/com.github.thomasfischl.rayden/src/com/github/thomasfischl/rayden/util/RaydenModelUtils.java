package com.github.thomasfischl.rayden.util;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordType;
import com.github.thomasfischl.rayden.raydenDSL.LocatorDecl;
import com.github.thomasfischl.rayden.raydenDSL.LocatorPartDecl;
import com.github.thomasfischl.rayden.raydenDSL.Model;
import com.github.thomasfischl.rayden.raydenDSL.ObjectRepositoryDecl;
import com.github.thomasfischl.rayden.raydenDSL.ObjectRepositryControlDecl;
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

  public static List<ObjectRepositryControlDecl> getAllApplications(EObject obj) {
    Model model = getRoot(obj);

    List<ObjectRepositryControlDecl> controls = new ArrayList<>();

    EList<ObjectRepositoryDecl> ors = model.getObjectrepositories();
    for (ObjectRepositoryDecl or : ors) {
      for (ObjectRepositryControlDecl ctrl : or.getControls()) {
        if ("application".equals(ctrl.getType().getType())) {
          controls.add(ctrl);
        }
      }
    }

    return controls;
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

  public static ObjectRepositryControlDecl getControl(LocatorDecl locator) {
    List<ObjectRepositryControlDecl> applications = getAllApplications(locator);
    EList<LocatorPartDecl> parts = locator.getParts();

    for (ObjectRepositryControlDecl application : applications) {
      ObjectRepositryControlDecl control = getControl(application, parts, 0);
      if (control != null) {
        return control;
      }
    }
    return null;
  }

  private static ObjectRepositryControlDecl getControl(ObjectRepositryControlDecl parentControl, EList<LocatorPartDecl> parts, int index) {
    if (parts.size() <= index) {
      return parentControl;
    }

    LocatorPartDecl part = parts.get(index);
    if (part.getName().trim().equals(parentControl.getName().trim())) {
      for (ObjectRepositryControlDecl control : parentControl.getControls()) {
        ObjectRepositryControlDecl result = getControl(control, parts, index + 1);
        if (result != null) {
          return result;
        }
      }
      return parentControl;
    } else {
      return null;
    }
  }

}
