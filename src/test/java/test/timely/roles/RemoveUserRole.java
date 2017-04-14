//Input: Joy Nelson
//Requirements: requires testsuite that runs Promotion before you can run Demotion
//Requirements: 3911ERD_ver21

package test.timely.roles;

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

public class RemoveUserRole {
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
	  driver.get(baseUrl + "/Timely/faces/login.xhtml?expired=true");
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100001");
	  driver.findElement(By.id("j_idt16:j_idt18")).click();
	  driver.findElement(By.linkText("Project Manager")).click();
	  driver.findElement(By.id("j_idt50:j_idt51:0:j_idt72")).click();
	  driver.findElement(By.id("j_idt55:j_idt56:1:j_idt62")).click();
	  driver.findElement(By.id("j_idt70:j_idt75:2:j_idt77")).click();
	  driver.findElement(By.id("j_idt70:j_idt75:2:j_idt77")).click();
	  driver.findElement(By.name("j_idt103:j_idt107:2:j_idt114")).click();
	  driver.findElement(By.name("j_idt103:j_idt107:3:j_idt114")).click();
	  assertFalse(isElementPresent(By.xpath("//table[@id='j_idt70:j_idt75']/tbody/tr/td[3]")));

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

