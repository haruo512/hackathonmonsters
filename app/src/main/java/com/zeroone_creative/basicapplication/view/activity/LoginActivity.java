package com.zeroone_creative.basicapplication.view.activity;

import android.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.enumerate.LoginPage;
import com.zeroone_creative.basicapplication.model.system.UserAccount;
import com.zeroone_creative.basicapplication.view.LoginFragmentCallbackListener;
import com.zeroone_creative.basicapplication.view.fragment.LoginNameFragment;
import com.zeroone_creative.basicapplication.view.fragment.LoginNameFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.NoTitle;
import org.androidannotations.annotations.ViewById;

import java.text.ParseException;

@EActivity(R.layout.activity_login)
public class LoginActivity extends ActionBarActivity implements LoginFragmentCallbackListener, SignUpCallback, LogInCallback {

    @ViewById(R.id.login_swiperefreshlayout)
    SwipeRefreshLayout mProgressLayout;
    private String mNameData;

    @AfterViews
    void onAfterViews() {
        mProgressLayout.setEnabled(false);
        mProgressLayout.setRefreshing(false);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.login_container_layout, LoginNameFragment_.builder().build()).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressLayout.isRefreshing()) {
            mProgressLayout.setRefreshing(false);
        }
    }

    @Override
    public void onNextStepListener(String data, LoginPage page) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (page) {
            //TODO Framgmenの切り替え
            case NickName:
                if (data != null) {
                    mNameData = data;
                    mProgressLayout.setRefreshing(true);
                    ParseUser user = new ParseUser();
                    user.setUsername(data);
                    user.setPassword(data);
                    user.setEmail("example@example.com");
                    user.signUpInBackground(this);
                }
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

    }

    @Override
    public void onBackStepListener(String data, LoginPage page) {

    }

    @Override
    public void done(com.parse.ParseException e) {
        if (e == null) {
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {
                new UserAccount(user.getObjectId(), user.getUsername(), user.getUsername()).saveUser(this);
                PlayActivity_.intent(this).start();
                finish();
            } else {
                ParseUser.logInInBackground(mNameData, mNameData, this);
            }
        } else {
            if (mProgressLayout.isRefreshing()) {
                mProgressLayout.setRefreshing(false);
            }
            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void done(ParseUser parseUser, com.parse.ParseException e) {
        new UserAccount(parseUser.getObjectId(), parseUser.getUsername(), parseUser.getUsername()).saveUser(this);
        PlayActivity_.intent(this).start();
        finish();
    }
}
