package com.company.server.user;

import com.company.server.dao.repo.AccountRepository;
import com.company.server.io.Logback;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserManagement {
    AccountRepository repo;
    private String encryptBySHA1(String str) {
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
    public String signUp(User user) {
        User opt = repo.findAccountByName(user.getName());
        if (opt != null) return "The username has been taken. Please sign up by another name";
        user.setPassword(encryptBySHA1(user.getPassword()));
        repo.save(user);
        return "Sign up success";
    }
    public boolean logIn(User user) {
        User opt = repo.findAccountByNameAndPassword(user.getName(),
                                        encryptBySHA1(user.getPassword()));
        if (opt != null) {
            Logback.logback("Logged in successfully");
            return true;
        }
        return false;
    }
}
