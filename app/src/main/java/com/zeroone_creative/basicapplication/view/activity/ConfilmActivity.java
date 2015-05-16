package com.zeroone_creative.basicapplication.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.controller.util.SharedPreferencesUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_confilm)
public class ConfilmActivity extends ActionBarActivity implements SaveCallback {

    @Extra("tag")
    List<String> tags = new ArrayList<>();
    @Extra("sentenceId")
    String sentenceId = "";

    private ImageParseObject mImageParseObject;

    @ViewById(R.id.confilm_textview_question)
    TextView mQuestionTextView;
    @ViewById(R.id.confilm_imageview_center)
    ImageView mConfilmImageView;
    private Bitmap mConfilmImage;

    @AfterInject
    void onAfterInject() {
        if(sentenceId==null) return;
        mImageParseObject = new ImageParseObject();
        mImageParseObject.setUserId(ParseUser.getCurrentUser().getObjectId());
        mImageParseObject.setSentenceId(sentenceId);
        mImageParseObject.setTag(tags);
        mImageParseObject.setBody(SharedPreferencesUtil.getPreferences(getApplicationContext(), SharedPreferencesUtil.PrefKey.Drawing).getString("drawimage", ""));
        if (!mImageParseObject.getBody().isEmpty()) {
            mConfilmImage = ImageUtil.decodeImageBase64(mImageParseObject.getBody());
        }
    }

    @AfterViews
    void onAfterViews() {
        if (mImageParseObject == null) {
            finish();
        }
        mConfilmImageView.setImageBitmap(mConfilmImage);
        ParseQuery<SentenceParseObject> query = ParseQuery.getQuery("Sentence");
        query.getInBackground(sentenceId, new GetCallback<SentenceParseObject>() {
            public void done(SentenceParseObject object, ParseException e) {
                if (e == null) {
                    mQuestionTextView.setText(object.getBody());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click(R.id.confilm_button_send)
    public void clickFinish() {
        mImageParseObject.saveInBackground(this);
    }

    @Override
    public void done(ParseException e) {
        Log.d(ConfilmActivity.class.getSimpleName(), "Send Test");
        if (e == null) {
            //TODO 他の人の投稿一覧にいくようにする
            Intent intent = OtherAnswerActivity_.intent(this).sentenceId(mImageParseObject.getSentenceId()).get();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            e.printStackTrace();
        }
    }
}
