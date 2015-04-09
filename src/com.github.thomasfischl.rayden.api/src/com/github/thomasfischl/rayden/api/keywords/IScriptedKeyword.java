package com.github.thomasfischl.rayden.api.keywords;

import com.github.thomasfischl.rayden.api.IRaydenReporter;

public interface IScriptedKeyword {

  KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter);

}
