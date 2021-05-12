package ABSoftTests;

public class ComputerData {

    private String name;
    private final String introduced;
    private final String discontinued;
    private final String companyValue;

    public ComputerData(){  // Values for default filling
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

    public String getCompanyValue() {
        return companyValue;
    }
    public ComputerData withName(String name){
        this.name = name;
        return this;
    }
}
