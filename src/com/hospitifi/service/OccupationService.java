package com.hospitifi.service;

import com.hospitifi.model.Occupation;
import com.hospitifi.model.Reservation;

import java.util.List;

public interface OccupationService extends Service<Occupation, Long>{

    List<Occupation> findGuestOccupations(String guestName);

    boolean saveOccupationAndCancelReservation(Occupation entity, Reservation reservation);
}
