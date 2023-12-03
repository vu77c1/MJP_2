package Model;



import java.util.Date;

public class Distribution {
    private int id;
    private int commissionId;
    private int householdID;
    private double amountReceived;
    private Date dateReceived;
    private String precintName; // New field
    private String cityName; // New field
    private String provinceName; // New field
    private int isHouseholdLlord; // New fie
    private String name;


    public Distribution(int id, int commissionId, int householdID, double amountReceived, Date dateReceived, String precintName, String cityName, String provinceName, int isHouseholdLlord, String name) {
        this.id = id;
        this.commissionId = commissionId;
        this.householdID = householdID;
        this.amountReceived = amountReceived;
        this.dateReceived = dateReceived;
        this.precintName = precintName;
        this.cityName = cityName;
        this.provinceName = provinceName;
        this.isHouseholdLlord = isHouseholdLlord;
        this.name = name;

    }

    public Distribution(int commissionId, String precintName, String cityName, String provinceName) {
        this.commissionId = commissionId;
        this.precintName = precintName;
        this.cityName = cityName;
        this.provinceName = provinceName;
    }

    public Distribution(int householdID, int isHouseholdLlord, String name) {
        this.householdID = householdID;
        this.isHouseholdLlord = isHouseholdLlord;
        this.name = name;
    }

    public String getPrecintName() {
        return precintName;
    }

    public void setPrecintName(String precintName) {
        this.precintName = precintName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getIsHouseholdLlord() {
        return isHouseholdLlord;
    }

    public void setIsHouseholdLlord(int isHouseholdLlord) {
        this.isHouseholdLlord = isHouseholdLlord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Distribution() {
    }

    public Distribution(int id, int commissionId, int householdID, double amountReceived, Date dateReceived) {
        this.id = id;
        this.commissionId = commissionId;
        this.householdID = householdID;
        this.amountReceived = amountReceived;
        this.dateReceived = dateReceived;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommissionId() {
        return commissionId;
    }

    public void setCommissionId(int commissionId) {
        this.commissionId = commissionId;
    }

    public int getHouseholdID() {
        return householdID;
    }

    public void setHouseholdID(int householeID) {
        this.householdID = householeID;
    }

    public double getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(double amountReceived) {
        this.amountReceived = amountReceived;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }


}

