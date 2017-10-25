package com.hospitifi.repository.impl;

import com.hospitifi.model.BedType;
import com.hospitifi.model.Room;
import com.hospitifi.repository.RoomRepository;
import com.hospitifi.util.NamedParameterStatement;
import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {
    private static final RoomRepository instance = new RoomRepositoryImpl();
    private static final String GET_ROOM_BY_ID = "SELECT ID, NUMBER, FLOOR, BEDS, BEDS_TYPE, SAFE, BATH, RATE_CATEGORY FROM ROOM WHERE ID = ?";
    private static final String UPDATE_ROOM = "UPDATE ROOM SET NUMBER =?, FLOOR =?, BEDS =?, BEDS_TYPE =?, SAFE =?, BATH =?, RATE_CATEGORY=? WHERE ID = ?";
    private static final String INSERT_ROOM = "INSERT INTO ROOM (NUMBER, FLOOR, BEDS, BEDS_TYPE, SAFE, BATH, RATE_CATEGORY) VALUES ?,?,?,?,?,?,?";
    private static final String DELETE_ROOM = "DETELE FROM ROOM WHERE ID = ?";
    private static final String FIND_AVAILABLE_ROOMS = "SELECT ID, NUMBER, FLOOR, BEDS, BEDS_TYPE, SAFE, BATH, RATE_CATEGORY FROM ROOM r " +
            "WHERE (" +
            "SELECT COUNT(*) FROM RESERVATION " +
            "WHERE ROOM_ID = r.ID AND CANCELED = 0 " +
            "AND ((CHECK_IN <= :from AND :from <= CHECK_OUT) " +
            "OR (CHECK_IN <= :to AND :to <= CHECK_OUT) " +
            "OR (:from <= CHECK_IN AND CHECK_OUT <= :to)) ) = 0 " +
            "AND ( " +
            "SELECT COUNT(*) FROM OCCUPATION " +
            "WHERE r.id = ID AND ((DATE_START <= :from AND :from <= DATE_END) " +
            "OR (DATE_START <= :to AND :to <= DATE_END) " +
            "OR (:from <= DATE_START AND DATE_END <= :to)) ) = 0";

    public static RoomRepository getInstance(){
        return instance;
    }
    private RoomRepositoryImpl(){}

    @Override
    public int getRoomRate(int rateCategory) {
        return 0;
    }

    /**
     * Usage example:
     * Calendar start = new GregorianCalendar();
            start.set(Calendar.DAY_OF_MONTH, 5);
            start.set(Calendar.MONTH, 6);
            start.set(Calendar.YEAR, 2017);

            Calendar end = new GregorianCalendar();
            end.set(Calendar.DAY_OF_MONTH, 8);
            end.set(Calendar.MONTH, 6);
            end.set(Calendar.YEAR, 2017);
            List<Room> availableRooms = repository.findAvailableRooms(start, end);
     */
    @Override
    public List<Room> findAvailableRooms(Calendar from, Calendar to) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return null;
        }
        try {
            List<Room> result = new ArrayList<>();
            NamedParameterStatement statement = new NamedParameterStatement(connection, FIND_AVAILABLE_ROOMS);
            statement.setLong("from", from.getTime().getTime());
            statement.setLong("to", to.getTime().getTime());

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(new Room(resultSet.getLong("ID"),
                        resultSet.getString("NUMBER"),
                        resultSet.getInt("FLOOR"),
                        resultSet.getInt("BEDS"),
                        BedType.valueOf(getBedType(resultSet)),
                        resultSet.getInt("SAFE") == 1,
                        resultSet.getInt("BATH") == 1,
                        resultSet.getInt("RATE_CATEGORY")));
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    private String getBedType(ResultSet resultSet) throws SQLException {
        String bedsType = resultSet.getString("BEDS_TYPE");
        if (bedsType == null){
            bedsType = "QUEEN";
        }else {
            bedsType = bedsType.toUpperCase();
        }
        return bedsType;
    }

    @Override
    public Room get(Long id) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ROOM_BY_ID);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Room(resultSet.getLong("ID"),
                        resultSet.getString("NUMBER"),
                        resultSet.getInt("FLOOR"),
                        resultSet.getInt("BEDS"),
                        BedType.valueOf(getBedType(resultSet)),
                        resultSet.getInt("SAFE") == 1,
                        resultSet.getInt("BATH") == 1,
                        resultSet.getInt("RATE_CATEGORY"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Room entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ROOM);
            preparedStatement.setString(1, entity.getNumber());
            preparedStatement.setInt(2, entity.getFloor());
            preparedStatement.setInt(3, entity.getBeds());
            preparedStatement.setString(4, entity.getBedType().name());
            preparedStatement.setInt(5, entity.hasSafe() ? 1 : 0);
            preparedStatement.setInt(6, entity.hasBath() ? 1 : 0);
            preparedStatement.setInt(7, entity.getRateCategory());
            preparedStatement.setLong(8, entity.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean save(Room entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ROOM);
            preparedStatement.setString(1, entity.getNumber());
            preparedStatement.setInt(2, entity.getFloor());
            preparedStatement.setInt(3, entity.getBeds());
            preparedStatement.setString(4, entity.getBedType().name());
            preparedStatement.setInt(5, entity.hasSafe() ? 1 : 0);
            preparedStatement.setInt(6, entity.hasBath() ? 1 : 0);
            preparedStatement.setInt(7, entity.getRateCategory());
            return preparedStatement.execute();
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
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ROOM);
            preparedStatement.setLong(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }
}
