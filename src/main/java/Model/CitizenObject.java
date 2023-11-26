package Model;

public class CitizenObject {
    public CitizenObject() {
    }

    public CitizenObject(int id, String typeNameObject) {
        this.id = id;
        this.typeNameObject = typeNameObject;
    }

    private int id;
    private String typeNameObject;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeNameObject() {
        return typeNameObject;
    }

    public void setTypeNameObject(String typeNameObject) {
        this.typeNameObject = typeNameObject;
    }


}
