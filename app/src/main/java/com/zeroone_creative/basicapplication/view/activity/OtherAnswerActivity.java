package com.zeroone_creative.basicapplication.view.activity;

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
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.ImageParseObject;
import com.zeroone_creative.basicapplication.view.adapter.AnswerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_other_answer)
public class OtherAnswerActivity extends ActionBarActivity {

    @ViewById(R.id.other_answer_gridview)
    GridView mGridView;

    private AnswerAdapter mAnswerAdapter;

    @AfterViews
    void onAfterViews() {
        mAnswerAdapter = new AnswerAdapter(getApplicationContext());
        mGridView.setAdapter(mAnswerAdapter);

        ParseQuery<ImageParseObject> query = ParseQuery.getQuery("Image");
        query.setLimit(30);
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

}
