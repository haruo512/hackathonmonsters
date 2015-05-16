package com.zeroone_creative.basicapplication.view.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.controller.util.ImageUtil;
import com.zeroone_creative.basicapplication.controller.util.SharedPreferencesUtil;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.model.system.AppConfig;
import com.zeroone_creative.basicapplication.model.viewobject.Deco;
import com.zeroone_creative.basicapplication.view.fragment.MessageDialogFragment;
import com.zeroone_creative.basicapplication.view.widget.PenDrawingView;
import com.zeroone_creative.basicapplication.view.widget.StampDrawingView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_play)
public class PlayActivity extends ActionBarActivity {

    @ViewById(R.id.play_layout_drawing)
    FrameLayout mDrawingLayout;
    @ViewById(R.id.play_pen_drawingview)
    PenDrawingView mPenDrawingView;
    @ViewById(R.id.play_stamp_drawingview)
    StampDrawingView mStampDrawingView;
    @ViewById(R.id.play_textview_question)
    TextView mQuestionTextView;
    @ViewById(R.id.play_swiperefreshlayout)
    SwipeRefreshLayout mProgressLayout;

    @Extra("sentenceId")
    String sentenceId = "";
    private SentenceParseObject mSentenceParseObject;

    @AfterInject
    public void onAfterInject() {

    }


    @AfterViews
    void onAfterViews() {
        mProgressLayout.setRefreshing(false);
        mProgressLayout.setEnabled(false);

        WindowManager windowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Point displaySize = new Point();
        windowManager.getDefaultDisplay().getSize(displaySize);
        ViewGroup.LayoutParams layoutParams = mDrawingLayout.getLayoutParams();
        layoutParams.width = Math.min(displaySize.x, displaySize.y);
        layoutParams.height = Math.min(displaySize.x, displaySize.y);
        mDrawingLayout.setLayoutParams(layoutParams);

        mStampDrawingView.setFocusable(false);
        mPenDrawingView.setFocusable(false);

        ParseQuery<SentenceParseObject> query = ParseQuery.getQuery("Sentence");
        query.getInBackground(sentenceId, new GetCallback<SentenceParseObject>() {
            public void done(SentenceParseObject object, ParseException e) {
                if (e == null) {
                    mSentenceParseObject = object;
                    mQuestionTextView.setText(mSentenceParseObject.getBody());
                } else {
                    e.printStackTrace();
                }
            }
        });

        Picasso.with(getApplicationContext()).load("https://dl.dropboxusercontent.com/u/31455721/mother.jpg").into(mAddHintTarget);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressLayout.isRefreshing()) mProgressLayout.setRefreshing(false);
    }

    @Click(R.id.play_button_finish)
    void clickPreview() {
       /*
        if (mStampDrawingView.getDecosCount() < 1) {
            if (getFragmentManager().findFragmentByTag(MessageDialogFragment.class.getSimpleName()) == null) {
                MessageDialogFragment messageDialogFragment = MessageDialogFragment.newInstance(getString(R.string.make_attention_title), getString(R.string.make_attention_message));
                messageDialogFragment.show(getFragmentManager(), MessageDialogFragment.class.getSimpleName());
            }
            //キャンセル
            return;
        }
        */
        if (!mProgressLayout.isRefreshing()) mProgressLayout.setRefreshing(true);

        mStampDrawingView.removeFrame();
        //背景を白く設定
        mStampDrawingView.setBackgroundColor(Color.WHITE);
        mPenDrawingView.setBackgroundColor(Color.TRANSPARENT);
        mDrawingLayout.setDrawingCacheEnabled(false);
        mDrawingLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = transformImage(Bitmap.createBitmap(mDrawingLayout.getDrawingCache()));
        bitmap = ImageUtil.resize(bitmap, 600, 600);
        SharedPreferences.Editor editor = SharedPreferencesUtil.getPreferences(getApplicationContext(), SharedPreferencesUtil.PrefKey.Drawing).edit();
        editor.putString("drawimage", ImageUtil.encodeImageBase64(bitmap));
        editor.commit();
        ConfilmActivity_.intent(this).start();
    }

    private Bitmap transformImage(Bitmap source) {
        int size = (int) (Math.min(source.getWidth(), source.getHeight()) * 27.0f / 32.0f);
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    private Target mAddHintTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mStampDrawingView.setFocusable(true);
            //画像を読み込み完了したので追加
            mStampDrawingView.addDecoItem(bitmap, Deco.DATA_TYPE_STAMP);
        }
        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(getApplicationContext(), getString(R.string.play_toast_faild_stamp_get), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

}

