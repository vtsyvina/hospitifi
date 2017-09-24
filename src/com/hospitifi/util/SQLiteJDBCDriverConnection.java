package com.hospitifi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteJDBCDriverConnection {
    /**
     * Connect to a sample database
     */
    public static Connection getConnection() {
        try {
            // db parameters
            String url = "jdbc:sqlite:db/hospitifi.db";
            // create a connection to the database
            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void closeConnection(Connection conn){
        try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
    }
}
