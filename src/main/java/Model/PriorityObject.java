package Model;

public class PriorityObject {
    private int id;
    private String objectType;
    public PriorityObject() {
        // Default constructor
    }

    public PriorityObject(int id, String objectType) {
        this.id = id;
        this.objectType = objectType;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }
}
