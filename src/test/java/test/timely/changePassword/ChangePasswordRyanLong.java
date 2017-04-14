//Input: Joy Nelson
//Requirements: requires testsuite that runs Promotion before you can run Demotion
//Requirements: 3911ERD_ver21

package test.timely.changePassword;

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

public class ChangePasswordRyanLong {
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
	    driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    
	    
	    driver.findElement(By.linkText("Change Password")).click();
	    driver.findElement(By.id("input_j_idt50:oldPassword")).clear();
	    //should not be able to store 5000 character password
	    driver.findElement(By.id("input_j_idt50:oldPassword")).sendKeys("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");
	    driver.findElement(By.id("j_idt50:j_idt56")).click();
	    driver.findElement(By.id("input_j_idt50:oldPassword")).clear();
	    driver.findElement(By.id("input_j_idt50:oldPassword")).sendKeys("");
	    assertEquals("The old Password must have been at least 8 characters and at most 16 characters.", driver.findElement(By.cssSelector("span.bf-message-detail")).getText());
	    assertEquals("The old Password must have been at least 8 characters and at most 16 characters.", driver.findElement(By.cssSelector("span.bf-message-detail")).getText());
	    assertEquals("The confirmed new Password must have at least 8 characters and at most 16 characters.", driver.findElement(By.xpath("//div[@id='j_idt50:j_idt57']/div/span[3]/span[2]")).getText());
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

