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

public class SubmitTimesheetAgain {
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
	  driver.get(baseUrl + "/Timely/faces/employee/employeefunctions.xhtml");
	  driver.findElement(By.linkText("Sign Out")).click();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  driver.findElement(By.id("j_idt16:j_idt18")).click();
	  driver.findElement(By.linkText("General Utilities")).click();
	  driver.findElement(By.cssSelector("input.form-control.input-sm")).clear();
	  driver.findElement(By.cssSelector("input.form-control.input-sm")).sendKeys("");
	  driver.findElement(By.id("j_idt49:j_idt50:3:j_idt63")).click();
	  driver.findElement(By.id("j_idt54:j_idt115")).click();
	  driver.findElement(By.id("j_idt54:j_idt120")).click();
	  driver.findElement(By.linkText("General Utilities")).click();
	  
	  
	  driver.findElement(By.linkText("General Utilities")).click();
	  driver.findElement(By.cssSelector("input.form-control.input-sm")).clear();
	  driver.findElement(By.cssSelector("input.form-control.input-sm")).sendKeys("");
	  driver.findElement(By.id("j_idt49:j_idt50:3:j_idt63")).click();
	  driver.findElement(By.id("j_idt54:j_idt115")).click();
	  driver.findElement(By.id("j_idt54:j_idt120")).click();
	  driver.findElement(By.linkText("General Utilities")).click();
	  
	  //check to see if we have a pending eye icon
	  assertTrue(isElementPresent(By.xpath("//table[@id='j_idt49:j_idt50']/tbody/tr/td[3]")));
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

