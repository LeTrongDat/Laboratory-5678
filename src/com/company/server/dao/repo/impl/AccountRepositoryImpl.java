package com.company.server.dao.repo.impl;

import com.company.server.user.User;
import com.company.server.dao.repo.AccountRepository;

import java.sql.Connection;

public class AccountRepositoryImpl implements AccountRepository {

    private Connection connection;

    public AccountRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User findAccountByName(String name) {
        String SQL = "SELECT * FROM account WHERE name = " + name;
        return null;
    }

    @Override
    public User findAccountByNameAndPassword(String name, String password) {
        String SQL = String.format("SELECT * FROM account WHERE name = %s AND password = %s",
                name, password);
        return null;
    }

    @Override
    public void save(User user) {
        String SQL = String.format("INSERT INTO account VALUE(%s, %s)", user.getName(), user.getPassword());
    }
}
