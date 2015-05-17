package com.zeroone_creative.basicapplication.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends BaseToolbarActicvity {

    @Extra("image_object_id")
    public String imageId;
    private ImageParseObject mImageParseObject;

    @ViewById(R.id.comment_imageview)
    ImageView mImageView;
    @ViewById(R.id.comment_edittext_answer)
    EditText mAnswerEditText;

    @AfterViews
    void onAfterViews() {
        setToolbarTitle(R.string.title_activity_commnet);
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
    }


    @Click(R.id.comment_button_ok)
    void clickOk() {
        CommentAnswerActivity_.intent(this).imageId(imageId).sentenceId(mImageParseObject.getSentenceId()).answerText(mAnswerEditText.getText().toString()).start();
        finish();
    }


}
