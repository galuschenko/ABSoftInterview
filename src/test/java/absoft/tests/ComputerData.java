package absoft.tests;

public class ComputerData {
  private final String name;
  private final String introduced;
  private final String discontinued;
  private final String companyValue;

  // Values for default filling in the form "Add new computer"
  public ComputerData() {
    this.name = "Apple";
    this.introduced = "2011-05-11";
    this.discontinued = "2021-05-11";
    this.companyValue = "10";
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
