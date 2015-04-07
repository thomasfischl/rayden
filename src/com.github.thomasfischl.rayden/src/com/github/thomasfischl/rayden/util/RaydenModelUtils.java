package com.github.thomasfischl.rayden.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.KeywordMetatype;
import com.github.thomasfischl.rayden.raydenDSL.Model;

public class RaydenModelUtils {

  public static List<KeywordDecl> getAllKeywords(EObject obj) {
    return getAllKeywords(getRoot(obj));
  }

  public static List<KeywordDecl> getAllKeywords(Model root) {
    List<KeywordDecl> keywords = new ArrayList<>();
    if (root != null) {
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
      name = name.substring(0, name.length() - 2);
    }

    return name;
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
    return keyword.getMetatype() == KeywordMetatype.SCRIPTED;
  }

  public static boolean isTestcaseKeyword(KeywordDecl keyword) {
    return keyword.getMetatype() == KeywordMetatype.TESTCASE;
  }

  // public static void loadImportedKeywords(Model model) {
  // for (Import importDecl : model.getImports()) {
  // String importedNamespace = importDecl.getImportedNamespace();
  //
  // String platformString = model.eResource().getURI().toPlatformString(true);
  //
  // RaydenRuntime runtime = RaydenRuntime.createRuntime();
  // try {
  // File f = new File(importedNamespace);
  // System.out.println(f.getAbsolutePath());
  // runtime.loadLibrary(new FileReader(f));
  // } catch (FileNotFoundException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  //
  // }
  // }

}
