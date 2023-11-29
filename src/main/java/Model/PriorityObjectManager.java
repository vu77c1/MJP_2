package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PriorityObjectManager {
    //Lay tat ca thong tin PriorityObject trong databse khong truyen tham so
    public ArrayList<PriorityObject> getPriorityObject() {
        ArrayList<PriorityObject> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            String sql = "select  * from PriorityObject";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new PriorityObject(
                                rs.getInt("id"),
                                rs.getString("object_type")
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

    //Lay tat ca thong tin PriorityObject trong databse co tham so
    public ArrayList<PriorityObject> getPriorityObject(String sql) {
        ArrayList<PriorityObject> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new PriorityObject(
                                rs.getInt("id"),
                                rs.getString("object_type")
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
    public PriorityObject inputPriorityObject() {
        String type = InputValidator.validateStringPriorityObject("\t\t\tEnter New ObjectType:  ");
        PriorityObject po = new PriorityObject();
        po.setObjectType(type);
        return po;
    }

    //Them du lieu vao bang PriorityObject
    public void addPriorityObject() {
        try {
            PriorityObject po = inputPriorityObject();
            JDBCQuery.openConnection();
            String sql = "INSERT INTO PriorityObject (object_type) VALUES (?)";
            Object[] prams = {po.getObjectType()};
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

    //cap nhat Priority Object
    public void updatePriorityObject() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to update: ");
        if (isIdExists(id)) {
            displayPriorityObjects(getPriorityObject("select * from PriorityObject where id=" + id));
            String type = InputValidator.validateStringPriorityObject("\t\t\tInput Object Type: ");
            try {
                JDBCQuery.openConnection();

                String sql = "UPDATE PriorityObject SET object_type = ? WHERE id = ?";
                Object[] params = {type, id};

                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tUpdate success!!");
                } else {
                    System.out.println("\t\t\tUpdate failed. No PriorityObject found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tUpdate failed. The list of PriorityObjects is empty.");
        }
    }

    //ham xoa  Priority Object theo ID
    public void deletePriorityObject() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to delete: ");
        if (isIdExists(id)) {
            try {
                JDBCQuery.openConnection();

                String sql = "DELETE FROM PriorityObject WHERE id = ?";
                Object[] params = {id};
                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tDelete success!!");
                } else {
                    System.out.println("\t\t\tDelete failed. No PriorityObject found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tDelete failed. The list of PriorityObjects is empty.");
        }

    }

    //hien thi danh sach PriorityObject
    public void displayPriorityObjects(ArrayList<PriorityObject> priorityObjects) {
        System.out.println("\t\t\t\u001B[1mID\tOBJECT_TYPE\u001B[0m");

        for (PriorityObject priorityObject : priorityObjects) {
            System.out.println("\t\t\t" + priorityObject.getId() + "\t" + priorityObject.getObjectType());
        }
    }
    //Kiem tra PriorityObject co ton tai trong database khong

    public boolean isIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery.openConnection();

            String sql = "SELECT COUNT(*) FROM PriorityObject WHERE id = ?";
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