package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class CitizenObjectManager {
    //Lay tat ca thong tin CitizenObject trong databse khong truyen tham so
    public Map<Integer, CitizenObject> getCitizenObject() {
        Map<Integer, CitizenObject> indexObjectMap = new LinkedHashMap<>();
        try {
            JDBCQuery.openConnection();
            String sql = "select  * from CitizenObject order by id desc";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                int index = 1;
                try {
                    while (rs.next()) {
                        CitizenObject citizenObject = new CitizenObject(
                                rs.getInt("id"),
                                rs.getString("type_name_object"),
                                rs.getInt("coefficient")
                        );
                        indexObjectMap.put(index, citizenObject);
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
        return indexObjectMap;
    }

    //Lay tat ca thong tin CitizenObject trong databse co tham so
    public Map<Integer, CitizenObject> getCitizenObject(String sql) {
        Map<Integer, CitizenObject> indexObjectMap = new LinkedHashMap<>();
        try {
            JDBCQuery.openConnection();
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                int index = 1;
                try {
                    while (rs.next()) {
                        CitizenObject citizenObject = new CitizenObject(
                                rs.getInt("id"),
                                rs.getString("type_name_object"),
                                rs.getInt("coefficient")
                        );
                        indexObjectMap.put(index, citizenObject);
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
        return indexObjectMap;
    }

    //Nhap du lieu
    public CitizenObject inputCitizenObject() {
        String type = InputValidator.validateStringCitizenObject("\t\t\tEnter New TypeNameObject:  ");
        int coefficient = 0;
        boolean check = false;
        do {
            coefficient = InputValidator.validateIntInput("\t\t\tEnter New Coefficient:  ");
            if (coefficient >= 0) {
                check = true;

            } else {
                System.out.println("Error:Coefficient<0 ");
            }
        } while (!check);
        CitizenObject co = new CitizenObject();
        co.setCoefficient(coefficient);
        co.setTypeNameObject(type);
        return co;
    }

    //Them du lieu vao bang CitizenObject
    public void addCitizenObject() {
        try {
            CitizenObject po = inputCitizenObject();
            JDBCQuery.openConnection();
            String sql = "INSERT INTO CitizenObject (type_name_object,coefficient) VALUES (?,?)";
            Object[] prams = {po.getTypeNameObject(), po.getCoefficient()};
            int rs = JDBCQuery.executeUpdateQuery(sql, prams);
            if (rs > 0) {
                System.out.println("\t\t\tAdd success!!");
                displayCitizenObjects(getCitizenObject());
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
        displayCitizenObjects(getCitizenObject());
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to update: ");
        int idCitizenObject = 0;
        if (getCitizenObject().containsKey(id)) {
            idCitizenObject = getCitizenObject().get(id).getId();
            if (isIdExists(idCitizenObject)) {
                getCitizenById(idCitizenObject, id);

                System.out.println("\t\t\tMENU UPDATE");
                System.out.println("\t\t\t1. Update Type Name Object");
                System.out.println("\t\t\t2. Update Coefficient");
                System.out.println("\t\t\t3. Update All");
                String sql = "";
                String type = "";
                String coefficient = "";
                int choose = -1;

                do {
                    choose = InputValidator.validateIntInput("\t\t\tPlease choose 1-3....");
                    if (choose < 1 || choose > 3) {
                        System.out.println("\t\t\tInvalid choice. Please enter a valid option between 1 and 3.");
                    }
                } while (choose < 1 || choose > 3);
                switch (choose) {
                    case 1:
                        System.out.println("\t\t\tUpdate Type Name Object");
                        sql = "UPDATE CitizenObject SET type_name_object = ? WHERE id = ?";
                        type = InputValidator.validateStringPriorityObject("\t\t\tInput Type Name Object: ");
                        break;
                    case 2:
                        System.out.println("\t\t\tUpdate Coefficient");
                        sql = "UPDATE CitizenObject SET coefficient = ? WHERE id = ?";
                        type = InputValidator.validateStringPriorityObject("\t\t\tInput Coefficient: ");
                        break;
                    case 3:
                        System.out.println("\t\t\tUpdate All");
                        sql = "UPDATE CitizenObject\n" +
                                "SET type_name_object=?, coefficient=?\n" +
                                "WHERE id=?;";
                        type = InputValidator.validateStringPriorityObject("\t\t\tInput Type Name Object: ");
                        coefficient = InputValidator.validateStringPriorityObject("\t\t\tInput Coefficient: ");
                        break;
                }

                try {
                    JDBCQuery.openConnection();

                    Object[] params;
                    if (choose == 3) {
                        params = new Object[]{type, coefficient, idCitizenObject};
                    } else {
                        params = new Object[]{type, idCitizenObject};
                    }

                    int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                    if (rowsAffected > 0) {
                        System.out.println("\t\t\tUpdate success!!");
                        displayCitizenObjects(getCitizenObject());
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
        } else {
            System.out.println("\t\t\tRecord not exist");
        }
    }

    private void getCitizenById(int idCitizenObject, int id) {
        System.out.println("\t\t\t\u001B[1mLIST CITIZEN OBJECT\u001B[0m");
        System.out.println("\t\t\tID\t\tObject Type\t\tCoefficient");
        for (Map.Entry<Integer, CitizenObject> entry : getCitizenObject("select * from CitizenObject where id=" + idCitizenObject).entrySet()) {
            String objectType = entry.getValue().getTypeNameObject();
            String coefficient = String.valueOf(entry.getValue().getCoefficient());
            // Set a specific width for each column
            String formattedOutput = String.format("\t\t\t%-8s%-25s%-10s", id, objectType, coefficient);
            System.out.println(formattedOutput);
        }
    }

    //ham xoa  CitizenObject theo ID
    public void deletePriorityObject() {
        displayCitizenObjects(getCitizenObject());
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to delete: ");
        int idCitizenObject = 0;

        if (getCitizenObject().containsKey(id)) {
            idCitizenObject = getCitizenObject().get(id).getId();
            if (isIdExists(idCitizenObject)) {
                getCitizenById(idCitizenObject, id);
                try {
                    JDBCQuery.openConnection();
                    String sql= """
                            BEGIN TRANSACTION;
                            UPDATE Citizen SET citizen_object_id = NULL WHERE citizen_object_id = ?;
                            DELETE FROM CitizenObject WHERE id = ?;
                            COMMIT;
                            """;
                    Object[] params = {idCitizenObject,idCitizenObject};
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
        else {
            System.out.println("\t\t\tRecord not exist");
        }

    }

    //hien thi danh sach CitizenObject
    public void displayCitizenObjects(Map<Integer, CitizenObject> citizenObjects) {
        System.out.println("\t\t\t\u001B[1mLIST CITIZEN OBJECT\u001B[0m");
        System.out.println("\t\t\tID\t\tObject Type\t\tCoefficient");
        for (Map.Entry<Integer, CitizenObject> entry : citizenObjects.entrySet()) {
            String id = String.valueOf(entry.getKey());
            String objectType = entry.getValue().getTypeNameObject();
            String coefficient = String.valueOf(entry.getValue().getCoefficient());

            // Set a specific width for each column
            String formattedOutput = String.format("\t\t\t%-8s%-25s%-10s", id, objectType, coefficient);
            System.out.println(formattedOutput);
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