package test.timely.weeklyReport;



import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class ViewWeeklyReport{
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
  public void testJava() throws Exception {
	  driver.findElement(By.linkText("Project Manager")).click();
	    driver.findElement(By.id("j_idt50:j_idt51:0:j_idt69")).click();
	    assertEquals("Monthly Report", driver.findElement(By.cssSelector("h1")).getText());
	    driver.findElement(By.id("input_j_idt16:inputUserName")).clear();
	    driver.findElement(By.id("input_j_idt16:inputUserName")).sendKeys("100001");
	    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
	    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
	    driver.findElement(By.id("j_idt16:j_idt18")).click();
	    driver.findElement(By.linkText("View Details")).click();
	    driver.findElement(By.linkText("Project Manager")).click();
	    driver.findElement(By.id("j_idt50:j_idt51:0:j_idt66")).click();
	    driver.findElement(By.id("j_idt48:j_idt49:0:j_idt55")).click();
	    assertEquals("Weekly Report", driver.findElement(By.cssSelector("tbody > tr > th")).getText());
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
}

