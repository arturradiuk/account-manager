package com.example.account_manager.services;


import com.example.account_manager.filters.EntitySignatureValidatorFilterBinding;
import com.example.account_manager.model.Account;
import com.example.account_manager.repositories.AccountRepository;
import com.example.account_manager.utils.EntityIdentitySignerVerifier;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

//@RequestScoped // previous version
@ApplicationScoped // necessary for put method
@Path("model.account")
public class AccountService {

    @Inject
    AccountRepository accountRepository;

    public AccountService() {
    }

    @GET
    @Path("_self")
    @Produces({MediaType.APPLICATION_JSON})
    public Account findSelf(@Context SecurityContext securityContext) {
        return accountRepository.findByLogin(securityContext.getUserPrincipal().getName());
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Account account) {
        accountRepository.create(account);
    }

    @PUT
    @Path("{login}")
    @Consumes({MediaType.APPLICATION_JSON})
    @EntitySignatureValidatorFilterBinding // to change something you need to get it firstly
    public void edit(@PathParam("login") String login, @HeaderParam("If-Match") @NotNull @NotEmpty String tagValue, Account account) throws Exception {

        if (!EntityIdentitySignerVerifier.verifyEntityIntegrity(tagValue, account)) {
            throw new Exception("Integrity broken");
        }
        Account storedAccount = accountRepository.findByLogin(login);
        if (null == storedAccount) {
            throw new Exception("Not found");
        }
        storedAccount.setEmail(account.getEmail());
    }

    @DELETE
    @Path("{login}")
    public void remove(@PathParam("login") String login) {
        accountRepository.removeByLogin(login);
    }

    @GET
    @Path("{login}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response find(@PathParam("login") String login) throws Exception {
        Account storedAccount = accountRepository.findByLogin(login);
        if (null == storedAccount) {
            throw new Exception("Not found");
        }
        return Response.ok()
                .entity(storedAccount)
                .tag(EntityIdentitySignerVerifier // ETag // todo remove this
                        .calculateEntitySignature(storedAccount))
                .build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Account> findAll() {
        return accountRepository.findAll();
    }


//    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    @GET
//    @Path("_self")
//    @Produces({ MediaType.APPLICATION_JSON})
//    public Account findSelf(@Context SecurityContext securityContext)
//    {
//        return accountRepo.findByLogin(securityContext.getUserPrincipal().getName());
//    }
//
//    @POST
//    @Consumes({ MediaType.APPLICATION_JSON})
//    public void create(Account account){
//        accountRepo.create(account);
//    }
//
//    @GET
//    @Path("count")
//    @Produces(MediaType.TEXT_PLAIN)
//    public String countREST(){
//        return String.valueOf(accountRepo.count());
//    }
//
//    @PUT
//    @Path("{login}")
//    @Consumes({MediaType.APPLICATION_JSON})
//    public void edit(@PathParam("login") String login, Account account) throws Exception {
//        Account storedAccount = accountRepo.findByLogin(login);
//        if (null == storedAccount) throw new Exception(account.toString());
//        storedAccount.setEmail(account.getEmail());
//    }
//
//    @GET
//    @Path("{from}/{to}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Account> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
//        return accountRepo.findRange(new int[]{from, to});
//    }
//
//    @GET
//    @Produces({MediaType.APPLICATION_JSON})
//    public List<Account> findAll() {
//        return accountRepo.findAll();
//    }
//
//    @DELETE
//    @Path("{login}")
//    public void remove(@PathParam("login") String login) {
//        accountRepo.removeByLogin(login);
//    }
//
//    @GET
//    @Path("{login}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Account find(@PathParam("login") String login) {
//        return accountRepo.findByLogin(login);
//    }

}
