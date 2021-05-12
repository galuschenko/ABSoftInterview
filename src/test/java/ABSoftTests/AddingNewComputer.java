package ABSoftTests;

import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;


public class AddingNewComputer extends TestBase {


    @Test
    @Description("Add new computer")
    public void addingNewComputerTest(){
        ComputerData computerData = new ComputerData();
        goToMainPage();
        int foundComputersBefore = getComputersListCounter(computerData);
        int ComputerCounterBefore = getCountFromComputersCounter();
        goToComputerRegistrationForm();
        fillTheComputerRegistrationForm(computerData);
        verificationSuccessfullMessage(computerData);
        int foundComputersAfter = getComputersListCounter(computerData);
        int ComputerCounterAfter = getCountFromComputersCounter();
        verificationBySiteCounter(ComputerCounterBefore, ComputerCounterAfter);
        verificationByFoundedMatches(foundComputersBefore, foundComputersAfter);
    }
}