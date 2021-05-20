package absoft.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

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
  public void setDriver() {
    applicationManager.init();
  }

  @AfterMethod(alwaysRun = true)
  public void removingTracesAfterTest(){
    ComputerData computerData = new ComputerData();
    applicationManager.removeTracesAfterTest(computerData);
    applicationManager.stop();
  }
}
