package com.company.server.dao.repo;

import com.company.server.user.User;

public interface AccountRepository {
    User findAccountByName(String name);
    User findAccountByNameAndPassword(String name, String password);
    void save(User user);
}
