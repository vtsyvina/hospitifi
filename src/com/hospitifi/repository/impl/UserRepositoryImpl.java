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
    private static final String GET_USER_BY_ID = "SELECT ID, LOGIN, PASSWORD, ROLE FROM USERS WHERE ID = ?";
    private static final String UPDATE_USER = "UPDATE USERS SET LOGIN = ?, PASSWORD = ?, ROLE = ? WHERE ID = ?";
    private static final String DELETE_USER = "DELETE FROM USERS WHERE ID = ?";
    private static final String INSERT_USER = "INSERT INTO USERS (LOGIN, PASSWORD, ROLE) VALUES (?, ?, ?)";

    public static UserRepository getInstance(){
        return instance;
    }

    private UserRepositoryImpl(){

    }
    @Override
    public User getUserByLoginAndPass(String login, String passwordHash) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_LOGIN_AND_PASS);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, passwordHash);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(resultSet.getLong("ID"),
                        resultSet.getString("LOGIN"),
                        null,
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
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_BY_ID);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return new User(resultSet.getLong("ID"),
                        resultSet.getString("LOGIN"),
                        null,
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
    public boolean update(User entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPasswordHash());
            preparedStatement.setString(3, entity.getRole());
            preparedStatement.setLong(4, entity.getId());
            return  preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean save(User entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement.setString(1, entity.getLogin());
            preparedStatement.setString(2, entity.getPasswordHash());
            preparedStatement.setString(3, entity.getRole());
            return  preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null){
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
            preparedStatement.setLong(1, id);
            return  preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }
}
