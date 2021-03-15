package com.example.account_manager.controllers;

// controller <- endpoint <- service <- facade (service) <- entity manager

import com.example.account_manager.utils.JWTGeneratorVerifier;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("authenticate")
public class LoginController {

    public LoginController() {
    }


    @Inject
    private IdentityStoreHandler identityStoreHandler;

    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_PLAIN})
    public Response authenticate(@NotNull LoginData loginData){
        Credential credential = new UsernamePasswordCredential(loginData.getLogin(), new Password(loginData.getPassword()));
        CredentialValidationResult result = identityStoreHandler.validate(credential);
        if(result.getStatus() == CredentialValidationResult.Status.VALID){
            return Response.accepted()
                    .type("application/jwt")
                    .entity(JWTGeneratorVerifier.generateJWTString(result))
                    .build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


    public static class LoginData{
        private String login;
        private String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
