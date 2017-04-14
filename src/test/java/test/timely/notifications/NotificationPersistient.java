//Double checking if the persisted value is still persisted after logging back in
//Requirements: 3911ERD_ver21

package test.timely.notifications;

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

public class NotificationPersistient {
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
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  
	  
	  driver.get(baseUrl + "/Timely/faces/index.xhtml");
	  driver.findElement(By.linkText("View Details")).click();
	  driver.findElement(By.id("j_idt49:j_idt50:1:j_idt63")).click();
	  
	  //check !
	  driver.get(baseUrl + "//table[@id='j_idt54:j_idt56']/thead/tr/th[12]");

	  
	  assertEquals("0", driver.findElement(By.id("dtLdropdown")).getText());
	  driver.findElement(By.id("j_idt54:j_idt115")).click();
	  driver.findElement(By.id("j_idt54:j_idt120")).click();
	  
	  
	  assertEquals("1", driver.findElement(By.id("dtLdropdown")).getText());
	  
	  //signout
	  driver.get(baseUrl + "link=Sign Out");

	  //log back in
	  driver.get(baseUrl + "/Timely/faces/login.xhtml?expired=true");
	  driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	  driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100001");
	  driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	  driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	  
	  //double check its still 1
	  assertEquals("1", driver.findElement(By.id("dtLdropdown")).getText());
	  
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

