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

public class EditTimesheetRowInvalidData {
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
	  driver.findElement(By.linkText("General Utilities")).click();
	  driver.findElement(By.id("j_idt49:j_idt50:2:j_idt63")).click();	
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:satHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:satHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:satHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:sunHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:sunHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:monHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:tuesHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:tuesHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:wedHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:wedHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:thursHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:thursHours")).sendKeys("$");
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:friHours")).clear();
	  driver.findElement(By.id("input_j_idt54:j_idt56:0:friHours")).sendKeys("$");
	  driver.findElement(By.name("j_idt54:j_idt56:0:j_idt102")).clear();
	  driver.findElement(By.name("j_idt54:j_idt56:0:j_idt102")).sendKeys("$");
	  driver.findElement(By.id("j_idt54:j_idt112")).click();
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is((driver.findElement(By.cssSelector("span.bf-message-detail")).getText())));
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is(not(driver.findElement(By.xpath("//div[@id='j_idt54:j_idt129']/div/span[2]/span[2]")).getText())));
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is(not(driver.findElement(By.xpath("//div[@id='j_idt54:j_idt129']/div/span[3]/span[2]")).getText())));
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is(not(driver.findElement(By.xpath("//div[@id='j_idt54:j_idt129']/div/span[4]/span[2]")).getText())));
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is(not(driver.findElement(By.xpath("//div[@id='j_idt54:j_idt129']/div/span[6]/span[2]")).getText())));
	  assertThat("Must follow the #00.0 format and be between 0 and 24", is(not(driver.findElement(By.cssSelector("span.bf-message-summary")).getText())));

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

