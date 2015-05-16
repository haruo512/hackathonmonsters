package com.zeroone_creative.basicapplication;

import android.app.Application;

import com.parse.Parse;
import com.zeroone_creative.basicapplication.model.system.AppConfig;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class LangagExpress extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Parseの初期化
        Parse.initialize(this, AppConfig.APPLICATION_ID, AppConfig.CLIENT_KEY);
        //TODO 必要に応じて初期化
    }
}
