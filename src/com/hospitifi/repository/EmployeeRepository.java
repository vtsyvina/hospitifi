package com.hospitifi.repository;

import com.hospitifi.model.Employee;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, Long> {

    List<Employee> getWorkingEmployees(String dayOfWeek);
}
