package Model;

public class Company {
    private int id;
    private String companyName;
    private String companyAddrress;

    public Company() {

    };

    public Company(int id, String companyName, String companyAddrress) {
        this.companyName = companyName;
        this.companyAddrress = companyAddrress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAddrress() {
        return companyAddrress;
    }

    public void setCompanyAddrress(String companyAddrress) {
        this.companyAddrress = companyAddrress;
    };

}
