package test.timely.ui;



import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestGeneralUtilitiesButton{
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
    driver.get(baseUrl + "/Timely/");
    driver.findElement(By.id("input_j_idt16:inputEmail")).clear();
    driver.findElement(By.id("input_j_idt16:inputEmail")).sendKeys("Ryan");
    driver.findElement(By.id("input_j_idt16:inputPassword")).clear();
    driver.findElement(By.id("input_j_idt16:inputPassword")).sendKeys("Comp@4911");
    driver.findElement(By.id("j_idt16:j_idt18")).click();
    driver.findElement(By.linkText("Features")).click();
    assertEquals("About Us", driver.findElement(By.id("j_idt47")).getText());
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

