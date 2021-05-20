package absoft.tests.tests;

import absoft.tests.inputdata.ComputerData;
import io.qameta.allure.Description;
import org.testng.annotations.Test;


public class AddingNewComputerTest extends TestBase {


  @Test
  @Description("Add new computer")
  public void testAddingNewComputer() {
    applicationManager.goToMainPage();
    ComputerData computerData = new ComputerData();
    int foundComputersBefore =
        applicationManager.getCheckingNewComputerCreation().getComputersListCounter(computerData);
    int ComputerCounterBefore =
        applicationManager.getCheckingNewComputerCreation().getCountFromComputersCounter();
    applicationManager.getCreatingNewComputer().goToComputerRegistrationForm();
    applicationManager.getCreatingNewComputer().fillTheComputerRegistrationForm(computerData);
    applicationManager.getCheckingNewComputerCreation().verificationSuccessfulMessage(computerData);
    int foundComputersAfter =
        applicationManager.getCheckingNewComputerCreation().getComputersListCounter(computerData);
    int ComputerCounterAfter =
        applicationManager.getCheckingNewComputerCreation().getCountFromComputersCounter();
    applicationManager.getCheckingNewComputerCreation()
        .verificationBySiteCounter(ComputerCounterBefore, ComputerCounterAfter);
    applicationManager.getCheckingNewComputerCreation()
        .verificationByFoundedMatches(foundComputersBefore, foundComputersAfter);
  }
}