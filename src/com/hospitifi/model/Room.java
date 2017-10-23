package com.hospitifi.model;

import java.util.Objects;

public class Room {
    private long id;
    private String number;
    private int floor;
    private int beds;
    private BedType bedType;
    private boolean safe;
    private boolean bath;
    private int rateCategory;

    public Room() {
    }

    public Room(long id, String number, int floor, int beds, BedType bedType, boolean safe, boolean bath, int rateCategory) {
        this.id = id;
        this.number = number;
        this.floor = floor;
        this.beds = beds;
        this.bedType = bedType;
        this.safe = safe;
        this.bath = bath;
        this.rateCategory = rateCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }

    public BedType getBedType() {
        return bedType;
    }

    public void setBedType(BedType bedType) {
        this.bedType = bedType;
    }

    public boolean isSafe() {
        return safe;
    }

    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public boolean isBath() {
        return bath;
    }

    public void setBath(boolean bath) {
        this.bath = bath;
    }

    public int getRateCategory() {
        return rateCategory;
    }

    public void setRateCategory(int rateCategory) {
        this.rateCategory = rateCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id &&
                floor == room.floor &&
                beds == room.beds &&
                safe == room.safe &&
                bath == room.bath &&
                rateCategory == room.rateCategory &&
                Objects.equals(number, room.number) &&
                bedType == room.bedType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, floor, beds, bedType, safe, bath, rateCategory);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Room{");
        sb.append("id=").append(id);
        sb.append(", number='").append(number).append('\'');
        sb.append(", floor=").append(floor);
        sb.append(", beds=").append(beds);
        sb.append(", bedType=").append(bedType);
        sb.append(", safe=").append(safe);
        sb.append(", bath=").append(bath);
        sb.append(", rateCategory=").append(rateCategory);
        sb.append('}');
        return sb.toString();
    }


}
