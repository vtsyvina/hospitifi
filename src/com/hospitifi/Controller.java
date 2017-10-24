package com.hospitifi;

import com.hospitifi.service.UserService;
import com.hospitifi.service.impl.UserServiceImpl;

public class Controller {
    //dependency injection
    private UserService userRepository = UserServiceImpl.getInstance();

    public void checkLogin(){
        String login = "admin";//it should be from text input
        String pass = "1234";
        //example of how it will work(not repository, but service later. Repository is dumb)
        boolean b = userRepository.authenticateUser(login, pass);
        if (b){
            System.out.println("Grats! You are logged in");
        } else {
            System.out.println("Sorry, wrong credentials");
        }
    }

}
