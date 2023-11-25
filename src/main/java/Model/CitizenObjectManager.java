package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CitizenObjectManager {
    //Lay tat ca thong tin CitizenObject trong databse khong truyen tham so
    public ArrayList<CitizenObject> getCitizenObject() {
        ArrayList<CitizenObject> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            String sql = "select  * from CitizenObject";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new CitizenObject(
                                rs.getInt("id"),
                                rs.getString("type_name_object")
                        ));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQuery.closeConnection();

        }
        return infoList;
    }

    //Lay tat ca thong tin CitizenObject trong databse co tham so
    public ArrayList<CitizenObject> getCitizenObject(String sql) {
        ArrayList<CitizenObject> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new CitizenObject(
                                rs.getInt("id"),
                                rs.getString("type_name_object")
                        ));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQuery.closeConnection();

        }
        return infoList;
    }

    //Nhap du lieu
    public CitizenObject inputCitizenObject() {
        String type = InputValidator.validateStringCitizenObject("\t\t\tEnter New TypeNameObject:  ");
        CitizenObject co = new CitizenObject();
        co.setTypeNameObject(type);
        return co;
    }

    //Them du lieu vao bang CitizenObject
    public void addCitizenObject() {
        try {
            CitizenObject po = inputCitizenObject();
            JDBCQuery.openConnection();
            String sql = "INSERT INTO CitizenObject (type_name_object) VALUES (?)";
            Object[] prams = {po.getTypeNameObject()};
            int rs = JDBCQuery.executeUpdateQuery(sql, prams);
            if (rs > 0) {
                System.out.println("\t\t\tAdd success!!");
            } else {
                System.out.println("\t\t\tAdd failed!!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery.closeConnection();
        }

    }

    //update table CitizenObject
    public void updateCitizenObject() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to update: ");
        if (isIdExists(id)) {
            displayCitizenObjects(getCitizenObject("select * from CitizenObject where id=" + id));
            String type = InputValidator.validateStringPriorityObject("\t\t\tInput Type Name Object: ");
            try {
                JDBCQuery.openConnection();

                String sql = "UPDATE CitizenObject SET type_name_object = ? WHERE id = ?";
                Object[] params = {type, id};

                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tUpdate success!!");
                } else {
                    System.out.println("\t\t\tUpdate failed. No CitizenObject found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tUpdate failed. The list of CitizenObject is empty.");
        }
    }

    //ham xoa  CitizenObject theo ID
    public void deletePriorityObject() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to delete: ");
        if (isIdExists(id)) {
            try {
                JDBCQuery.openConnection();

                String sql = "DELETE FROM CitizenObject WHERE id = ?";
                Object[] params = {id};
                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tDelete success!!");
                } else {
                    System.out.println("\t\t\tDelete failed. No CitizenObject found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tDelete failed. The list of CitizenObject is empty.");
        }

    }

    //hien thi danh sach CitizenObject
    public void displayCitizenObjects(ArrayList<CitizenObject> citizenObjects) {
        System.out.println("\t\t\tID\tObject Type");

        for (CitizenObject citizenObject : citizenObjects) {
            System.out.println("\t\t\t" + citizenObject.getId() + "\t" + citizenObject.getTypeNameObject());
        }
    }
    //Kiem tra CitizenObject co ton tai trong database khong

    public boolean isIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery.openConnection();

            String sql = "SELECT COUNT(*) FROM CitizenObject WHERE id = ?";
            Object[] params = {id};

            ResultSet rs = JDBCQuery.executeSelectQuery(sql, params);

            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery.closeConnection();
        }
        return exists;
    }
}
