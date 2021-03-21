package com.tuo.housekeeping.model;

public class AppVersionModel {
    String Appname;
    String AppVersion;

    public AppVersionModel(String appname, String appVersion) {
        Appname = appname;
        AppVersion = appVersion;
    }

    public String getAppname() {
        return Appname;
    }

    public String getAppVersion() {
        return AppVersion;
    }
}
