package com.company.server.controller;

import com.company.server.dao.repo.UserRepository;
import com.company.server.dao.repo.impl.UserRepositoryImpl;
import com.company.shared.entity.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

public class UserController {

    private final static UserRepository repo;

    static {
        Connection con = null;

        try {
            con = DriverManager.getConnection(System.getenv("url"),
                    System.getenv("host"),
                    System.getenv("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        repo = new UserRepositoryImpl(con);
    }

    private static String encryptBySHA1(String str) {
        MessageDigest messageDigest = null;

        try { messageDigest = MessageDigest.getInstance("SHA-1"); } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        byte[] bytes = Objects.requireNonNull(messageDigest).digest(str.getBytes());

        BigInteger bi = new BigInteger(1, bytes);

        StringBuilder strBuilder = new StringBuilder(bi.toString());

        while (strBuilder.length() < 40) strBuilder.insert(0, '0');

        return strBuilder.toString();
    }

    public static boolean signUp(User user) throws SQLException {
        Optional<User> opt = repo.findAccountByName(user.getUsername());

        if (opt.isPresent()) return false;

        user.setPassword(encryptBySHA1(user.getPassword()));

        repo.save(user);

        return true;
    }

    public static boolean logIn(User user) throws SQLException {
        Optional<User> opt = repo.findAccountByNameAndPassword(user.getUsername(),
                                        encryptBySHA1(user.getPassword()));
        return opt.isPresent();
    }
}
