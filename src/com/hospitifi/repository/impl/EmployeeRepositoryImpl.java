package com.hospitifi.repository.impl;

import com.hospitifi.model.Employee;
import com.hospitifi.model.WorkingTimeUnit;
import com.hospitifi.repository.EmployeeRepository;
import com.hospitifi.util.NamedParameterStatement;
import com.hospitifi.util.SQLiteJDBCDriverConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    private static final EmployeeRepository instance = new EmployeeRepositoryImpl();
    private static final String GET_BY_ID = "SELECT ID, NAME, POSITION FROM EMPLOYEES WHERE ID = ?";
    private static final String GET_ALL_EMPLOYEES = "SELECT ID, NAME, POSITION FROM EMPLOYEES";
    private static final String GET_WORKING_HOURS = "SELECT EMPLOYEE_ID, DAY, START_TIME, END_TIME FROM EMPLOYEES_WORK_HOURS WHERE EMPLOYEE_ID = ?";
    private static final String UPDATE_EMPLOYEE = "UPDATE EMPLOYEES SET NAME = ?, POSITION = ? WHERE ID = ?";
    private static final String DELETE_EMPLOYEE = "DELETE FROM EMPLOYEES_WORK_HOURS WHERE EMPLOYEE_ID = :id; DELETE FROM EMPLOYEE WHERE ID = :id";
    private static final String INSERT_EMPLOYEE = "INSERT INTO EMPLOYEES (NAME, POSITION) VALUES (?,?)";
    private static final String INSERT_WORKING_HOURS = "INSERT INTO EMPLOYEES_WORK_HOURS (EMPLOYEE_ID, DAY, START_TIME, END_TIME) VALUES (?,?,?,?)";
    private static final String DELETE_WORKING_HOURS = "DELETE FROM EMPLOYEES_WORK_HOURS WHERE EMPLOYEE_ID = ?";
    private static final String GET_WORKING_EMPLOYEES = "SELECT ID, NAME, POSITION FROM EMPLOYEES WHERE (SELECT COUNT(*) FROM EMPLOYEES_WORK_HOURS WHERE EMPLOYEE_ID = ID AND DAY = ?) > 0";
    private static final String TIME_PATTERN = "HH:mm";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(TIME_PATTERN);

    public static EmployeeRepository getInstance(){
        return instance;
    }

    private EmployeeRepositoryImpl(){}

    @Override
    public List<Employee> getWorkingEmployees(String dayOfWeek) {
        List<Employee> employees = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return employees;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_WORKING_EMPLOYEES);
            statement.setString(1, dayOfWeek.toUpperCase());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = getEmployeeFromResultSet(resultSet);
                fillEmployeeWorkingHours(employee);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return employees;
    }

    @Override
    public Employee get(Long id) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return null;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_ID);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Employee employee = getEmployeeFromResultSet(resultSet);
                fillEmployeeWorkingHours(employee);
                return employee;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return null;
    }

    @Override
    public boolean update(Employee entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPosition());
            statement.setLong(3, entity.getId());
            statement.execute();
            statement = connection.prepareStatement(DELETE_WORKING_HOURS);
            statement.setLong(1, entity.getId());
            statement.execute();
            insertWorkingHours(entity, connection);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }


    @Override
    public boolean save(Employee entity) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return false;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getPosition());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int newId = rs.getInt(1);
                entity.setId(newId);
            }
            insertWorkingHours(entity, connection);
            return true;
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
            NamedParameterStatement statement = new NamedParameterStatement(connection, DELETE_EMPLOYEE);
            statement.setLong("id", id);
            return statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return false;
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null) {
            return employees;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_ALL_EMPLOYEES);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Employee employee = getEmployeeFromResultSet(resultSet);
                fillEmployeeWorkingHours(employee);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
        return employees;
    }

    private Employee getEmployeeFromResultSet(ResultSet resultSet) throws SQLException {
        return new Employee(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("POSITION"));
    }

    private List<WorkingTimeUnit> getHoursFromResultSet(ResultSet resultSet, Employee employee) throws SQLException {
        List<WorkingTimeUnit> result = new ArrayList<>();
        while (resultSet.next()) {
            try {
                Date date = DATE_FORMAT.parse(resultSet.getString("START_TIME"));
                Calendar start = GregorianCalendar.getInstance();
                start.setTime(date);
                date = DATE_FORMAT.parse(resultSet.getString("END_TIME"));
                Calendar end = GregorianCalendar.getInstance();
                end.setTime(date);
                result.add(new WorkingTimeUnit(employee, DayOfWeek.valueOf(resultSet.getString("DAY").toUpperCase()), start, end));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    private void fillEmployeeWorkingHours(Employee employee) {
        Connection connection = SQLiteJDBCDriverConnection.getConnection();
        if (connection == null || employee == null) {
            return;
        }
        try {
            PreparedStatement statement = connection.prepareStatement(GET_WORKING_HOURS);
            statement.setLong(1, employee.getId());
            ResultSet resultSet = statement.executeQuery();
            employee.setWorkingTime(getHoursFromResultSet(resultSet, employee));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLiteJDBCDriverConnection.closeConnection(connection);
        }
    }

    private void insertWorkingHours(Employee entity, Connection connection) throws SQLException {
        PreparedStatement statement;
        if (entity.getWorkingTime() != null) {
            statement = connection.prepareStatement(INSERT_WORKING_HOURS);
            for (WorkingTimeUnit unit : entity.getWorkingTime()) {
                statement.setLong(1, entity.getId());
                statement.setString(2, unit.getDay().name());
                statement.setString(3, DATE_FORMAT.format(unit.getStart().getTime()));
                statement.setString(4, DATE_FORMAT.format(unit.getEnd().getTime()));
                statement.execute();
            }
        }
    }
}
