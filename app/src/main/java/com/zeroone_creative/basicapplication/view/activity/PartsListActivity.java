package com.zeroone_creative.basicapplication.view.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.model.parseobject.PartsParseObject;
import com.zeroone_creative.basicapplication.view.adapter.AnswerAdapter;
import com.zeroone_creative.basicapplication.view.adapter.PartsAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_parts_list)
public class PartsListActivity extends BaseToolbarActicvity {

    @Extra("tag_name")
    String tagName = "";

    @ViewById(R.id.parts_list_textview_notfound)
    TextView mNotfoundTextView;
    @ViewById(R.id.parts_list_gridview)
    GridView mGridView;

    PartsAdapter mPartsAdapter;

    @AfterViews
    void onAfterViews() {
        if (tagName.isEmpty()) {
            setResult(RESULT_CANCELED);
            finish();
        }

        setToolbarTitle(R.string.title_activity_parts_list);
        mToolbar.setNavigationIcon(R.drawable.ic_ab_close);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mPartsAdapter = new PartsAdapter(getApplicationContext());
        mGridView.setAdapter(mPartsAdapter);
        ParseQuery<PartsParseObject> query = ParseQuery.getQuery("Parts");
        query.whereEqualTo("tag", tagName);
        query.setLimit(30);
        query.findInBackground(new FindCallback<PartsParseObject>() {
            public void done(List<PartsParseObject> partsist, ParseException e) {
                if (e == null) {
                    mPartsAdapter.setContent(partsist);
                    if (partsist.size() < 1) {
                        mNotfoundTextView.setVisibility(View.VISIBLE);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PartsParseObject partsParseObject = (PartsParseObject) mPartsAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("objectId", partsParseObject.getObjectId());
                intent.putExtra("url", partsParseObject.getUrl());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

}
