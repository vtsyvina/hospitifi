package com.hospitifi.model;

import java.util.List;
import java.util.Objects;

public class Employee {
    private long id;
    private String name;
    private String position;
    private List<WorkingTimeUnit> workingTime;

    public Employee(){}

    public Employee(long id, String name, String position) {
        this.id = id;
        this.name = name;
        this.position = position;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public List<WorkingTimeUnit> getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(List<WorkingTimeUnit> workingTime) {
        this.workingTime = workingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                Objects.equals(name, employee.name) &&
                Objects.equals(position, employee.position) &&
                Objects.equals(workingTime, employee.workingTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, workingTime);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", position='").append(position).append('\'');
        sb.append(", workingTime=").append(workingTime);
        sb.append('}');
        return sb.toString();
    }
}
