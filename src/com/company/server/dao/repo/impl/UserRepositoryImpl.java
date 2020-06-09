package com.company.server.dao.repo.impl;

import com.company.shared.entity.User;
import com.company.server.dao.repo.UserRepository;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private Connection con;

    public UserRepositoryImpl(Connection con) {
        this.con = con;
    }

    @Override
    public Optional<User> findAccountByName(String name) throws SQLException {
        String SQL = String.format("SELECT * FROM user_info WHERE username = '%s'", name);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        return rs.next()
                ? Optional.of(new User(rs.getString("username"), rs.getString("password")))
                : Optional.empty();
    }

    @Override
    public Optional<User> findAccountByNameAndPassword(String name, String password) throws SQLException {
        String SQL = String.format("SELECT * FROM user_info WHERE username = '%s' AND password = '%s'", name, password);

        ResultSet rs = con.createStatement().executeQuery(SQL);

        return rs.next()
                ? Optional.of(new User(rs.getString("username"), rs.getString("password")))
                : Optional.empty();
    }

    @Override
    public void save(User user) throws SQLException {
        String SQL = String.format("INSERT INTO user_info VALUES('%s', '%s')", user.getUsername(), user.getPassword());

        con.createStatement().executeUpdate(SQL);
    }
}
