package com.hospitifi.repository.impl;

import com.hospitifi.model.Occupation;
import com.hospitifi.repository.OccupationRepository;
import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class OccupationRepositoryImpl implements OccupationRepository {
    private static final OccupationRepository instance = new OccupationRepositoryImpl();
    private static final String GET_BY_ID = "SELECT ID, ROOM_ID, ADULTS, CHILDREN, GUEST_NAME, DATE_START, DATE_END, BREAKFAST_INCLUDED, RATE FROM OCCUPATION WHERE ID = ?";
    private static final String GET_BY_GUEST_NAME = "SELECT ID, ROOM_ID, ADULTS, CHILDREN, GUEST_NAME, DATE_START, DATE_END, BREAKFAST_INCLUDED, RATE FROM OCCUPATION WHERE GUEST_NAME = ?";
    private static final String GET_ALL = "SELECT ID, ROOM_ID, ADULTS, CHILDREN, GUEST_NAME, DATE_START, DATE_END, BREAKFAST_INCLUDED, RATE FROM OCCUPATION";
    private static final String UPDATE_OCCUPATION = "UPDATE OCCUPATION SET ROOM_ID = ? , ADULTS = ? , CHILDREN = ? , " +
            "GUEST_NAME = ? , DATE_START = ? , DATE_END = ? , BREAKFAST_INCLUDED = ? , RATE = ?  WHERE ID = ?";
    private static final String INSERT_OCCUPATION = "INSERT INTO OCCUPATION (ROOM_ID, ADULTS, CHILDREN, GUEST_NAME, DATE_START, DATE_END, BREAKFAST_INCLUDED, RATE) VALUES (?,?,?,?,?,?,?,?)";
    private static final String DELETE_OCCUPATION = "DELETE FROM OCCUPATION WHERE ID = ?";

    public static OccupationRepository getInstance() {
        return instance;
    }

    private OccupationRepositoryImpl(){}

    @Override
    public List<Occupation> findGuestOccupations(String guestName) {
        List<Occupation> result = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_GUEST_NAME);
            statement.setString(1, guestName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add( getOccupationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return result;
    }

    @Override
    public Occupation get(Long id) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getOccupationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Occupation entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_OCCUPATION);
            statement.setLong(1, entity.getRoomId());
            statement.setLong(2, entity.getAdults());
            statement.setLong(3, entity.getChildren());
            statement.setString(4, entity.getGuestName());
            statement.setLong(5, entity.getStart().getTimeInMillis());
            statement.setLong(6, entity.getEnd().getTimeInMillis());
            statement.setInt(7, entity.isBreakfastIncluded() ? 1 : 0);
            statement.setInt(8, entity.getRate());
            statement.setLong(9, entity.getId());
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean save(Occupation entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_OCCUPATION);
            statement.setLong(1, entity.getRoomId());
            statement.setLong(2, entity.getAdults());
            statement.setLong(3, entity.getChildren());
            statement.setString(4, entity.getGuestName());
            statement.setLong(5, entity.getStart().getTimeInMillis());
            statement.setLong(6, entity.getEnd().getTimeInMillis());
            statement.setInt(7, entity.isBreakfastIncluded() ? 1 : 0);
            statement.setInt(8, entity.getRate());
            return statement.execute();
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
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(DELETE_OCCUPATION);
            statement.setLong(1, id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Occupation> getAll() {
        List<Occupation> result = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add( getOccupationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return result;
    }

    private Occupation getOccupationFromResultSet(ResultSet resultSet) throws SQLException{
        Calendar in = GregorianCalendar.getInstance();
        in.setTime(resultSet.getDate("DATE_START"));
        Calendar out = GregorianCalendar.getInstance();
        out.setTime(resultSet.getDate("DATE_END"));
        return new Occupation(resultSet.getLong("ID"),
                resultSet.getLong("ROOM_ID"),
                resultSet.getInt("ADULTS"),
                resultSet.getInt("CHILDREN"),
                resultSet.getString("GUEST_NAME"),
                in , out,
                resultSet.getInt("BREAKFAST_INCLUDED") == 1,
                resultSet.getInt("RATE"));
    }
}
