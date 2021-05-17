package absoft.tests;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.List;

/* Container with @BeforeMethod, @AfterMethod and methods which are used in AddingComputerTest.

 * @BeforeMethod contains WebDriver setup for Firefox, Chrome, Internet Explorer drivers.
 * (Default browser is Firefox, for switch browser, change BrowserType.FIREFOX to .CHROME or .IE)
 *
 * @AfterMethod contains methods with deleting already registered computer and closing browser
 *
 * Other methods are explained below
 */
public class TestBase {
  public WebDriver driver;
  public String browser = BrowserType.FIREFOX;  // Change BrowserType for using another browser

  @BeforeMethod(alwaysRun = true)
  public void setDriver() {
    switch (browser) {
      case BrowserType.FIREFOX:
        System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
        driver = new FirefoxDriver();
        break;
      case BrowserType.CHROME:
        System.setProperty("webdriver.Chrome", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        break;
      case BrowserType.IE:
        System.setProperty("webdriver.ie.driver", "src/test/resources/IEDriverServer.exe");
        driver = new InternetExplorerDriver();
        break;
      default:
        System.out.println("The specified Webdriwer was not found");
        driver.quit();
        break;
    }
  }

  @AfterMethod(alwaysRun = true)
  public void stop() {
    driver.quit();
  }

  // Method opens the main page
  @Step
  public void goToMainPage() {
    if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers")) {
      driver.get("http://computer-database.gatling.io/computers");
    }
  }

  // Method opens page with registration form
  @Step
  public void goToComputerRegistrationForm() {
    if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers/new")) {
      if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers")) {
        driver.get("http://computer-database.gatling.io/computers");
      }
      driver.findElement(By.id("add")).click();
    }
  }
  // Method fills the registration form with data from ComputerData class
  @Step
  public void fillTheComputerRegistrationForm(ComputerData computerData) {
    driver.findElement(By.name("name")).sendKeys(computerData.getName());
    driver.findElement(By.name("introduced")).sendKeys(computerData.getIntroduced());
    driver.findElement(By.name("discontinued")).sendKeys(computerData.getDiscontinued());
    Select select = new Select(driver.findElement(By.name("company")));
    select.selectByValue(computerData.getCompanyValue());
    driver.findElement(By.xpath("//input[@type='submit']")).click();
  }

  // Method returns the value from the counter of registered computers on the main page
  @Step
  public int getCountFromComputersCounter() {
    driver.get("http://computer-database.gatling.io/computers");
    String computersFound = driver.findElement
        (By.xpath("//section[@id='main']//child::h1")).getText();
    String[] computersInString = computersFound.split(" ");
    return Integer.parseInt(computersInString[0]);
  }

  // Method returns the registered computers' count that have the same name as ComputerData
  @Step
  public int getComputersListCounter(ComputerData computerData) {
    if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers")) {
      driver.get("http://computer-database.gatling.io/computers");
    }
    driver.findElement(By.id("searchbox")).sendKeys(computerData.getName());
    driver.findElement(By.id("searchsubmit")).click();
    List<WebElement> registeredComputers =
        driver.findElements
            (By.xpath("//table[@class='computers zebra-striped']//tbody/child::tr"));
    int counter = +registeredComputers.size();
    if (registeredComputers.size() >= 10) {
      counter += getComputerListOnTheNextPage();
    }
    return counter;
  }

  /*
   * If the page have more than 10 computers with the same name as ComputerData, method returns
   * the count of the same computers on the next pages
   */
  private int getComputerListOnTheNextPage() {
    List<WebElement> registeredComputers;
    driver.findElement(By.linkText("Next →")).click();
    registeredComputers = driver.findElements(By.xpath("//tbody//tr"));
    if (registeredComputers.size() >= 10) {
      getComputerListOnTheNextPage();
    }
    return registeredComputers.size();
  }

  // Method returns the text from the message after creating new computer
  public String successfulMessage() {
    return driver.findElement
        (By.xpath("//section[@id='main']/child::div[@class='alert-message warning']")).getText();
  }

  // Method returns the text which expected to be shown in the message after creating new computer
  public String successfulMessageForming(ComputerData computerData) {
    return "Done ! Computer " + computerData.getName() + " has been created";
  }

  /*
   *The method checks the count of created computers in the list of computers with the same name
   * to ComputerData is increased by one
   */
   @Step
  public void verificationByFoundedMatches(int foundComputersBefore, int foundComputersAfter) {
    Assert.assertEquals(foundComputersAfter, foundComputersBefore + 1);
  }

  /*
   * The method checks that computers' counter on the main page increases by one after creating
   * new computer
   */
  @Step
  public void verificationBySiteCounter(int computerCounterBefore, int computerCounterAfter) {
    Assert.assertEquals(computerCounterAfter, computerCounterBefore + 1);
  }

  // The method checks that the message after creating new computer contains expected text
  @Step
  public void verificationSuccessfulMessage(ComputerData computerData) {
    Assert.assertEquals(successfulMessage(), successfulMessageForming(computerData));
  }
}
