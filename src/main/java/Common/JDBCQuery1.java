package Common;

import java.sql.*;

public class JDBCQuery1 {
    public JDBCQuery1() {

    }

    public static void openConnection() {
        try {
            Class.forName(JdbcConfig.JDBC_DRIVER);
            JdbcConfig.connection = DriverManager.getConnection(JdbcConfig.JDBC_URL, JdbcConfig.USERNAME, JdbcConfig.PASSWORD);
            //System.out.println("Connected successfully to the database.");
        } catch (SQLException e) {
            System.err.println("Connection error to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm để đóng kết nốid
    public static void closeConnection() {
        if (JdbcConfig.connection != null) {
            try {
                JdbcConfig.connection.close();
                // System.out.println("Connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    // Hàm để thực hiện truy vấn SELECT
    public static ResultSet executeSelectQuery(String query, Object... params) {
        ResultSet resultSet = null;
        try {
            PreparedStatement preparedStatement = JdbcConfig.connection.prepareStatement(query);
            // Đặt các tham số cho truy vấn (nếu có)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực hiện truy vấn SELECT: " + e.getMessage());
        }
        return resultSet;
    }

    // Hàm để thực hiện truy vấn UPDATE hoặc INSERT
    public static int executeUpdateQuery(String query, Object... params) {
        int rowsAffected = 0;
        try {
            PreparedStatement preparedStatement = JdbcConfig.connection.prepareStatement(query);
            // Đặt các tham số cho truy vấn (nếu có)
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            rowsAffected = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Lỗi khi thực hiện truy vấn UPDATE hoặc INSERT: " + e.getMessage());
        }
        return rowsAffected;
    }

}
