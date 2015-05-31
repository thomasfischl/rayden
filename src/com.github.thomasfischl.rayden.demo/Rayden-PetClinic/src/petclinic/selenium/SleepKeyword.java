package petclinic.selenium;

import com.github.thomasfischl.rayden.api.RaydenReporter;
import com.github.thomasfischl.rayden.api.keywords.KeywordResult;
import com.github.thomasfischl.rayden.api.keywords.KeywordScope;
import com.github.thomasfischl.rayden.api.keywords.ScriptedKeyword;

public class SleepKeyword implements ScriptedKeyword {

  @Override
  public KeywordResult execute(String keyword, KeywordScope scope, RaydenReporter reporter) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return new KeywordResult(true);
  }
}
