package absoft.tests.UI.appmanager;

import absoft.tests.UI.inputdata.ComputerData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class contains methods used in @AfterMethod
public class AfterMethodActions {
  private WebDriver driver;

  public AfterMethodActions(WebDriver driver) {
    this.driver = driver;
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

  // Method opens the main page
  @Step
  public void goToMainPage() {
    if (!driver.getCurrentUrl().equals("http://computer-database.gatling.io/computers")) {
      driver.get("http://computer-database.gatling.io/computers");
    }
  }
  public void stop() {
    driver.quit();
  }
}
