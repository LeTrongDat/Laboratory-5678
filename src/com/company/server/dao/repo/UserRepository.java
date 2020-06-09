package com.company.server.dao.repo;

import com.company.shared.entity.User;

import java.sql.SQLException;
import java.util.Optional;


public interface UserRepository extends Repository {
    Optional<User> findAccountByName(String name) throws SQLException;
    Optional<User> findAccountByNameAndPassword(String name, String password) throws SQLException;
    void save(User user) throws SQLException;
}
