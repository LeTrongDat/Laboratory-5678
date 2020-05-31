package com.company.server.dao.repo.impl;

import com.company.shared.entity.User;
import com.company.server.dao.repo.UserRepository;

import java.sql.*;

public class UserRepositoryImpl implements UserRepository {
    private Statement stm;

    public UserRepositoryImpl() {
        String url = System.getenv("url");
        String host = System.getenv("host");
        String password = System.getenv("password");
        try {
            Connection con = DriverManager.getConnection(url, host, password);
            stm = con.createStatement();
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }
    @Override
    public User findAccountByName(String name) {
        String SQL = "SELECT * FROM user.user WHERE name = " + name;
        try {
            ResultSet rs = stm.executeQuery(SQL);
            if (rs.next()) return new User(rs.getString("username"), rs.getString("password"));
        } catch (SQLException e) {
            handleSQLException(e);
        }
        return null;
    }

    @Override
    public User findAccountByNameAndPassword(String name, String password) {
        String SQL = String.format("SELECT * FROM user.user WHERE name = %s AND password = %s",
                name, password);
        try {
            ResultSet rs = stm.executeQuery(SQL);
            if (rs.next()) return new User(rs.getString("username"), rs.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void save(User user) {
        String SQL = String.format("INSERT INTO user.user VALUE(%s, %s)", user.getName(), user.getPassword());
        try {
            stm.executeUpdate(SQL);
        } catch (SQLException e) {
            handleSQLException(e);
        }
    }

    @Override
    public void register(User user) {

    }
}
