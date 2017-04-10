package test.timely.timesheet;

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
    driver = new FirefoxDriver();
    baseUrl = "http://localhost:8080/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testJava() throws Exception {
    driver.get(baseUrl + "/Timely/");
    driver.findElement(By.id("j_idt16:j_idt18")).click();
    driver.findElement(By.id("j_idt50:j_idt51:2:j_idt61")).click();
    driver.findElement(By.id("j_idt59:j_idt122")).click();
    driver.findElement(By.id("j_idt59:j_idt119")).click();
    driver.findElement(By.id("j_idt59:j_idt117")).click();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt69")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt69")).sendKeys("111111");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt77")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt77")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt82")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt82")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt87")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt87")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt92")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt92")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt97")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt97")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt102")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt102")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt107")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt107")).sendKeys("-1");
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt112")).clear();
    driver.findElement(By.name("j_idt59:j_idt60:1:j_idt112")).sendKeys("-1");
    driver.findElement(By.id("j_idt59:j_idt117")).click();
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
