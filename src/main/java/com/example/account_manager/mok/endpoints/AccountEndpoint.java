package com.example.account_manager.mok.endpoints;

import com.example.account_manager.model.Account;
import com.example.account_manager.mok.servicess.AccountService;

import javax.inject.Inject;

public class AccountEndpoint {
    @Inject
    private AccountService accountService;

    public Account getAccountByLogin(String login) {
        return this.accountService.getAccountByLogin(login);
    }
}
