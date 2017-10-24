package com.hospitifi.repository;

import com.hospitifi.model.Room;

import java.util.Calendar;
import java.util.List;

public interface RoomRepository extends Repository<Room, Long> {

        int getRoomRate(int rateCategory);

        List<Room> findAvailableRooms(Calendar from, Calendar to);
}
