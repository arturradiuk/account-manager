package com.example.account_manager.model;


import com.example.account_manager.adapters.SerializeStringToEmptyAdapter;
import com.example.account_manager.security.SignableEntity;

import javax.json.bind.annotation.JsonbNillable;
import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbTypeAdapter;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@JsonbNillable(value = true)
public class Account implements Serializable, SignableEntity {

    @JsonbProperty
    private String login;
    @JsonbProperty
    private boolean active;
    @JsonbProperty
    private String email;
    @JsonbProperty
    private String password;
    @JsonbProperty
    private Set<AccessLevel> accessLevels;

    public Account() {

    }

    public Account(@JsonbProperty("login") String login,
                   @JsonbProperty("email") String email,
                   @JsonbProperty("password") String password,
                   @JsonbProperty("active") boolean active,
                   @JsonbProperty("accessLevels") Set<AccessLevel> accessLevels) {
        this.login = login;
        this.active = active;
        this.email = email;
        this.password = password;
        this.accessLevels = accessLevels;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonbTypeAdapter(SerializeStringToEmptyAdapter.class)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    @JsonbTransient
    public String getSignablePayload() { // dane nie naruszone
        return login;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + Objects.hashCode(this.login);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!Objects.equals(this.login, ((Account) obj).login)) {
            return false;
        }
        return true;
    }

    public Set<AccessLevel> getAccessLevels() {
        return accessLevels;
    }

    public void setAccessLevels(Set<AccessLevel> accessLevels) {
        this.accessLevels = accessLevels;
    }
}
