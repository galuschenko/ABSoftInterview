package absoft.tests;

import io.qameta.allure.Description;
import org.testng.annotations.Test;


public class AddingNewComputerTest extends TestBase {


  @Test
  @Description("Add new computer")
  public void testAddingNewComputer() {
    goToMainPage();
    ComputerData computerData = new ComputerData();
    int foundComputersBefore = getComputersListCounter(computerData);
    int ComputerCounterBefore = getCountFromComputersCounter();
    goToComputerRegistrationForm();
    fillTheComputerRegistrationForm(computerData);
    verificationSuccessfulMessage(computerData);
    int foundComputersAfter = getComputersListCounter(computerData);
    int ComputerCounterAfter = getCountFromComputersCounter();
    verificationBySiteCounter(ComputerCounterBefore, ComputerCounterAfter);
    verificationByFoundedMatches(foundComputersBefore, foundComputersAfter);
  }
}