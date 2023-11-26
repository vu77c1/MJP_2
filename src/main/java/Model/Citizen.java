package Model;

import java.util.Date;

public class Citizen {
    private int id;
    private String name;
    private String identityCard;
    private Date dateOfBirth;
    private String phoneNumber;
    private String address;
    private int houseId;
    private boolean isHouseholdLord;
    private boolean sex;
    private int citizenObjectId;

    // Constructors
    public Citizen() {
        // Default constructor
    }

    public Citizen(String name, String identityCard, Date dateOfBirth, String phoneNumber, String address, int houseId, boolean isHouseholdLord, boolean sex, int citizenObjectId) {
        this.name = name;
        this.identityCard = identityCard;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.houseId = houseId;
        this.isHouseholdLord = isHouseholdLord;
        this.sex = sex;
        this.citizenObjectId = citizenObjectId;
    }

    // Getters and setters for all fields
    // ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Identity Card
    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    // Date of Birth
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    // Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // Address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // House ID
    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    // Is Household Lord
    public boolean isHouseholdLord() {
        return isHouseholdLord;
    }

    public void setHouseholdLord(boolean householdLord) {
        isHouseholdLord = householdLord;
    }

    // Sex
    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    // Citizen Object ID
    public int getCitizenObjectId() {
        return citizenObjectId;
    }

    public void setCitizenObjectId(int citizenObjectId) {
        this.citizenObjectId = citizenObjectId;
    }
}
