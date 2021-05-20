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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Container for all methods used for tests. Description of each method are presented below.
public class ApplicationManager {

  public WebDriver driver;
  public String browser = BrowserType.FIREFOX;  // Change BrowserType for using another browser

  protected void init() {
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
        stop();
        break;
    }
  }

  protected void stop() {
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

  // Deleting the created computer
  @Step
  public void removeTracesAfterTest(ComputerData computerData){
    Map<String, String> mapOfCompanies = getListForCompanyConversion();
    goToMainPage();
    driver.findElement(By.id("searchbox")).sendKeys(computerData.getName());
    driver.findElement(By.id("searchsubmit")).click();
    List<WebElement> computerNames = driver
        .findElements(By.xpath("//table[@class='computers " +
            "zebra-striped']/child::tbody/child::tr//child::td/child::a[@href]"));
    List<WebElement> computerIntroduced = driver
        .findElements(By.xpath("//table[@class='computers " +
            "zebra-striped']/child::tbody/child::tr//child::td/child::a[@href]/ancestor::td" +
            "/following-sibling::td[1]"));
    List<WebElement> computerDiscontinued = driver.findElements(By.xpath("//table[@class" +
        "='computers zebra-striped']/child::tbody/child::tr/child::td/child::a[@href]/ancestor" +
        "::td/following-sibling::td[2]"));
    List<WebElement> computerCompany = driver.findElements((By.xpath("//table[@class='computers " +
        "zebra-striped']/child::tbody/child::tr/child::td/child::a[@href]/ancestor::td/following" +
        "-sibling::td[3]")));
    boolean computerDeleted = deleteMatchedComputers(computerData, computerNames,
        computerIntroduced, computerDiscontinued, computerCompany, mapOfCompanies);
    if (!computerDeleted){
      System.out.println("No matches found on this page");
    }
    if (computerNames.size() >= 10 && !computerDeleted){
      removeTracesFromNextPages(computerData, mapOfCompanies);
      }
    }

  /* If created computer wasn't found on the first page, the method looking for it on other pages
   * and delete if it exist
   */
  private void removeTracesFromNextPages(ComputerData computerData,
                                                  Map<String, String> mapOfCompanies){
    driver.findElement(By.linkText("Next →")).click();
    List<WebElement> computerNames = driver
        .findElements(By.xpath("//table[@class='computers " +
            "zebra-striped']/child::tbody/child::tr//child::td/child::a[@href]"));
    List<WebElement> computerIntroduced = driver
        .findElements(By.xpath("//table[@class='computers " +
            "zebra-striped']/child::tbody/child::tr//child::td/child::a[@href]/ancestor::td" +
            "/following-sibling::td[1]"));
    List<WebElement> computerDiscontinued = driver.findElements(By.xpath("//table[@class" +
        "='computers zebra-striped']/child::tbody/child::tr/child::td/child::a[@href]/ancestor" +
        "::td/following-sibling::td[2]"));
    List<WebElement> computerCompany = driver.findElements((By.xpath("//table[@class='computers " +
        "zebra-striped']/child::tbody/child::tr/child::td/child::a[@href]/ancestor::td/following" +
        "-sibling::td[3]")));
    boolean computerDeleted = deleteMatchedComputers(computerData, computerNames,
        computerIntroduced, computerDiscontinued, computerCompany, mapOfCompanies);
    if (computerNames.size() >= 10 && !computerDeleted){
      removeTracesFromNextPages(computerData, mapOfCompanies);
    }
  }

  // Additional to "RemoveTracesAfterTest" method for removing computer
  private boolean deleteMatchedComputers(ComputerData computerData, List<WebElement> computerNames,
                                         List<WebElement> computerIntroduced,
                                         List<WebElement> computerDiscontinued,
                                         List<WebElement> computerCompany,
                                         Map<String, String> listOfCompanies) {
    boolean computerDeleted = false;
    List<String> convertedIntroduced = dateConversion(computerIntroduced);
    List<String> convertedDiscontinued = dateConversion(computerDiscontinued);
    List<String> convertedCompanies = companyConversion(listOfCompanies, computerCompany);
    for (int i = 0; i < computerNames.size(); i++){
      if (computerData.getName().equals(computerNames.get(i).getText()) &&
              computerData.getIntroduced().equals(convertedIntroduced.get(i)) &&
              computerData.getDiscontinued().equals(convertedDiscontinued.get(i)) &&
              computerData.getCompanyValue().equals(convertedCompanies.get(i))){
        driver.findElement(By.xpath("//tr[" + (i + 1) + "]/td/child::a")).click();
        driver.findElement(By.xpath("//input[@class='btn danger']")).click();
        computerDeleted = true;
        break;
      }
    }
    return computerDeleted;
  }

  /*
   * Additional to "RemoveTracesAfterTest" method for removing computer
   * Converts dates from "dd Mon yyyy" (shown in the computers table) to "yyyy-mm-dd"
   */
  private List<String> dateConversion(List<WebElement> computerDate){
    Map<String, String> monthMap = new HashMap<>();
    monthMap.put("Jan", "01");
    monthMap.put("Feb", "02");
    monthMap.put("Mar", "03");
    monthMap.put("Apr", "04");
    monthMap.put("May", "05");
    monthMap.put("Jun", "06");
    monthMap.put("Jul", "07");
    monthMap.put("Aug", "08");
    monthMap.put("Sep", "09");
    monthMap.put("Oct", "10");
    monthMap.put("Nov", "11");
    monthMap.put("Dec", "12");
    List<String> date = new ArrayList<>();
    for (WebElement webElement : computerDate) {

      StringBuilder dateFromList = new StringBuilder(webElement.getText());
      if (dateFromList.capacity() < 27){
        date.add("-");
      } else {
        String day = dateFromList.substring(0, 2);
        String month = monthMap.get(dateFromList.substring(3, 6));
        String year = dateFromList.substring(7);
        date.add(year + "-" + month + "-" + day);
      }
    }
    return date;
  }

  /*
   * Additional to "RemoveTracesAfterTest" method for removing computer
   * Returns Map with company names and corresponding values
   */
  private Map<String, String> getListForCompanyConversion(){
    goToMainPage();
    driver.findElement(By.linkText("Add a new computer")).click();
    driver.findElement(By.xpath("//select[@id='company']")).click();
    List<WebElement> companyValues = driver.findElements(By.xpath("//option[@class='blank" +
        "']/following-sibling::option"));
    Map<String, String> companyMap = new HashMap<>();
    for (WebElement companyValue : companyValues) {
      companyMap.put(companyValue.getText(), companyValue.getAttribute("value"));
    }
    return companyMap;
  }

  /*
   * Additional to "RemoveTracesAfterTest" method for removing computer
   * Returns corresponding to companies values
   */
  private List<String> companyConversion(Map<String,String> mapForCompanyConversion,
                                         List<WebElement> listForCompanyConversion){
    List<String> companyConversion = new ArrayList<>();
    for (WebElement webElement : listForCompanyConversion) {
      companyConversion.add(mapForCompanyConversion.get(webElement.getText()));
    }
    return companyConversion;
  }
}


