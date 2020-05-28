package com.company.server.dao.repo;

import com.company.server.account.Account;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findAccountByName(String name);
    Optional<Account> findAccountByNameAndPassword(String name, String password);
    void save(Account account);
    void signIn(Account account);
}
