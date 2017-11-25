package com.hospitifi.service;

import com.hospitifi.model.Reservation;

import java.util.List;

public interface ReservationService extends Service<Reservation, Long>{

    List<Reservation> findGuestReservations(String guestName);
}
