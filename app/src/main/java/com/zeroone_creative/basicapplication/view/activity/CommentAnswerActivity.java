package com.zeroone_creative.basicapplication.view.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_comment_answer)
public class CommentAnswerActivity extends ActionBarActivity {

    @Extra("image_object_id")
    String imageId;
    @Extra("sentence_object_id")
    String sentenceId;
    private ImageParseObject mImageParseObject;
    @Extra("text_answer")
    String answerText;

    @ViewById(R.id.comment_imageview)
    ImageView mImageView;

    @ViewById(R.id.comment_answer_textview_orizinal)
    TextView mOrizinalTextView;
    @ViewById(R.id.comment_answer_textview_answer)
    TextView mAnswerTextView;

    @AfterViews
    void onAfterViews() {
        ParseQuery<ImageParseObject> query = ParseQuery.getQuery("Image");
        query.getInBackground(imageId, new GetCallback<ImageParseObject>() {
            public void done(ImageParseObject object, ParseException e) {
                if (e == null) {
                    mImageParseObject = object;
                    mImageView.setImageBitmap(ImageUtil.decodeImageBase64(mImageParseObject.getBody()));
                } else {
                    e.printStackTrace();
                }
            }
        });
        ParseQuery<SentenceParseObject> querySentence = ParseQuery.getQuery("Sentence");
        querySentence.getInBackground(sentenceId, new GetCallback<SentenceParseObject>() {
            public void done(SentenceParseObject object, ParseException e) {
                if (e == null) {
                    mOrizinalTextView.setText(object.getBody());
                } else {
                    e.printStackTrace();
                }
            }
        });
        mAnswerTextView.setText(answerText);
    }

    @Click(R.id.comment_button_ok)
    void clickOk() {
        TopActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
    }



}
