package com.hospitifi.service.impl;

import com.hospitifi.model.Room;
import com.hospitifi.repository.RoomRepository;
import com.hospitifi.repository.impl.RoomRepositoryImpl;
import com.hospitifi.service.RoomService;

import java.util.Calendar;
import java.util.List;

public class RoomServiceImpl implements RoomService{
    private static final RoomService instance = new RoomServiceImpl();
    private RoomRepository roomRepository = RoomRepositoryImpl.getInstance();

    public static RoomService getInstance() {
        return instance;
    }
    private RoomServiceImpl(){}
    @Override
    public int getRoomRate(int rateCategory) {
        return 0;
    }

    @Override
    public List<Room> findAvailableRooms(Calendar from, Calendar to) {
        return roomRepository.findAvailableRooms(from, to);
    }

    @Override
    public Room get(Long id) {
        return roomRepository.get(id);
    }

    @Override
    public boolean update(Room entity) {
        return roomRepository.update(entity);
    }

    @Override
    public boolean save(Room entity) {
        return roomRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return roomRepository.delete(id);
    }

    @Override
    public List<Room> getAll() {
        return roomRepository.getAll();
    }
}
