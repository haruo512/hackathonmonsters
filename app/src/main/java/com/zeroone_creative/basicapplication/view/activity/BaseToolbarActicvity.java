package com.zeroone_creative.basicapplication.view.activity;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.FontUtil;

/**
 * Created by shunhosaka on 15/05/17.
 */
public class BaseToolbarActicvity extends ActionBarActivity {

    Toolbar mToolbar;
    TextView mTitleTextView;

    public void setToolbarTitle(int titleId) {
        mToolbar = (Toolbar) findViewById(R.id.header_toolbar);
        mTitleTextView = (TextView) mToolbar.findViewById(R.id.header_textview_title);
        Typeface typeface = FontUtil.getFont("font/A-OTF-UDShinMGoPro-Bold.otf.zip", getApplicationContext());
        mTitleTextView.setTypeface(typeface);
        mTitleTextView.setText(titleId);
    }
}
