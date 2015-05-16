package com.zeroone_creative.basicapplication.view.fragment;

import android.app.Fragment;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.view.activity.PlayActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.fragment_pager_top)
public class TopPagerFragment extends Fragment {

    @FragmentArg("langage_code")
    String mLangageCode = "";
    @FragmentArg("langage_name")
    String mLangageName = "";

    @ViewById(R.id.top_textview_language)
    TextView mLangNameTextView;

    private List<SentenceParseObject> mSententceList = new ArrayList<>();

    @AfterViews
    public void onAfterViews() {
        mLangNameTextView.setText(mLangageName);
        ParseQuery<SentenceParseObject> query = ParseQuery.getQuery("Sentence");
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
            PlayActivity_.intent(getActivity()).sentenceId(mSententceList.get(0).getId()).start();
        }
    }

}