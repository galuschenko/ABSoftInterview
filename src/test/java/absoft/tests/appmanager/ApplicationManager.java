package absoft.tests.appmanager;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

// Container for all methods used for tests. Description of each method are presented below.
public class ApplicationManager {

  public WebDriver driver;
  public AfterMethodActions afterMethodActions;
  public CheckingNewComputerCreation checkingNewComputerCreation;
  public CreatingNewComputer creatingNewComputer;

  public String browser = BrowserType.FIREFOX;  // Change BrowserType for using another browser

  public void init() {
    switch (browser) {
      case BrowserType.FIREFOX:
        System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
        driver = new FirefoxDriver();
        afterMethodActions = new AfterMethodActions(driver);
        checkingNewComputerCreation = new CheckingNewComputerCreation(driver);
        creatingNewComputer = new CreatingNewComputer(driver);


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
        getAfterMethodActions().stop();
        break;
    }
  }


  // Method opens the main page
  @Step
  public void goToMainPage() {
    if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers")) {
      driver.get("http://computer-database.gatling.io/computers");
    }
  }

  // Getters from additional classes
  public AfterMethodActions getAfterMethodActions() {
    return afterMethodActions;
  }

  public CheckingNewComputerCreation getCheckingNewComputerCreation() {
    return checkingNewComputerCreation;
  }

  public CreatingNewComputer getCreatingNewComputer() {
    return creatingNewComputer;
  }
}


