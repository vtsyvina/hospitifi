package com.hospitifi.repository;

import com.hospitifi.model.Reservation;

import java.util.List;

public interface ReservationRepository extends Repository<Reservation, Long> {

    List<Reservation> findGuestReservations(String guestName);

}
