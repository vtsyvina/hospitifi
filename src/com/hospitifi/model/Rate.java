package com.hospitifi.model;

import java.util.Calendar;
import java.util.Objects;

public class Rate {
    private long id;
    private int bedType;
    private int rateCategory;
    private Calendar start;
    private Calendar end;

    public Rate() {
    }

    public Rate(long id, int bedType, int rateCategory, Calendar start, Calendar end) {
        this.id = id;
        this.bedType = bedType;
        this.rateCategory = rateCategory;
        this.start = start;
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
    }

    public int getRateCategory() {
        return rateCategory;
    }

    public void setRateCategory(int rateCategory) {
        this.rateCategory = rateCategory;
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
        Rate rate = (Rate) o;
        return id == rate.id &&
                bedType == rate.bedType &&
                rateCategory == rate.rateCategory &&
                Objects.equals(start, rate.start) &&
                Objects.equals(end, rate.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bedType, rateCategory, start, end);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Rate{");
        sb.append("id=").append(id);
        sb.append(", bedType=").append(bedType);
        sb.append(", rateCategory=").append(rateCategory);
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append('}');
        return sb.toString();
    }
}
