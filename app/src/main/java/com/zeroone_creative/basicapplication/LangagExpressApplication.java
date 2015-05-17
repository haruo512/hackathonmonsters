package com.zeroone_creative.basicapplication;

import android.app.Application;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.zeroone_creative.basicapplication.controller.util.FontUtil;
import com.zeroone_creative.basicapplication.model.parseobject.CommentParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.PartsParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.model.system.AppConfig;
import com.zeroone_creative.basicapplication.model.system.UserAccount;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class LangagExpressApplication extends Application implements LogInCallback {

    @Override
    public void onCreate() {
        super.onCreate();
        FontUtil.getFont("font/A-OTF-UDShinMGoPro-Bold.otf.zip", getApplicationContext());
        //Parseの初期化
        Parse.initialize(this, AppConfig.APPLICATION_ID, AppConfig.CLIENT_KEY);
        UserAccount account = new UserAccount(getApplicationContext());
        ParseUser.logInInBackground(account.username, account.password, this);
        ParseObject.registerSubclass(ImageParseObject.class);
        ParseObject.registerSubclass(CommentParseObject.class);
        ParseObject.registerSubclass(PartsParseObject.class);
        ParseObject.registerSubclass(SentenceParseObject.class);
    }

    @Override
    public void done(ParseUser parseUser, ParseException e) {

    }
}
