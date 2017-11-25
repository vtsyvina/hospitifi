package com.hospitifi.service.impl;

import com.hospitifi.model.Occupation;
import com.hospitifi.model.Reservation;
import com.hospitifi.repository.OccupationRepository;
import com.hospitifi.repository.ReservationRepository;
import com.hospitifi.service.OccupationService;
import com.hospitifi.util.RepositoryFactory;

import java.util.List;

public class OccupationServiceImpl implements OccupationService {
    private static final OccupationService instance = new OccupationServiceImpl();
    private OccupationRepository repository = RepositoryFactory.getOccupationRepository();
    private ReservationRepository reservationRepository = RepositoryFactory.getReservationRepository();


    public static OccupationService getInstance(){
        return instance;
    }

    private OccupationServiceImpl(){}

    @Override
    public List<Occupation> findGuestOccupations(String guestName) {
        return repository.findGuestOccupations(guestName);
    }

    @Override
    public boolean saveOccupationAndCancelReservation(Occupation entity, Reservation reservation) {
        reservation.setCanceled(true);
        reservationRepository.update(reservation);
        return repository.save(entity);
    }

    @Override
    public Occupation get(Long id) {
        return repository.get(id);
    }

    @Override
    public boolean update(Occupation entity) {
        return repository.update(entity);
    }

    @Override
    public boolean save(Occupation entity) {
        return repository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return repository.delete(id);
    }

    @Override
    public List<Occupation> getAll() {
        return repository.getAll();
    }
}
