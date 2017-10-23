package com.hospitifi;

import com.hospitifi.model.User;
import com.hospitifi.repository.UserRepository;
import com.hospitifi.repository.impl.UserRepositoryImpl;

public class Controller {
    //dependency injection
    private UserRepository userRepository = UserRepositoryImpl.getInstance();

    public void checkLogin(){
        String login = "admin";//it should be from text input
        String pass = "1234";
        //example of how it will work(not repository, but service later. Repository is dumb)
        User user = userRepository.getUserByLoginAndPass(login, pass);
        if (user != null ){
            System.out.println("Grats! You are logged in");
        } else {
            System.out.println("Sorry, wrong credentials");
        }
    }

}
