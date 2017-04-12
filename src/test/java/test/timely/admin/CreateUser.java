//Input: Joy Nelson
//Requirements: requires testsuite that runs Promotion before you can run Demotion
//Requirements: 3911ERD_ver21

package test.timely.admin;

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

public class CreateUser {
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
  public void CreateUserTest() throws Exception {
	  driver.get(baseUrl + "/Timely/");
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	    driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("00001");
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    driver.findElement(By.id("j_idt48:j_idt49")).click();
	    driver.findElement(By.id("input_addUser:firstName")).clear();
	    driver.findElement(By.id("input_addUser:firstName")).sendKeys("Fred");
	    driver.findElement(By.id("input_addUser:lastName")).clear();
	    driver.findElement(By.id("input_addUser:lastName")).sendKeys("Left");
	    driver.findElement(By.id("input_addUser:lastName")).clear();
	    driver.findElement(By.id("input_addUser:lastName")).sendKeys("LeftLeft");
	    driver.findElement(By.id("input_addUser:j_idt54")).clear();
	    driver.findElement(By.id("input_addUser:j_idt54")).sendKeys("IS");
	    driver.findElement(By.id("input_addUser:password")).clear();
	    driver.findElement(By.id("input_addUser:password")).sendKeys("Comp@4911");
	    driver.findElement(By.id("addUser:addButton")).click();
	    assertEquals("id=input_j_idt16:inputUserName", closeAlertAndGetItsText());
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    driver.findElement(By.linkText("3")).click();
	    assertEquals("Fred LeftLeft", driver.findElement(By.xpath("//table[@id='j_idt52:j_idt53']/tbody/tr[21]/td[2]")).getText());
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

