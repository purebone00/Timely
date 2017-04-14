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

public class ChangeSuccessful {
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
	  driver.findElement(By.linkText("Change Password")).click();
	    
	  	//login normally
	  	
	   
	    driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	    driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    driver.findElement(By.linkText("Change Password")).click();
	    driver.findElement(By.id("input_j_idt50:oldPassword")).clear();
	    
	    //changed password
	    driver.findElement(By.id("input_j_idt50:oldPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("input_j_idt50:inputNewPassword")).clear();
	    driver.findElement(By.id("input_j_idt50:inputNewPassword")).sendKeys("P@$$w0rd");
	    driver.findElement(By.id("input_j_idt50:confirmPassword")).clear();
	    driver.findElement(By.id("input_j_idt50:confirmPassword")).sendKeys("P@$$w0rd");
	    driver.findElement(By.id("j_idt50:j_idt56")).click();
	    driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	    
	    //log back in w/ new credentials
	    driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100002");
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("P@$$w0rd");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    assertEquals("Hello, Tony Romo!", driver.findElement(By.cssSelector("h1")).getText());
	    
	    
	    //cleanup:  reset database back to normal
	  
	    driver.findElement(By.id("input_j_idt50:oldPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("input_j_idt50:inputNewPassword")).clear();
	    driver.findElement(By.id("input_j_idt50:inputNewPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("input_j_idt50:confirmPassword")).clear();
	    driver.findElement(By.id("input_j_idt50:confirmPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt50:j_idt56")).click();
	    driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	    
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

