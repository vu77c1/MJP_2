package Model;

public class Officer {
    private int id;
    private String name;
    private String phoneNumber;
    private String address;
    private int commissionId;

    public Officer(int id, String name, String phoneNumber, String address, int commissionId) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.commissionId = commissionId;
    }

    public int getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(int commissionId) {
        this.commissionId = commissionId;
    }

    public Officer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}