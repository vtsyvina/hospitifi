package com.hospitifi.model;

import java.util.Date;
import java.util.Objects;

public class Reservation {
    private long id;
    private long roomId;
    private Date checkIn;
    private Date checkOut;
    private String guest_name;
    private boolean canceled;

    public Reservation() {
    }

    public Reservation(long id, long roomId, Date checkIn, Date checkOut, String guest_name, boolean canceled) {
        this.id = id;
        this.roomId = roomId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.guest_name = guest_name;
        this.canceled = canceled;
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

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }

    public String getGuest_name() {
        return guest_name;
    }

    public void setGuest_name(String guest_name) {
        this.guest_name = guest_name;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return id == that.id &&
                roomId == that.roomId &&
                canceled == that.canceled &&
                Objects.equals(checkIn, that.checkIn) &&
                Objects.equals(checkOut, that.checkOut) &&
                Objects.equals(guest_name, that.guest_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, checkIn, checkOut, guest_name, canceled);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Reservation{");
        sb.append("id=").append(id);
        sb.append(", roomId=").append(roomId);
        sb.append(", checkIn=").append(checkIn);
        sb.append(", checkOut=").append(checkOut);
        sb.append(", guest_name='").append(guest_name).append('\'');
        sb.append(", canceled=").append(canceled);
        sb.append('}');
        return sb.toString();
    }
}
