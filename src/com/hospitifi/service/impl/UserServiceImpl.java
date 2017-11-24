package com.hospitifi.service.impl;

import com.hospitifi.model.User;
import com.hospitifi.repository.UserRepository;
import com.hospitifi.service.UserService;
import com.hospitifi.util.RepositoryFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserServiceImpl implements UserService {
    private static final UserService instance = new UserServiceImpl();
    private static final String SALT = "w9V_mM1#";
    private static User currentUser;
    private UserRepository userRepository = RepositoryFactory.getUserRepository();

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        return instance;
    }

    @Override
    public boolean authenticateUser(String login, String password) {
        currentUser = userRepository.getUserByLoginAndPass(login, getPasswordHash(password));
        return currentUser != null;
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logOut() {
        currentUser = null;
    }

    @Override
    public String getCurrentUserRole() {
        return currentUser == null ? null : currentUser.getRole();
    }

    @Override
    public User get(Long id) {
        return userRepository.get(id);
    }

    @Override
    public boolean update(User entity) {
        if (entity.getPassword() != null){
            entity.setPasswordHash(getPasswordHash(entity.getPassword()));
        }
        return userRepository.update(entity);
    }

    @Override
    public boolean save(User entity) {
        if (entity.getPassword() != null){
            entity.setPasswordHash(getPasswordHash(entity.getPassword()));
        }
        return userRepository.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    private String getPasswordHash(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] digest = instance.digest((password + SALT).getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte aDigest : digest) {
                hexString.append(Integer.toHexString(0xFF & aDigest));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("We have a problem with hashing algorithm");
            e.printStackTrace();
            return null;
        }
    }
}
