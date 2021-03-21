package com.tuo.housekeeping.model;

public class LoginUserModel {

    String access_token;
    String refresh_token;

    public LoginUserModel(String access_token, String refresh_token) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
    }

    public String getAccess_token() {
        return access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }
}
