package com.zeroone_creative.basicapplication.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.enumerate.LoginPage;
import com.zeroone_creative.basicapplication.view.LoginFragmentCallbackListener;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends ActionBarActivity implements LoginFragmentCallbackListener {

    @Override
    public void onNextStepListener(String data, LoginPage page) {
        switch (page) {
            case NickName:
                break;
            case Age:
                break;
            case Country:
                break;
            case Start:
                break;
            default:
                break;
        }


        if (data != null) {

        }
    }

    @Override
    public void onBackStepListener(String data, LoginPage page) {

    }
}
