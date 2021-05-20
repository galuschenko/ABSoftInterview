package absoft.tests.appmanager;

import absoft.tests.inputdata.ComputerData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.List;

// Class contains methods that check whether a new computer has been created
  public class CheckingNewComputerCreation {
  private WebDriver driver;

  public CheckingNewComputerCreation(WebDriver driver) {
    this.driver = driver;
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
    Assert.assertEquals(successfulMessage(),
        successfulMessageForming(computerData));
  }
}
