package com.zeroone_creative.basicapplication.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.view.activity.AnswerListActivity;
import com.zeroone_creative.basicapplication.view.activity.AnswerListActivity_;
import com.zeroone_creative.basicapplication.view.activity.PlayActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EFragment(R.layout.fragment_pager_top)
public class TopPagerFragment extends Fragment {

    @FragmentArg("langage_code")
    String mLangageCode = "en";
    @FragmentArg("langage_name")
    String mLangageName = "";

    private List<SentenceParseObject> mSententceList = new ArrayList<>();

    @AfterViews
    public void onAfterViews() {
        ParseQuery<SentenceParseObject> query = ParseQuery.getQuery("Sentence");
        query.whereContains("lang", mLangageCode);
        //TODO　変えるかも
        query.setLimit(10);
        query.findInBackground(new FindCallback<SentenceParseObject>() {
            public void done(List<SentenceParseObject> sententceList, ParseException e) {
                if (e == null) {
                    mSententceList.addAll(sententceList);
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Click(R.id.top_button_start)
    public void clickStart() {
        if (mSententceList.size() > 0) {
            Log.d("Sentence", mSententceList.get(0).toString());
            Collections.shuffle(mSententceList);
            PlayActivity_.intent(getActivity()).sentenceId(mSententceList.get(0).getObjectId()).start();
        }
    }

    @Click(R.id.top_button_gallery)
    public void clickGallery() {
        AnswerListActivity_
                .intent(this)
                .pageType(AnswerListActivity.PAGE_GALLERY)
                .start();
    }

    @Click(R.id.top_button_all)
    public void clickAllImage() {
        AnswerListActivity_
                .intent(this)
                .pageType(AnswerListActivity.PAGE_ALL)
                .start();
    }

}