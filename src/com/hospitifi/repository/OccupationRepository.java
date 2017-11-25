package com.hospitifi.repository;

import com.hospitifi.model.Occupation;

import java.util.List;

public interface OccupationRepository extends Repository<Occupation, Long> {

    List<Occupation> findGuestOccupations(String guestName);

}
