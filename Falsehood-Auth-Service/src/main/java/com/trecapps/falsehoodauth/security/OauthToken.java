package com.trecapps.falsehoodauth.security;

import org.springframework.stereotype.Component;

@Component
public class OauthToken {

    String access_token, refresh_token, id_token, token_type;

    int expires_in;

    public OauthToken() {
    }

    public OauthToken(String access_token, String refresh_token, String id_token, String token_type, int expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.id_token = id_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}
