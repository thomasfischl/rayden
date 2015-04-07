package com.github.thomasfischl.rayden.runtime.keywords;

import java.util.Set;

public interface IKeywordScope {

  Object getVariable(String name);

  boolean getVariableAsBoolean(String name);

  Set<String> getVariableNames();

  void addVariable(String name, Object value);

}
