package com.hospitifi.repository;

import com.hospitifi.model.User;

public interface UserRepository extends Repository<User, Long> {

    User getUserByLoginAndPass(String login, String password);
}
