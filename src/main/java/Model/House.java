package Model;

import java.util.ArrayList;

public class House {
    private int id;
    private int commissionId;
    private int priorityObjectId;
    public ArrayList<House> houses = new ArrayList<>();

    public House(int id, int commissionId, int priorityObjectId, String precintName, String objectType) {
        this.id = id;
        this.commissionId = commissionId;
        this.priorityObjectId = priorityObjectId;
        this.houses = houses;
        this.precintName = precintName;
        this.objectType = objectType;
    }

    public String getPrecintName() {
        return precintName;
    }

    public void setPrecintName(String precintName) {
        this.precintName = precintName;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }



    private String precintName;
    private String objectType;

    // Constructor
    public House(int id, int commissionId, int priorityObjectId) {
        this.id = id;
        this.commissionId = commissionId;
        this.priorityObjectId = priorityObjectId;
    }

    // Getters and setters
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

    public int getPriorityObjectId() {
        return priorityObjectId;
    }

    public void setPriorityObjectId(int priorityObjectId) {
        this.priorityObjectId = priorityObjectId;
    }

    // toString() method to represent House object as a String
    @Override
    public String toString() {
        return "House{" +
                "id=" + id +
                ", commissionId=" + commissionId +
                ", priorityObjectId=" + priorityObjectId +
                '}';
    }
}