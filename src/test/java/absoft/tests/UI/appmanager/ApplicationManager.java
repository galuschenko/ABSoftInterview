package absoft.tests.UI.appmanager;

import absoft.tests.UI.inputdata.ComputerData;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

// Container for all methods used for tests. Description of each method are presented below.
public class ApplicationManager {

  File fileProperties = new File("src/test/resources/config/environment.properties");
  Properties properties = new Properties();

  public WebDriver driver;
  public AfterMethodActions afterMethodActions;
  public CheckingNewComputerCreation checkingNewComputerCreation;
  public CreatingNewComputer creatingNewComputer;
  public ComputerData computerData;


  public void init() throws IOException {
    properties.load(new FileReader(fileProperties));
    Map<String, String> browserMap = new HashMap<>();
    browserMap.put("FIREFOX", BrowserType.FIREFOX);
    browserMap.put("CHROME", BrowserType.CHROME);
    browserMap.put("IE", BrowserType.IE);
    String browser = browserMap.get(properties.getProperty("BROWSER"));
    String os = properties.getProperty("OS");
      switch (browser) {
        case BrowserType.FIREFOX:
          if (os.equals("Windows")) {
            System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
          } else if (os.equals("MacOS")){
            System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver");
          }
          driver = new FirefoxDriver();
          afterMethodActions = new AfterMethodActions(driver);
          checkingNewComputerCreation = new CheckingNewComputerCreation(driver);
          creatingNewComputer = new CreatingNewComputer(driver);
          break;
        case BrowserType.CHROME:
          if (os.equals("Windows")) {
            System.setProperty("webdriver.Chrome", "src/test/resources/chromedriver.exe");
          }
          if (os.equals("MacOS")){
            System.getProperty("webdriver.Chrome\", \"src/test/resources/chromedriver");
          }
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
  public Properties getProperties(){
    return properties;
  }
}


