package petclinic.selenium;

import org.openqa.selenium.WebElement;

import com.github.thomasfischl.rayden.api.RaydenExpressionLocator;
import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;

public class VerifyTextKeyword implements ScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter) {

    RaydenExpressionLocator locator = (RaydenExpressionLocator) scope.getVariable("locator");
    String text = scope.getVariableAsString("text");

    reporter.log("locator:" + locator.getEvalLocator());
    reporter.log("text:" + text);
    
    WebElement element = Selenium.getInstance().findElement(locator.getEvalLocator());
    String elementText = element.getText();

    text = text.trim();
    elementText = elementText.trim();
    reporter.log("Verify Text: '" + text + "'='" + elementText + "'");

    return new KeywordResult(text.equals(elementText));
  }
}
