
package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.*;
import java.util.*;

public class PriorityObjectManager {
    //Lay tat ca thong tin PriorityObject trong databse khong truyen tham so
    public Map<Integer, PriorityObject> getPriorityObject(String sql) {
        Map<Integer, PriorityObject> indexObjectMap = new LinkedHashMap<>(); // Sử dụng LinkedHashMap để giữ thứ tự
        try {
            JDBCQuery.openConnection();

            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    int index = 1;
                    while (rs.next()) {
                        PriorityObject priorityObject = new PriorityObject(
                                rs.getInt("id"),
                                rs.getString("object_type")
                        );
                        indexObjectMap.put(index, priorityObject);
                        index++;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        }
        return indexObjectMap;
    }

    //Lay tat ca thong tin PriorityObject trong databse co tham so
    public Map<Integer, PriorityObject> getPriorityObject() {
        Map<Integer, PriorityObject> indexObjectMap = new LinkedHashMap<>(); // Sử dụng LinkedHashMap để giữ thứ tự

        try {
            JDBCQuery.openConnection();
            String sql = "select  * from PriorityObject order by id desc";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                int index = 1;
                try {
                    while (rs.next()) {
                        PriorityObject priorityObject = new PriorityObject(
                                rs.getInt("id"),
                                rs.getString("object_type")
                        );
                        indexObjectMap.put(index, priorityObject);
                        index++;
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
        // Convert Map values (PriorityObjects) to ArrayList
        return indexObjectMap;
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
                displayPriorityObjects(getPriorityObject());
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
        displayPriorityObjects(getPriorityObject());
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to update: ");
        int idPriorityObject = 0;

        if (getPriorityObject().containsKey(id)) {
            idPriorityObject = getPriorityObject().get(id).getId();
            if (isIdExists(idPriorityObject)) {
                System.out.println("\t\t\t\u001B[1mID\t\tOBJECT_TYPE\u001B[0m");
                for (Map.Entry<Integer, PriorityObject> entry : getPriorityObject("select * from PriorityObject where id=" + idPriorityObject).entrySet()) {
                    System.out.println("\t\t\t" + id + "\t\t" + entry.getValue().getObjectType());
                }
                String type = InputValidator.validateStringPriorityObject("\t\t\tInput Object Type: ");
                try {
                    JDBCQuery.openConnection();

                    String sql = "UPDATE PriorityObject SET object_type = ? WHERE id = ?";
                    Object[] params = {type, idPriorityObject};

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
        else {
            System.out.println("\t\t\tRecord not exist");
        }
    }

    //ham xoa  Priority Object theo ID
    public void deletePriorityObject() {
        displayPriorityObjects(getPriorityObject());
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to delete: ");
        int idPriorityObject = 0;

        if (getPriorityObject().containsKey(id)) {
            idPriorityObject = getPriorityObject().get(id).getId();
            if (isIdExists(idPriorityObject)) {
                try {
                    JDBCQuery.openConnection();
                    for (Map.Entry<Integer, PriorityObject> entry : getPriorityObject("select * from PriorityObject where id=" + idPriorityObject).entrySet()) {
                        System.out.println("\t\t\t" + id + "\t\t" + entry.getValue().getObjectType());
                    }
                    String sql = """
                            BEGIN TRANSACTION;
                            UPDATE House SET priority_object_id = NULL WHERE priority_object_id = ?;
                            DELETE FROM PriorityObject WHERE id = ?;
                            COMMIT;
                            """;
                    Object[] params = {idPriorityObject, idPriorityObject};
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
        } else {
            System.out.println("\t\t\tRecord not exist");
        }


    }

    //hien thi danh sach PriorityObject
    public void displayPriorityObjects(Map<Integer, PriorityObject> priorityObjects) {
        System.out.println("\t\t\tLIST PRIORITY OBJECT");
        System.out.println("\t\t\t\u001B[1mID\t\tOBJECT_TYPE\u001B[0m");

        for (Map.Entry<Integer, PriorityObject> entry : priorityObjects.entrySet()) {
            System.out.println("\t\t\t" + entry.getKey() + "\t\t" + entry.getValue().getObjectType());
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