package Model;

public class House {
    private int id;
    private int commissionId;
    private int priorityObjectId;

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

