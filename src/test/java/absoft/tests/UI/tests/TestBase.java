package absoft.tests.UI.tests;

import absoft.tests.UI.appmanager.ApplicationManager;
import absoft.tests.UI.inputdata.ComputerData;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

/* Container with @BeforeMethod, @AfterMethod.

 * @BeforeMethod contains WebDriver setup for Firefox, Chrome, Internet Explorer drivers.
 * (Default browser is Firefox, for switch browser, change BrowserType.FIREFOX to .CHROME or .IE
 * in ApplicationManager)
 *
 * @AfterMethod contains methods with deleting already registered computer and closing browser
 */
public class TestBase {

  protected final ApplicationManager applicationManager = new ApplicationManager();

  @BeforeMethod(alwaysRun = true)
  public void setDriver() throws IOException {
    applicationManager.init();
  }

  @AfterMethod(alwaysRun = true)
  public void removingTracesAfterTest(){
    ComputerData computerData = new ComputerData(applicationManager.getProperties());
    applicationManager.getAfterMethodActions().removeTracesAfterTest(computerData);
    applicationManager.getAfterMethodActions().stop();
  }
}
