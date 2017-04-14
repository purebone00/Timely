//Input: Joy Nelson
//Requirements: requires testsuite that runs Promotion before you can run Demotion
//Requirements: 3911ERD_ver21

package test.timely.ResponsbileEngineer;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class EstimatedWorkRemainingNegativeNumbers {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
	  
	System.setProperty("webdriver.gecko.driver","c:\\geckodriver.exe");
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void loginAdmin() throws Exception {
	  driver.get(baseUrl + "/Timely/faces/index.xhtml");
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  driver.findElement(By.linkText("Responsible Engineer")).click();
	  driver.findElement(By.id("j_idt48:0:j_idt57:j_idt58")).click();
	  driver.findElement(By.linkText("Strong Random Password Generator")).click();
	  driver.findElement(By.id("lst-ib")).clear();
	  driver.findElement(By.id("lst-ib")).sendKeys("generate long string");
	  // ERROR: Caught exception [ERROR: Unsupported command [selectWindow | null | ]]
	  driver.findElement(By.id("input_j_idt59:j_idt60:0:estimate")).clear();
	  driver.findElement(By.id("input_j_idt59:j_idt60:0:estimate")).sendKeys("-1");
	  driver.findElement(By.id("j_idt59:j_idt84")).click();
	  driver.findElement(By.id("j_idt48:0:j_idt57:j_idt58")).click();
	  // Warning: assertTextNotPresent may require manual changes
	  assertFalse(driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*id=input_j_idt59:j_idt60:0:estimate[\\s\\S]*$"));

  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}

