package com.example.account_manager.mok.servicess;

import com.example.account_manager.model.Account;
import com.example.account_manager.mok.facades.AccountFacade;

import javax.ejb.AccessLocalException;
import javax.inject.Inject;

public class AccountService {
    @Inject
    private AccountFacade accountFacade;

    public Account getAccountByLogin(String login) {
        return this.accountFacade.getAccountByLogin(login);
    }
}
