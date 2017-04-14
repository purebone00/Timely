//Input: Joy Nelson
//Requirements: requires testsuite that runs Promotion before you can run Demotion
//Requirements: 3911ERD_ver21

package test.timely.timesheet;

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

public class EditRowMondayNegativeHours {
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
	  driver.findElement(By.id("j_idt54:j_idt112")).click();
	  driver.get(baseUrl + "/Timely/");
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  driver.findElement(By.id("j_idt16:j_idt18")).click();
	  driver.findElement(By.linkText("Responsible Engineer")).click();
	  driver.findElement(By.linkText("General Utilities")).click();
	  driver.findElement(By.id("j_idt49:j_idt65")).click();
	  driver.findElement(By.id("j_idt49:j_idt50:2:j_idt63")).click();
	  driver.findElement(By.id("j_idt54:j_idt113")).click();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).sendKeys("-1");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).sendKeys("25");
	  new Select(driver.findElement(By.id("j_idt54:j_idt56:0:j_idt66Inner"))).selectByVisibleText("B00000");
	  driver.findElement(By.id("j_idt54:j_idt112")).click();
	  assertEquals("Hours worked on Monday must be between 0 and 24", driver.findElement(By.cssSelector("span.bf-message-detail")).getText());

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

