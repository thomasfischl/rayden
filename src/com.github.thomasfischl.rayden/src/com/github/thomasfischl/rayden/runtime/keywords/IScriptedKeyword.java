package com.github.thomasfischl.rayden.runtime.keywords;

import com.github.thomasfischl.rayden.runtime.RaydenReporter;

public interface IScriptedKeyword {

  KeywordResult execute(String keyword, IKeywordScope scope, RaydenReporter reporter);

}
