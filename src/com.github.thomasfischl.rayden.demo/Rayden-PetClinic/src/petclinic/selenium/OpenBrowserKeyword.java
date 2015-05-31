package petclinic.selenium;

import org.openqa.selenium.WebDriver;

import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;

public class OpenBrowserKeyword implements ScriptedKeyword {

	@Override
	public KeywordResult execute(String keyword, KeywordScope scope,
			RaydenReporter reporter) {

		String browserType = scope.getVariableAsString("browserType");
		String url = scope.getVariableAsString("url");

		WebDriver driver = Selenium.getInstance().initializeDriver(browserType);
		driver.navigate().to(url);
		return new KeywordResult(true);
	}
}
