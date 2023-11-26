package Model;

import Common.DBConnect;

import java.sql.Connection;
import java.time.LocalDate;

public class DonateDetail {
    private Connection con = DBConnect.connectDatabase();
    private int id;
    private double amount;
    private LocalDate donateDate;
    private int commissionId;
    private int representativeId;
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
    //Getter and Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
