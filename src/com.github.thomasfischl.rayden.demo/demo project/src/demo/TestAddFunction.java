package demo;

import com.github.thomasfischl.rayden.api.IRaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.IKeywordScope;
import com.github.thomasfischl.rayden.api.keywords.IScriptedKeyword;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;

public class TestAddFunction implements IScriptedKeyword {

	@Override
	public KeywordResult execute(String name, IKeywordScope scope,
			IRaydenReporter reporter) {

		// TODO implement me

		return new KeywordResult(true);
	}

}
