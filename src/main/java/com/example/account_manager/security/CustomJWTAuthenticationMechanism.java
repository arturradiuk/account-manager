package com.example.account_manager.security;

import com.example.account_manager.utils.JWTGeneratorVerifier;
import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@ApplicationScoped
public class CustomJWTAuthenticationMechanism implements HttpAuthenticationMechanism { // used to handle requests, check if the user has appropriate token and notify container about login with particular set of Roles

    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String BEARER = "Bearer ";

    @Inject
    private JWTGeneratorVerifier jwtGeneratorVerifier;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, HttpMessageContext httpMessageContext) throws AuthenticationException {
        if (httpServletRequest.getRequestURL().toString().endsWith("/api/authenticate")) { // this is public resource, you don't have jwt token to access it
            return httpMessageContext.doNothing();
        }

        String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            return httpMessageContext.responseUnauthorized  ();
        }

        String jwtSerializedToken = authorizationHeader.substring((BEARER.length())).trim();
        if (JWTGeneratorVerifier.validateJWTSignature(jwtSerializedToken)) {
            try {
                SignedJWT jwtToken = SignedJWT.parse(jwtSerializedToken);
                String login = jwtToken.getJWTClaimsSet().getSubject();
                String groups = jwtToken.getJWTClaimsSet().getStringClaim("auth");
                Date expirationTime = (Date) (jwtToken.getJWTClaimsSet().getClaim("exp"));
                boolean tokenExpired = new Date().after(expirationTime);

                if (tokenExpired) {
                    return httpMessageContext.responseUnauthorized();
                }

                return httpMessageContext.notifyContainerAboutLogin(login, new HashSet<>(Arrays.asList(groups.split(","))));
            } catch (ParseException ex) {
                return httpMessageContext.responseUnauthorized();
            }
        }
        return null;
    }

}
