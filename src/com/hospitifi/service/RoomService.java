package com.hospitifi.service;

import com.hospitifi.model.Room;

import java.util.Calendar;
import java.util.List;

public interface RoomService extends Service<Room, Long> {
    int getRoomRate(int rateCategory);

    List<Room> findAvailableRooms(Calendar from, Calendar to);
}
