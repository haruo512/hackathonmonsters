package com.zeroone_creative.basicapplication.model.system;

import android.content.Context;

import com.zeroone_creative.basicapplication.controller.util.SharedPreferencesUtil;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class UserAccount {
    public String id;
    public String username;
    public String password;

    public UserAccount(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void saveUser(Context context) {
        SharedPreferencesUtil
                .getPreferences(context, SharedPreferencesUtil.PrefKey.Account)
                .edit()
                .putString("id", id)
                .putString("username", username)
                .putString("password", password)
                .commit();
    }
}
