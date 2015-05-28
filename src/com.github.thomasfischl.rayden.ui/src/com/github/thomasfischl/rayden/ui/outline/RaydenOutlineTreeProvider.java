package com.github.thomasfischl.rayden.ui.outline;

import org.eclipse.xtext.ui.editor.outline.impl.DefaultOutlineTreeProvider;
import org.eclipse.xtext.ui.editor.outline.impl.DocumentRootNode;
import org.eclipse.xtext.ui.editor.outline.impl.EObjectNode;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.raydenDSL.Model;

/**
 * Customization of the default outline structure.
 * 
 * see http://www.eclipse.org/Xtext/documentation.html#outline
 */
public class RaydenOutlineTreeProvider extends DefaultOutlineTreeProvider {
//
//  // @Inject
//  // private StylerFactory stylerFactory;
//
//  protected void _createChildren(DocumentRootNode parentNode, Model model) {
//    for (KeywordDecl element : model.getKeywords()) {
//      createNode(parentNode, element);
//    }
//  }
//
//  protected void _createChildren(EObjectNode parentNode, KeywordDecl model) {
//    if (model.getKeywordlist() != null) {
//      for (KeywordCall element : model.getKeywordlist().getChildren()) {
//        createNode(parentNode, element);
//      }
//    }
//  }
//
//  protected void _createChildren(EObjectNode parentNode, KeywordCall model) {
//    if (model.getKeywordList() != null && model.getKeywordList().getKeywordlist() != null) {
//      for (KeywordCall element : model.getKeywordList().getKeywordlist().getChildren()) {
//        createNode(parentNode, element);
//      }
//    }
//  }
//
//  // protected boolean _isLeaf(KeywordCall obj) {
//  // return true;
//  // }
//
//  public Object _text(KeywordDecl obj) {
//    // List<String> props = new ArrayList<>();
//    //
//    // for (PropertyDecl prop : obj.getProperties()) {
//    // props.add(prop.getName());
//    // }
//    //
//    // return "" + obj.getName() + "(" + Joiner.on(",").join(props) + ")";
//    return obj.getName();
//  }
//
//  public Object _text(KeywordCall obj) {
//    return obj.getName();
//  }
}
