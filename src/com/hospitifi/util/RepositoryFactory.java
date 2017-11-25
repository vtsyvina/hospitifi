package com.hospitifi.util;

import com.hospitifi.repository.EmployeeRepository;
import com.hospitifi.repository.ReservationRepository;
import com.hospitifi.repository.RoomRepository;
import com.hospitifi.repository.UserRepository;
import com.hospitifi.repository.impl.EmployeeRepositoryImpl;
import com.hospitifi.repository.impl.ReservationRepositoryImpl;
import com.hospitifi.repository.impl.RoomRepositoryImpl;
import com.hospitifi.repository.impl.UserRepositoryImpl;

/**
 * Class is responsible for repository dependency injection
 */
public class RepositoryFactory {

    private RepositoryFactory(){}

    public static EmployeeRepository getEmployeeRepository(){
        return EmployeeRepositoryImpl.getInstance();
    }

    public static ReservationRepository getReservationRepository(){
        return ReservationRepositoryImpl.getInstance();
    }

    public static RoomRepository getRoomRepository(){
        return RoomRepositoryImpl.getInstance();
    }

    public static UserRepository getUserRepository(){
        return UserRepositoryImpl.getInstance();
    }
}
