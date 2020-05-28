package com.company.server.dao.repo;

import com.company.shared.entity.User;

public interface UserRepository {
    User findAccountByName(String name);
    User findAccountByNameAndPassword(String name, String password);
    void save(User user);
}
