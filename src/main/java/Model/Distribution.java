package Model;



import java.util.Date;

public class Distribution {
    private int id;
    private int commissionId;
    private int householdID;
    private double amountReceived;
    private Date dateReceived;

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

