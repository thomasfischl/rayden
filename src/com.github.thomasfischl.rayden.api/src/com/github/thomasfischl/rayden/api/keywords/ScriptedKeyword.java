package com.github.thomasfischl.rayden.api.keywords;

import com.github.thomasfischl.rayden.api.RaydenReporter;

public interface ScriptedKeyword {

  KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter);

}
