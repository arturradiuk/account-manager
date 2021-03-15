package com.example.account_manager.repositories;


import com.example.account_manager.model.AccessLevel;
import com.example.account_manager.model.Account;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AccountRepository {
    private final List<Account> accounts = new ArrayList<>();

    public AccountRepository() { // will be used during injecting, consider also @PostConstruct
        Set<AccessLevel> al = new HashSet<>();
        al.add(AccessLevel.ADMIN);
        Account account = new Account("lemmy", "no@where.com", "pas", true, al);
        accounts.add(account);

        al = new HashSet<>();
        al.add(AccessLevel.CLIENT);
        account = new Account("bruce", "bruce@im.com", "pas", true, al);
        accounts.add(account);

        al = new HashSet<>();
        al.add(AccessLevel.CLIENT);
        account = new Account("james", "james@ma.com", "pas", false, al);
        accounts.add(account);
    }

    public Account findByLogin(String name) {
        return this.accounts.stream().filter(account -> account.getLogin().equals(name)).findFirst().get();
    }

    public void create(Account account) {
        this.accounts.add(account);
    }

    public List<Account> findAll() {
        return this.accounts;
    }


    public void removeByLogin(String login) {
        this.accounts.remove(this.findByLogin(login));
    }

    public Account findByLoginPasswordActive(String login, String passwordAsString) {
        Account account = this.findByLogin(login);
        if (account.getPassword().equals(passwordAsString.trim()) && account.isActive())
            return account;

        return null;
    }
}
