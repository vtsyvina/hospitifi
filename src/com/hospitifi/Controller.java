package com.hospitifi;

import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {

    public void checkLogin(){
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");
            System.out.println("Done");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
