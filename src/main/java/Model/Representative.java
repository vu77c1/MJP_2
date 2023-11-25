package Model;

public class Representative {
    private int id;
    private String representativeName;
    private String representativeAddress;
    private String phoneNumber;
    private int companyId;

    public Representative() {

    };

    public Representative(int id, String representativeName, String representativeAddress, String phoneNumber,
                          int companyId) {
        this.id = id;
        this.representativeName = representativeName;
        this.representativeAddress = representativeAddress;
        this.phoneNumber = phoneNumber;
        this.companyId = companyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public String getRepresentativeAddress() {
        return representativeAddress;
    }

    public void setRepresentativeAddress(String representativeAddress) {
        this.representativeAddress = representativeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }


}
