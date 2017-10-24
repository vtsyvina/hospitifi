package com.hospitifi.repository.impl;

import com.hospitifi.model.Room;
import com.hospitifi.repository.RoomRepository;

import java.util.Calendar;
import java.util.List;

public class RoomRepositoryImpl implements RoomRepository {
    @Override
    public int getRoomRate(int rateCategory) {
        return 0;
    }

    @Override
    public List<Room> findAvailableRooms(Calendar from, Calendar to) {
        return null;
    }

    @Override
    public Room get(Long id) {
        return null;
    }

    @Override
    public boolean update(Room entity) {
        return false;
    }

    @Override
    public boolean save(Room entity) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
