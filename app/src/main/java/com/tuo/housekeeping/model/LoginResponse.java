package com.tuo.housekeeping.model;

public class LoginResponse {
    private String access_token;
    private String refresh_token;
    private String token_type;
    private String expires_in;

    public LoginResponse(String access_token, String refresh_token, String token_type, String expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getExpires_in() {
        return expires_in;
    }
}
