package petclinic.selenium;

import org.openqa.selenium.WebElement;

import com.github.thomasfischl.rayden.api.RaydenExpressionLocator;
import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;

public class TypeTextKeyword implements ScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter) {

    RaydenExpressionLocator locator = (RaydenExpressionLocator) scope.getVariable("locator");
    String text = scope.getVariableAsString("text");

    reporter.log("locator:" + locator);
    reporter.log("text:" + text);
    
    WebElement element = Selenium.getInstance().findElement(locator.getEvalLocator());
    element.sendKeys(text);
    reporter.log("Type Text '" + text + "' to locator " + locator.getEvalLocator() + "'");

    return new KeywordResult(true);
  }
}
