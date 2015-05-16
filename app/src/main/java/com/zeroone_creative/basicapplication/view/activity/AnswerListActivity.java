package com.zeroone_creative.basicapplication.view.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.view.adapter.AnswerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_answer_list)
public class AnswerListActivity extends ActionBarActivity {

    public static final int PAGE_SENTENCE = 1;
    public static final int PAGE_GALLERY = 2;
    public static final int PAGE_ALL = 3;

    @Extra("page_type")
    int pageType = PAGE_GALLERY;
    @Extra("sentenceId")
    String sentenceId = "";
    @ViewById(R.id.other_answer_gridview)
    GridView mGridView;
    private AnswerAdapter mAnswerAdapter;

    @AfterViews
    void onAfterViews() {
        mAnswerAdapter = new AnswerAdapter(getApplicationContext());
        mGridView.setAdapter(mAnswerAdapter);
        ParseQuery<ImageParseObject> query = ParseQuery.getQuery("Image");
        switch (pageType) {
            case PAGE_SENTENCE:
                query.whereEqualTo("sentenceId", sentenceId);
                query.setLimit(30);
                break;
            case PAGE_GALLERY:
                ParseUser parseUser = ParseUser.getCurrentUser();
                query.whereEqualTo("userId", parseUser.getObjectId());
                break;
            case PAGE_ALL:
                query.setLimit(30);
                break;
            default:
                break;
        }
        query.findInBackground(new FindCallback<ImageParseObject>() {
            public void done(List<ImageParseObject> imageList, ParseException e) {
                if (e == null) {
                    mAnswerAdapter.setContent(imageList);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click(R.id.other_answer_button_send)
    void clickSend() {
        Intent intent = TopActivity_.intent(this).get();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}
