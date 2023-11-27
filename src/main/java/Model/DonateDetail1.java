package Model;

import java.util.Date;

public class DonateDetail1 {
    private int id;
    private float amount;
    private Date donateDate;
    private int commissionID;
    private int representativeId;

    public DonateDetail1() {
    }

    public DonateDetail1(int id, float amount, Date donateDate, int commissionID, int representativeId) {
        this.id = id;
        this.amount = amount;
        this.donateDate = donateDate;
        this.commissionID = commissionID;
        this.representativeId = representativeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDonateDate() {
        return donateDate;
    }

    public void setDonateDate(Date donateDate) {
        this.donateDate = donateDate;
    }

    public int getCommissionID() {
        return commissionID;
    }

    public void setCommissionID(int commissionID) {
        this.commissionID = commissionID;
    }

    public int getRepresentativeId() {
        return representativeId;
    }

    public void setRepresentativeId(int representativeId) {
        this.representativeId = representativeId;
    }
}
