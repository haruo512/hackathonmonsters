package com.zeroone_creative.basicapplication.view.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zeroone_creative.basicapplication.R;

/**
 * Created by shunhosaka on 15/05/17.
 */
public class BaseToolbarActicvity extends ActionBarActivity {

    Toolbar mToolbar;
    TextView mTitleTextView;

    public void setToolbarTitle(int titleId) {
        mToolbar = (Toolbar) findViewById(R.id.header_toolbar);
        mTitleTextView = (TextView) mToolbar.findViewById(R.id.header_textview_title);
        mTitleTextView.setText(titleId);
    }

}
