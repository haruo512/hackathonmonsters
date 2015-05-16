package com.zeroone_creative.basicapplication.view.activity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.view.adapter.TopPagerAdapter;
import com.zeroone_creative.basicapplication.view.fragment.TopPagerFragment_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_pager)
public class TopActivity extends Activity {

    TopPagerAdapter mBasicPagerAdapter;

    @ViewById(R.id.pager_viewpager)
    ViewPager mViewPager;

    @AfterViews
    void onAfterViews() {
        setPager();
    }

    void setPager() {
        mBasicPagerAdapter = new TopPagerAdapter(getFragmentManager());
        mBasicPagerAdapter.addPages(TopPagerFragment_.builder().mSectionNumber(1).build());
        mViewPager.setAdapter(mBasicPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
