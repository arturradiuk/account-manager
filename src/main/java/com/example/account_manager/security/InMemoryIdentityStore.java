package com.example.account_manager.security;


import com.example.account_manager.model.Account;
import com.example.account_manager.repositories.AccountRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class InMemoryIdentityStore
        implements IdentityStore { // used to validate received data,
    // confirm or refuse the fact of account existing (and login, password, activity - integrity)  and in case of success extract login and access levels
    @Inject
    private AccountRepository accountRepository;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return IdentityStore.super.getCallerGroups(validationResult);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential usernamePasswordCredential = (UsernamePasswordCredential) credential;
            Account account = accountRepository.findByLoginPasswordActive(usernamePasswordCredential.getCaller(), usernamePasswordCredential.getPasswordAsString());
            if (account != null) {
                Set<String> accessLevelsStr = new HashSet<>();
                account.getAccessLevels().forEach(al -> accessLevelsStr.add(al.toString()));
                return new CredentialValidationResult(account.getLogin(), accessLevelsStr);
            } else {
                return CredentialValidationResult.INVALID_RESULT;
            }
        }
        return CredentialValidationResult.NOT_VALIDATED_RESULT;

    }
}
