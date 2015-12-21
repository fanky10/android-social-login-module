package io.github.fanky10.sociallogin.model;

/**
 * Created by fanky on 12/18/15.
 */
public class BasicUserModel {
    private String username;
    private String password;
    private String scope;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
