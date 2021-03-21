package com.tuo.housekeeping.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.tuo.housekeeping.model.LoginUserModel;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME="my_shared_preff";
    private static SharedPrefManager mInstance;
    private Context mCtx;

    private SharedPrefManager(Context mCtx){
        this.mCtx=mCtx;
    }

    public static synchronized SharedPrefManager getInstance(Context mCtx){
        if(mInstance==null){
            mInstance=new SharedPrefManager(mCtx);
        }
        return mInstance;
    }

    public void saveUser(String getAccess_token,String getRefresh_token){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("access_token",getAccess_token);
        editor.putString("refresh_token",getRefresh_token);
        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("access_token", null) != null;
    }
    public LoginUserModel getUser(){
        SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new LoginUserModel(
                sharedPreferences.getString("access_token",null),
                sharedPreferences.getString("refresh_token",null)

        );
    }
public void clear(){
    SharedPreferences sharedPreferences=mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    SharedPreferences.Editor editor=sharedPreferences.edit();
    editor.clear();
    editor.apply();
}
}
