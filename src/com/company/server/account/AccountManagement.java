package com.company.server.account;

import com.company.server.dao.repo.AccountRepository;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class AccountManagement {
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
    public String signUp(Account account) {
        Optional<Account> opt = repo.findAccountByName(account.getName());
        if (opt.isPresent()) return "The username has been taken. Please sign up by another name";
        account.setPassword(encryptBySHA1(account.getPassword()));
        repo.save(account);
        return "Sign up success";
    }
    public boolean signIn(Account account) {
        Optional<Account> opt = repo.findAccountByNameAndPassword(account.getName(),
                                        encryptBySHA1(account.getPassword()));
        if (opt.isPresent()) return true;
        return false;
    }
}
