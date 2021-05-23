package absoft.tests.UI.inputdata;

import org.openqa.selenium.WebDriver;

import java.util.Properties;

public class ComputerData {
  private final String name;
  private final String introduced;
  private final String discontinued;
  private final String companyValue;

  // Values for default filling in the form "Add new computer"
  public ComputerData(Properties properties) {
    this.name = properties.getProperty("ComputerName");
    this.introduced = properties.getProperty("Introduced");
    this.discontinued = properties.getProperty("Discontinued");
    this.companyValue = properties.getProperty("Company");
  }

  public String getName() {
    return name;
  }

  public String getIntroduced() {
    return introduced;
  }

  public String getDiscontinued() {
    return discontinued;
  }

  public String getCompanyValue() { return companyValue; }
}
