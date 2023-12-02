package Model;

import Common.DBConnect;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;

public class DonateDetail {
    private Connection con = DBConnect.connectDatabase();
    public ArrayList<DonateDetail> donateDetails = new ArrayList<>();
    private int id;
    private double amount;
    private LocalDate donateDate;
    private int commissionId;
    private int representativeId;

    private String precintName; // New field
    private String representativeName; // New field
    private String companyName; // New field
    private String officerName; // New field

    // New constructor with additional fields
    public DonateDetail(int id, double amount, LocalDate donateDate, int commissionId, int representativeId,
                        String precintName, String representativeName, String companyName, String officerName) {
        this.id = id;
        this.amount = amount;
        this.donateDate = donateDate;
        this.commissionId = commissionId;
        this.representativeId = representativeId;
        this.precintName = precintName;
        this.representativeName = representativeName;
        this.companyName = companyName;
        this.officerName = officerName;
    }

    // New getter methods for additional fields
    public String getPrecintName() {
        return precintName;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Constructor
    public DonateDetail(){
    }
    public DonateDetail(Connection con) {

        this.con = con;
    }
    public DonateDetail(int id, double amount, LocalDate donateDate, int commissionId, int representativeId) {
        this.id = id;
        this.amount = amount;
        this.donateDate = donateDate;
        this.commissionId = commissionId;
        this.representativeId = representativeId;
    }
    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }
    public double getAmount() {
        return amount;
    }



    public LocalDate getDonateDate() {

        return donateDate;
    }

    public void setDonateDate(LocalDate donateDate) {

        this.donateDate = donateDate;
    }

    public int getCommissionId() {

        return commissionId;
    }

    public void setCommissionId(int commissionId) {
        this.commissionId = commissionId;
    }

    public int getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(int representativeId) {
        this.representativeId = representativeId;
    }
    @Override
    public String toString() {
        return "DonateDetail{" +
                "id=" + id +
                ", amount=" + amount +
                ", donateDate=" + donateDate +
                ", commissionId=" + commissionId +
                ", representativeId=" + representativeId +
                '}';
    }
}
