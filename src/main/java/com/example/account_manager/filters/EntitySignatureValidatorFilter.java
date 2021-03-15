package com.example.account_manager.filters;


import com.example.account_manager.utils.EntityIdentitySignerVerifier;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@EntitySignatureValidatorFilterBinding
public class EntitySignatureValidatorFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String header = containerRequestContext.getHeaderString("If-Match");
        if(header == null || header.isEmpty()){
            containerRequestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).build());
        }else if(!EntityIdentitySignerVerifier.validateEntitySignature(header)){
            containerRequestContext.abortWith(Response.status(Response.Status.PRECONDITION_FAILED).build());
        }
    }
}
