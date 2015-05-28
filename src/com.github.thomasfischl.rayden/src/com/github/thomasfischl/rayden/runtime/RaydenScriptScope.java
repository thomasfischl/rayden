package com.github.thomasfischl.rayden.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedCompoundKeyword;
import com.github.thomasfischl.rayden.raydenDSL.KeywordCall;
import com.github.thomasfischl.rayden.raydenDSL.KeywordDecl;

class RaydenScriptScope implements KeywordScope {
  private int curpos = 0;
  private final KeywordDecl keyword;
  private final KeywordCall keywordCall;
  private final List<KeywordCall> list;
  private final RaydenScriptScope parent;
  private final Map<String, Object> variables = new HashMap<>();
  private ScriptedCompoundKeyword scriptedCompoundKeyword;

  public RaydenScriptScope(RaydenScriptScope parent, KeywordCall keywordCall, KeywordDecl keyword) {
    super();
    this.parent = parent;
    this.keywordCall = keywordCall;
    this.keyword = keyword;
    list = null;
  }

  public RaydenScriptScope(RaydenScriptScope parent, List<KeywordCall> list) {
    super();
    this.parent = parent;
    this.list = list;
    keyword = null;
    keywordCall = null;
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

  public KeywordCall getKeywordCall() {
    return keywordCall;
  }

  public void setScriptedCompoundKeyword(ScriptedCompoundKeyword scriptedCompoundKeyword) {
    this.scriptedCompoundKeyword = scriptedCompoundKeyword;
  }

  public ScriptedCompoundKeyword getScriptedCompoundKeyword() {
    return scriptedCompoundKeyword;
  }

  public void resetCursor() {
    curpos = 1;
  }

  @Override
  public void setVariable(String name, Object value) {
    if (value instanceof Integer) {
      variables.put(name, ((Integer) value).doubleValue());
    } else {
      variables.put(name, value);
    }
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
  public<T> T getVariable(String name, Class<T> clazz) {
    if (!variables.containsKey(name)) {
      if (parent != null) {
        return parent.getVariable(name, clazz);
      }
      throw new RaydenScriptException("Variable '" + name + "' is not declared.");
    }
    return clazz.cast(variables.get(name));
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
  public int getVariableAsInteger(String name) {
    Object value = getVariable(name);
    if (value instanceof Integer) {
      return (Integer) value;
    } else if (value instanceof Double) {
      return Long.valueOf(Math.round((Double) value)).intValue();
    } else {
      throw new RaydenScriptException("Variable '" + name + "' is not from type 'number'.");
    }
  }

  @Override
  public String getVariableAsString(String name) {
    Object value = getVariable(name);
    if (value instanceof String) {
      return (String) value;
    } else {
      throw new RaydenScriptException("Variable '" + name + "' is not from type 'string'.");
    }
  }

  @Override
  public Set<String> getVariableNames() {
    return variables.keySet();
  }

  public RaydenScriptScope getParent() {
    return parent;
  }
}