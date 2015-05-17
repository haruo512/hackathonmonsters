package com.zeroone_creative.basicapplication.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.view.adapter.AnswerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_answer_list)
public class AnswerListActivity extends BaseToolbarActicvity implements AdapterView.OnItemClickListener {

    public static final int PAGE_SENTENCE = 1;
    public static final int PAGE_GALLERY = 2;
    public static final int PAGE_ALL = 3;

    @Extra("page_type")
    int pageType = PAGE_GALLERY;
    @Extra("sentenceId")
    String sentenceId = "";
    @ViewById(R.id.other_answer_gridview)
    GridView mGridView;
    @ViewById(R.id.other_answer_layout_question)
    LinearLayout mQuestionLayout;
    @ViewById(R.id.other_answer_textview_question)
    TextView mQuestionTextView;

    private AnswerAdapter mAnswerAdapter;

    @AfterViews
    void onAfterViews() {
        mAnswerAdapter = new AnswerAdapter(getApplicationContext());
        mGridView.setAdapter(mAnswerAdapter);
        ParseQuery<ImageParseObject> query = ParseQuery.getQuery("Image");
        switch (pageType) {
            case PAGE_SENTENCE:
                mQuestionLayout.setVisibility(View.VISIBLE);
                query.whereEqualTo("sentenceId", sentenceId);
                query.setLimit(6);
                setToolbarTitle(R.string.title_activity_other_answer);
                ParseQuery<SentenceParseObject> sentenceQuery = ParseQuery.getQuery("Sentence");
                sentenceQuery.getInBackground(sentenceId, new GetCallback<SentenceParseObject>() {
                    public void done(SentenceParseObject object, ParseException e) {
                        if (e == null) {
                            mQuestionTextView.setText(object.getBody());
                        } else {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case PAGE_GALLERY:
                mQuestionLayout.setVisibility(View.GONE);
                ParseUser parseUser = ParseUser.getCurrentUser();
                query.whereEqualTo("userId", parseUser.getObjectId());
                setToolbarTitle(R.string.title_activity_history);
                break;
            case PAGE_ALL:
                mQuestionLayout.setVisibility(View.GONE);
                query.setLimit(6);
                setToolbarTitle(R.string.title_activity_gallery_all);
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

        mGridView.setOnItemClickListener(this);
    }

    @Click(R.id.other_answer_button_ok)
    void clickSend() {
        Intent intent = TopActivity_.intent(this).get();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ImageParseObject imageParseObject = mAnswerAdapter.getItem(position);
        CommentActivity_.intent(this).imageId(imageParseObject.getObjectId()).start();
    }
}
