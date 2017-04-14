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

public class AddARowEditDataNotSaveData {
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
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100004");
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  driver.findElement(By.id("j_idt16:j_idt18")).click();
	  driver.findElement(By.linkText("General Utilities")).click();
	  driver.findElement(By.id("j_idt49:j_idt50:0:j_idt63")).click();
	  driver.findElement(By.id("j_idt54:j_idt114")).click();
	  driver.findElement(By.id("j_idt54:j_idt113")).click();
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:monHours")).clear();
	  
	  //editing the row before hand, so don't need to reset database after
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:monHours")).sendKeys("1");
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:tuesHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:tuesHours")).sendKeys("1");
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:wedHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:wedHours")).sendKeys("1");
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:thursHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:thursHours")).sendKeys("1");
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:friHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:3:friHours")).sendKeys("1");
	  
	  //adding  a new row, click on a block w/o chaning the value, then just click save again
	  driver.findElement(By.id("j_idt54:j_idt112")).click();
	  driver.findElement(By.id("j_idt54:j_idt113")).click();
	  driver.findElement(By.id("j_idt54:j_idt112")).click();
	  
	  //verify that the values are still ones
	  assertEquals("1", driver.findElement(By.xpath("//table[@id='j_idt54:j_idt56']/tbody/tr/td[6]")).getText());
	  assertEquals("1", driver.findElement(By.xpath("//table[@id='j_idt54:j_idt56']/tbody/tr/td[7]")).getText());
	  assertEquals("1", driver.findElement(By.xpath("//table[@id='j_idt54:j_idt56']/tbody/tr/td[8]")).getText());
	  assertEquals("1", driver.findElement(By.xpath("//table[@id='j_idt54:j_idt56']/tbody/tr/td[9]")).getText());
	  assertEquals("1", driver.findElement(By.xpath("//table[@id='j_idt54:j_idt56']/tbody/tr/td[10]")).getText());

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

