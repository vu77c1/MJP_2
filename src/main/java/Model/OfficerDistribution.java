package Model;

import java.awt.*;
import java.util.Date;

public class OfficerDistribution {
    private int id;
    private String officerName;
    private float amountDistribution;
    private Date dateDistribution;
    private String addressDistribution;

    public OfficerDistribution(int id, String officerName, float amountDistribution, Date dateDistribution, String addressDistribution) {
        this.id = id;
        this.officerName = officerName;
        this.amountDistribution = amountDistribution;
        this.dateDistribution = dateDistribution;
        this.addressDistribution = addressDistribution;
    }

    public OfficerDistribution() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public float getAmountDistribution() {
        return amountDistribution;
    }

    public void setAmountDistribution(float amountDistribution) {
        this.amountDistribution = amountDistribution;
    }

    public Date getDateDistribution() {
        return dateDistribution;
    }

    public void setDateDistribution(Date dateDistribution) {
        this.dateDistribution = dateDistribution;
    }

    public String getAddressDistribution() {
        return addressDistribution;
    }

    public void setAddressDistribution(String addressDistribution) {
        this.addressDistribution = addressDistribution;
    }
}