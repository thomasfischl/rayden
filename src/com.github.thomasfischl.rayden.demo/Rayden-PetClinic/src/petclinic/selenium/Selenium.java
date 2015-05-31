package petclinic.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Selenium {

  private static final int SLEEP_TIME = 1000;

  private static Selenium instance;

  private WebDriver driver;

  private Selenium() {
  }

  public WebDriver initializeDriver(String browserType) {
    driver = new FirefoxDriver();
    return driver;
  }

  public WebDriver getCurrentDriver() {
    if (driver == null) {
      throw new IllegalStateException("Driver is not active.");
    }
    return driver;
  }

  public WebElement findElement(String xpath) {
    InterruptedException lastException = null;
    for (int i = 0; i < 3; i++) {
      try {
        return getCurrentDriver().findElement(By.xpath(xpath));
      } catch (NoSuchElementException e) {
        // nothing to do
      }

      try {
        Thread.sleep(SLEEP_TIME);
      } catch (InterruptedException e) {
        lastException = e;
      }
      System.out.println("retry");
    }
    if (lastException != null) {
      throw new NoSuchElementException("No element with xpath '" + xpath + "' found.", lastException);
    }
    throw new NoSuchElementException("No element with xpath '" + xpath + "' found.");
  }

  public static Selenium getInstance() {
    if (instance == null) {
      instance = new Selenium();
    }
    return instance;
  }

}
