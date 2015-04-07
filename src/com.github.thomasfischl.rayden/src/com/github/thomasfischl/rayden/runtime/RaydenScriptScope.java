package com.github.thomasfischl.rayden.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;
import com.github.thomasfischl.rayden.runtime.keywords.IKeywordScope;

class RaydenScriptScope implements IKeywordScope {
  private int curpos = 0;
  private final KeywordDecl keyword;
  private final List<KeywordCall> list;
  private final RaydenScriptScope parent;
  private final Map<String, Object> variables = new HashMap<>();

  public RaydenScriptScope(RaydenScriptScope parent, KeywordDecl keyword) {
    super();
    this.parent = parent;
    this.keyword = keyword;
    list = null;
  }

  public RaydenScriptScope(RaydenScriptScope parent, List<KeywordCall> list) {
    super();
    this.parent = parent;
    this.list = list;
    keyword = null;
  }

  Object getNextOpt() {
    if (keyword != null && curpos < 2) {
      curpos++;
      return keyword;
    }
    if (list != null && curpos < list.size()) {
      return list.get(curpos++);
    }
    return null;
  }

  boolean isKeywordBegin() {
    if (keyword == null) {
      throw new IllegalStateException("This method is only allowd for a keyword scope");
    }

    return curpos == 1;
  }

  boolean isKeywordEnd() {
    if (keyword == null) {
      throw new IllegalStateException("This method is only allowd for a keyword scope");
    }
    return curpos == 2;
  }

  @Override
  public void addVariable(String name, Object value) {
    variables.put(name, value);
  }

  @Override
  public Object getVariable(String name) {
    if (!variables.containsKey(name)) {
      if (parent != null) {
        return parent.getVariable(name);
      }
      throw new RaydenScriptException("Variable '" + name + "' is not declared.");
    }
    return variables.get(name);
  }

  @Override
  public boolean getVariableAsBoolean(String name) {
    Object value = getVariable(name);
    if (value instanceof Boolean) {
      return (boolean) value;
    } else {
      throw new RaydenScriptException("Variable '" + name + "' is not from type 'boolean'.");
    }
  }

  @Override
  public Set<String> getVariableNames() {
    return variables.keySet();
  }
}