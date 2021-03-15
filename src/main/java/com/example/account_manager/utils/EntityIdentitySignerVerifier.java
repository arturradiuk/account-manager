package com.example.account_manager.utils;

import com.example.account_manager.model.SignableEntity;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import java.text.ParseException;

public class EntityIdentitySignerVerifier {
    private static final String SECRET = "8YuS04LvRqjpnGnet02bvcdoLIubmcXEt597Gj1rU6bW2MXHvQM90jNnascqF71jsmbp-co91xqE1hie-xKz68BwqAfukX8pGCpXtlzXxrXF_fz46kTcC1HsbvwDzLpxaoAoRKAtEt0onytN4wflPcNvzWjZvAYVcfhb6ydUofU";

    public static String calculateEntitySignature(SignableEntity entity) { // generate entity signature for an entity
        try {
            JWSSigner signer = new MACSigner(SECRET);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(entity.getSignablePayload()));
            jwsObject.sign(signer);

            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return "ETag failure";
        }
    }


    public static boolean validateEntitySignature(String tagValue) {
        try {
            JWSObject jwsObject = JWSObject.parse(tagValue);
            JWSVerifier verifier = new MACVerifier(SECRET);
            return jwsObject.verify(verifier);

        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyEntityIntegrity(String tagValue, SignableEntity entity) {
        try {  // todo null value checking
            final String valueFromIfMatchHeader = JWSObject.parse(tagValue).getPayload().toString(); // if match value

            final String valueFromEntitySignablePayload = entity.getSignablePayload(); // here we get signable payload - value that should be constant (the same in database and in received updated data)

            boolean fist = validateEntitySignature(tagValue);  // if the tag value is valid due to the secret, check if we have created this sign
            boolean second = valueFromEntitySignablePayload.equals(valueFromIfMatchHeader); // compare payload in received entity with payload in if match header

            return fist && second;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}
