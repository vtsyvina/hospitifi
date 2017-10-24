package com.hospitifi.service;

import com.hospitifi.model.User;

/**
 * UserService assumes that for update, save methods entity should contain raw password,
 * service will
 */
public interface UserService extends Service<User, Long> {

    boolean authenticateUser(String login, String password);

    User getCurrentUser();

    void logOut();

    String getCurrentUserRole();
}
