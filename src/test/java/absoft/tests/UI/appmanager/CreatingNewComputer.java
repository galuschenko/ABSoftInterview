package absoft.tests.UI.appmanager;

import absoft.tests.UI.inputdata.ComputerData;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

// Class contains methods used to create a new computer
public class CreatingNewComputer {

  private WebDriver driver;

  public CreatingNewComputer(WebDriver driver){
    this.driver = driver;
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
}
