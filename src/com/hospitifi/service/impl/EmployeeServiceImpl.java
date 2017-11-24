package com.hospitifi.service.impl;

import com.hospitifi.model.Employee;
import com.hospitifi.repository.EmployeeRepository;
import com.hospitifi.service.EmployeeService;
import com.hospitifi.util.RepositoryFactory;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    private static final EmployeeService instance = new EmployeeServiceImpl();
    private EmployeeRepository repository = RepositoryFactory.getEmployeeRepository();

    public static EmployeeService getInstance(){
        return instance;
    }

    private EmployeeServiceImpl(){}

    @Override
    public List<Employee> getWorkingEmployees(String dayOfWeek) {
        return repository.getWorkingEmployees(dayOfWeek);
    }

    @Override
    public Employee get(Long id) {
        return repository.get(id);
    }

    @Override
    public boolean update(Employee entity) {
        return repository.update(entity);
    }

    @Override
    public boolean save(Employee entity) {
        return repository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<Employee> getAll() {
        return repository.getAll();
    }
}
