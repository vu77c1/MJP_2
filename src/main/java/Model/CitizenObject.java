package Model;

public class CitizenObject {
    public CitizenObject() {
    }


    private int id;
    private String typeNameObject;

    public CitizenObject(int id, String typeNameObject, int coefficient) {
        this.id = id;
        this.typeNameObject = typeNameObject;
        this.coefficient = coefficient;
    }

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    private int coefficient;
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