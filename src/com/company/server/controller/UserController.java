package com.company.server.controller;

import com.company.server.dao.repo.UserRepository;
import com.company.server.dao.repo.impl.UserRepositoryImpl;
import com.company.shared.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserController {
    private static UserRepository repo = new UserRepositoryImpl();
    private static String encryptBySHA1(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger bi = new BigInteger(1, messageDigest);
            String hashText = bi.toString(16);
            while (hashText.length() < 40) hashText = '0' + hashText;
            return hashText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static synchronized boolean signUp(User user) {
        User opt = repo.findAccountByName(user.getUsername());
        if (opt != null) return false;
        user.setPassword(encryptBySHA1(user.getPassword()));
        repo.save(user);
        return true;
    }
    public static synchronized boolean logIn(User user) {
        User opt = repo.findAccountByNameAndPassword(user.getUsername(),
                                        encryptBySHA1(user.getPassword()));
        if (opt != null) return true;
        return false;
    }
}
