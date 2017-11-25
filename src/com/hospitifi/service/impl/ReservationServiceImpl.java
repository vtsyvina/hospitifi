package com.hospitifi.service.impl;

import com.hospitifi.model.Reservation;
import com.hospitifi.repository.ReservationRepository;
import com.hospitifi.service.ReservationService;
import com.hospitifi.util.RepositoryFactory;

import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    ReservationRepository repository = RepositoryFactory.getReservationRepository();
    @Override
    public List<Reservation> findGuestReservations(String guestName) {
        return repository.findGuestReservations(guestName);
    }

    @Override
    public Reservation get(Long id) {
        return repository.get(id);
    }

    @Override
    public boolean update(Reservation entity) {
        return repository.update(entity);
    }

    @Override
    public boolean save(Reservation entity) {
        return repository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<Reservation> getAll() {
        return repository.getAll();
    }
}
