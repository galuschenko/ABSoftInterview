package absoft.tests;

import io.qameta.allure.Description;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


public class AddingNewComputerTest extends TestBase {


  @Test
  @Description("Add new computer")
  public void testAddingNewComputer() {
    applicationManager.goToMainPage();
    ComputerData computerData = new ComputerData();
    int foundComputersBefore = applicationManager.getComputersListCounter(computerData);
    int ComputerCounterBefore = applicationManager.getCountFromComputersCounter();
    applicationManager.goToComputerRegistrationForm();
    applicationManager.fillTheComputerRegistrationForm(computerData);
    applicationManager.verificationSuccessfulMessage(computerData);
    int foundComputersAfter = applicationManager.getComputersListCounter(computerData);
    int ComputerCounterAfter = applicationManager.getCountFromComputersCounter();
    applicationManager.verificationBySiteCounter(ComputerCounterBefore, ComputerCounterAfter);
    applicationManager.verificationByFoundedMatches(foundComputersBefore, foundComputersAfter);
  }
}