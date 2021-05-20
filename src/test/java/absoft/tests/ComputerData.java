package absoft.tests;

public class ComputerData {
  private final String name;
  private final String introduced;
  private final String discontinued;
  private final String companyValue;

  // Values for default filling in the form "Add new computer"
  public ComputerData() {
    this.name = "ASCI White";
    this.introduced = "2001-01-01";
    this.discontinued = "2006-01-01";
    this.companyValue = "13";
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
