package com.hospitifi.repository.impl;

import com.hospitifi.model.Reservation;
import com.hospitifi.repository.ReservationRepository;
import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ReservationRepositoryImpl implements ReservationRepository {
    private static final ReservationRepository instance = new ReservationRepositoryImpl();
    private static final String GET_BY_ID = "SELECT ID, ROOM_ID, CHECK_IN, CHECK_OUT, GUEST_NAME, CANCELED FROM RESERVATION WHERE ID = ?";
    private static final String GET_ALL = "SELECT ID, ROOM_ID, CHECK_IN, CHECK_OUT, GUEST_NAME, CANCELED FROM RESERVATION";
    private static final String FIND_GUEST_RESERVATION = "SELECT ID, ROOM_ID, CHECK_IN, CHECK_OUT, GUEST_NAME, CANCELED FROM RESERVATION WHERE GUEST_NAME = ?";
    private static final String UPDATE_RESERVATION = "UPDATE RESERVATION SET ROOM_ID = ? , CHECK_IN = ?, CHECK_OUT = ?, GUEST_NAME = ?, CANCELED = ? WHERE ID = ?";
    private static final String INSERT_RESERVATION = "INSERT INTO RESERVATION (ROOM_ID, CHECK_IN, CHECK_OUT, GUEST_NAME, CANCELED) VALUES (?,?,?,?,?)";
    private static final String DELETE_RESERVATION = "DELETE FROM RESERVATION WHERE ID = ?";

    public static ReservationRepository getInstance() {
        return instance;
    }
    private ReservationRepositoryImpl(){}

    @Override
    public List<Reservation> findGuestReservations(String guestName) {
        List<Reservation> result = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(FIND_GUEST_RESERVATION);
            statement.setString(1, guestName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add( getReservationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return result;
    }

    @Override
    public Reservation get(Long id) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return getReservationFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Reservation entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_RESERVATION);
            preparedStatement.setLong(1, entity.getRoomId());
            preparedStatement.setLong(2, entity.getCheckIn().getTimeInMillis());
            preparedStatement.setLong(3, entity.getCheckOut().getTimeInMillis());
            preparedStatement.setString(4, entity.getGuestName());
            preparedStatement.setInt(5, entity.isCanceled() ? 1 : 0);
            preparedStatement.setLong(6, entity.getId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public boolean save(Reservation entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RESERVATION);
            preparedStatement.setLong(1, entity.getRoomId());
            preparedStatement.setLong(2, entity.getCheckIn().getTimeInMillis());
            preparedStatement.setLong(3, entity.getCheckOut().getTimeInMillis());
            preparedStatement.setString(4, entity.getGuestName());
            preparedStatement.setInt(5, entity.isCanceled() ? 1 : 0);
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
            PreparedStatement statement = connection.prepareStatement(DELETE_RESERVATION);
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
    public List<Reservation> getAll() {
        List<Reservation> result = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return result;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add( getReservationFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return result;
    }

    private Reservation getReservationFromResultSet(ResultSet resultSet) throws SQLException {
            Calendar in = GregorianCalendar.getInstance();
            in.setTime(resultSet.getDate("CHECK_IN"));
            Calendar out = GregorianCalendar.getInstance();
            out.setTime(resultSet.getDate("CHECK_OUT"));
            return new Reservation(resultSet.getLong("ID"),
                    resultSet.getLong("ROOM_ID"),
                    in , out,
                    resultSet.getString("GUEST_NAME"),
                    resultSet.getInt("CANCELED") == 1);
    }
}
