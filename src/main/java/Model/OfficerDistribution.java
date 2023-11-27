package Model;

import java.util.Date;

public class OfficerDistribution {
    private int id;
    private int officerId;
    private int distributionId;
    private Date dateDistribution;
    private String addressDistribution;

    public OfficerDistribution(int id, int officerId, int distributionId, Date dateDistribution, String addressDistribution) {
        this.id = id;
        this.officerId = officerId;
        this.distributionId = distributionId;
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

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public int getDistributionId() {
        return distributionId;
    }

    public void setDistributionId(int distributionId) {
        this.distributionId = distributionId;
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
