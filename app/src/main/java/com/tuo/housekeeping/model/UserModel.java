package com.tuo.housekeeping.model;

public class UserModel {
    public String username;
    public String empname;
    public String usertype;

    public UserModel(String username, String empname, String usertype) {
        this.username = username;
        this.empname = empname;
        this.usertype = usertype;
    }

    public String getUsername() {
        return username;
    }

    public String getEmpname() {
        return empname;
    }

    public String getUsertype() {
        return usertype;
    }
}
