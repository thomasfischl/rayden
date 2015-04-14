package com.github.thomasfischl.rayden.api.keywords;

import java.util.Set;

public interface IKeywordScope {

  Object getVariable(String name);

  <T> T getVariable(String name, Class<T> clazz);

  boolean getVariableAsBoolean(String name);

  int getVariableAsInteger(String name);

  String getVariableAsString(String name);

  Set<String> getVariableNames();

  void setVariable(String name, Object value);

}
