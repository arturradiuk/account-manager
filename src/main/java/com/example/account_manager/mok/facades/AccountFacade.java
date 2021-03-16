package com.example.account_manager.mok.facades;

import com.example.account_manager.model.Account;
import com.example.account_manager.repositories.AccountRepository;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
@DeclareRoles({"CLIENT"})
public class AccountFacade {
    @Inject
    private AccountRepository accountRepository;

    @RolesAllowed({"CLIENT"})
    public Account getAccountByLogin(String login) {
        return this.accountRepository.findByLogin(login);
    }
}
