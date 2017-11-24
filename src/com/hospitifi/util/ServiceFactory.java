package com.hospitifi.util;

import com.hospitifi.service.EmployeeService;
import com.hospitifi.service.RoomService;
import com.hospitifi.service.UserService;
import com.hospitifi.service.impl.EmployeeServiceImpl;
import com.hospitifi.service.impl.RoomServiceImpl;
import com.hospitifi.service.impl.UserServiceImpl;


/**
 * Class is responsible for service dependency injection
 */
public class ServiceFactory {

    private ServiceFactory(){}

    public static EmployeeService getEmployeeService(){
        return EmployeeServiceImpl.getInstance();
    }

    public static RoomService getRoomService(){
        return RoomServiceImpl.getInstance();
    }

    public static UserService getUserService(){
        return UserServiceImpl.getInstance();
    }
}
