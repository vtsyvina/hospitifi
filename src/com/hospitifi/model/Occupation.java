package com.hospitifi.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Occupation {
    private long id;
    private long roomId;
    private int adults;
    private int children;
    private String guestName;
    private Calendar start;
    private Calendar end;
    private boolean breakfastIncluded;
    private int rate;

    public Occupation() {
    }

    public Occupation(long id, long roomId, int adults, int children, String guestName, Calendar start, Calendar end, boolean breakfastIncluded, int rate) {
        this.id = id;
        this.roomId = roomId;
        this.adults = adults;
        this.children = children;
        this.guestName = guestName;
        this.start = start;
        this.end = end;
        this.breakfastIncluded = breakfastIncluded;
        this.rate = rate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
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

    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        this.breakfastIncluded = breakfastIncluded;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Occupation that = (Occupation) o;
        return id == that.id &&
                roomId == that.roomId &&
                adults == that.adults &&
                children == that.children &&
                breakfastIncluded == that.breakfastIncluded &&
                rate == that.rate &&
                Objects.equals(guestName, that.guestName) &&
                Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, adults, children, guestName, start, end, breakfastIncluded, rate);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Occupation{");
        sb.append("id=").append(id);
        sb.append(", roomId=").append(roomId);
        sb.append(", adults=").append(adults);
        sb.append(", children=").append(children);
        sb.append(", guestName='").append(guestName).append('\'');
        sb.append(", start=").append(start);
        sb.append(", end=").append(end);
        sb.append(", breakfastIncluded=").append(breakfastIncluded);
        sb.append(", rate=").append(rate);
        sb.append('}');
        return sb.toString();
    }
}
