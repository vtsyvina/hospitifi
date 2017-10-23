package com.hospitifi.repository.impl;

import com.hospitifi.model.User;
import com.hospitifi.repository.UserRepository;
import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository {
    //it should be a Singleton
    private static final UserRepository instance = new UserRepositoryImpl();
    private static final String GET_USER_BY_LOGIN_AND_PASS = "SELECT ID, LOGIN, PASSWORD, ROLE FROM USERS WHERE LOGIN = ? AND PASSWORD = ?";

    public static UserRepository getInstance(){
        return instance;
    }

    private UserRepositoryImpl(){

    }
    @Override
    public User getUserByLoginAndPass(String login, String password) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN_AND_PASS);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(resultSet.getLong("ID"),
                        resultSet.getString("LOGIN"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getString("ROLE"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public User get(Long id) {
        return null;
    }

    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public boolean save(User entity) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
