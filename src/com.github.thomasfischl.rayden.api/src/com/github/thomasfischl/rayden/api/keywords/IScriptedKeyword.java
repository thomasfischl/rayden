package com.github.thomasfischl.rayden.api.keywords;

public interface IScriptedKeyword {

  KeywordResult execute(String keyword, IKeywordScope scope, IRaydenReporter reporter);

}
