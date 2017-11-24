package com.hospitifi.service;

import com.hospitifi.model.Employee;

import java.util.List;

public interface EmployeeService extends Service<Employee, Long> {

    List<Employee> getWorkingEmployees(String dayOfWeek);
}
