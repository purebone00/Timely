package test.timely.admin;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class SearchBob {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
	public void setUp() throws Exception {      
		System.setProperty("webdriver.gecko.driver","c:\\geckodriver.exe");
  	driver = new FirefoxDriver();
	}

  @Test
  public void testCreateUser() throws Exception {
    driver.get(baseUrl + "/Timely/");
    driver.findElement(By.id("input_j_idt16:inputEmail")).clear();
    driver.findElement(By.id("input_j_idt16:inputEmail")).sendKeys("Admin");
    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
    driver.findElement(By.id("j_idt16:j_idt18")).click();
    driver.findElement(By.name("j_idt47:j_idt48")).click();
    driver.findElement(By.name("addUser:j_idt18")).clear();
    driver.findElement(By.name("addUser:j_idt18")).sendKeys("Bob");
    driver.findElement(By.name("addUser:j_idt20")).clear();
    driver.findElement(By.name("addUser:j_idt20")).sendKeys("Left");
    driver.findElement(By.name("addUser:j_idt22")).clear();
    driver.findElement(By.name("addUser:j_idt22")).sendKeys("IS");
    driver.findElement(By.name("addUser:j_idt27")).clear();
    driver.findElement(By.name("addUser:j_idt27")).sendKeys("Comp@4911");
    driver.findElement(By.id("addUser:addButton")).click();
    driver.findElement(By.cssSelector("input.form-control.input-sm")).clear();
    driver.findElement(By.cssSelector("input.form-control.input-sm")).sendKeys("Bob");
    
    WebElement element = driver.findElement(By.xpath("<td class='sorting_1'>Bob Left</td>"));
    String strng = element.getText();
    System.out.println(strng);
    Assert.assertEquals("Bob Left", strng);
    
   
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
