package com.hospitifi.model;

import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Objects;

/**
 * How to set time for WorkingUnit:
 *  Calendar instance = Calendar.getInstance();
 *  instance.set(Calendar.HOUR_OF_DAY, 18);
 *  instance.set(Calendar.MINUTE, 35);
 */
public class WorkingTimeUnit {
    private Employee employee;
    private DayOfWeek day;
    private Calendar start;
    private Calendar end;

    public WorkingTimeUnit() {
    }

    public WorkingTimeUnit(Employee employee, DayOfWeek day, Calendar start, Calendar end) {
        this.employee = employee;
        this.day = day;
        this.start = start;
        this.end = end;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkingTimeUnit that = (WorkingTimeUnit) o;
        return Objects.equals(employee, that.employee) &&
                day == that.day &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, day, start, end);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WorkingTimeUnit{");
        sb.append("employee=").append(employee.getName());
        sb.append(", day=").append(day);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append('}');
        return sb.toString();
    }
}
