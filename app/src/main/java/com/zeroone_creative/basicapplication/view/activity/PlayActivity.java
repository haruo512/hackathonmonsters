package com.zeroone_creative.basicapplication.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.zeroone_creative.basicapplication.model.enumerate.Language;
import com.zeroone_creative.basicapplication.model.enumerate.PalleteColor;
import com.zeroone_creative.basicapplication.model.parseobject.SentenceParseObject;
import com.zeroone_creative.basicapplication.model.system.AppConfig;
import com.zeroone_creative.basicapplication.model.viewobject.Deco;
import com.zeroone_creative.basicapplication.view.fragment.MessageDialogFragment;
import com.zeroone_creative.basicapplication.view.widget.PenDrawingView;
import com.zeroone_creative.basicapplication.view.widget.StampDrawingView;
import com.zeroone_creative.basicapplication.view.widget.TextLinker;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

@EActivity(R.layout.activity_play)
public class PlayActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    private final static int PART_LIST_RESULT_CODE = 100;

    @Extra("sentenceId")
    String sentenceId = "";

    @ViewById(R.id.play_layout_color_selector)
    LinearLayout mColorSelectorLayout;
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

    private SentenceParseObject mSentenceParseObject;
    private TextToSpeech mTextToSpeech;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextToSpeech = new TextToSpeech(this, this);
        mContext = this;
    }

    @AfterViews
    void onAfterViews() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        for (PalleteColor color : PalleteColor.values()) {
            mColorSelectorLayout.addView(getColorItemImageView(inflater, color));
        }
        mProgressLayout.setRefreshing(false);
        mProgressLayout.setEnabled(false);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // ディスプレイのインスタンス生成
        Point displaySize = new Point();
        windowManager.getDefaultDisplay().getSize(displaySize);
        ViewGroup.LayoutParams layoutParams = mDrawingLayout.getLayoutParams();
        int size = (int) (Math.min(displaySize.x, displaySize.y) * 0.9);
        layoutParams.width = size;
        layoutParams.height = size;
        mDrawingLayout.setLayoutParams(layoutParams);
        ParseQuery<SentenceParseObject> query = ParseQuery.getQuery("Sentence");
        query.getInBackground(sentenceId, new GetCallback<SentenceParseObject>() {
            public void done(SentenceParseObject object, ParseException e) {
                if (e == null) {
                    mSentenceParseObject = object;
                    setQuestion();
                } else {
                    e.printStackTrace();
                }
            }
        });
        clickPen();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawingLayout.setBackgroundResource(R.drawable.bg_drawing);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressLayout.isRefreshing()) mProgressLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTextToSpeech != null) {
            // TextToSpeechのリソースを解放する
            mTextToSpeech.shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PART_LIST_RESULT_CODE && resultCode == RESULT_OK) {
            Picasso.with(getApplicationContext()).load(data.getStringExtra("url")).into(mAddHintTarget);
        }
    }

    @Click(R.id.play_imagebutton_pen)
    void clickPen() {
        mStampDrawingView.removeFrame();
        //フォーカスを変える
        mStampDrawingView.setFocusable(false);
        mPenDrawingView.setFocusable(true);
        mPenDrawingView.setPenPaint();
    }

    @Click(R.id.play_imagebutton_move)
    void clickMove() {
        //フォーカスを変える
        mStampDrawingView.setFocusable(true);
        mPenDrawingView.setFocusable(false);
    }

    @Click(R.id.play_imagebutton_eraser)
    void clickEraser() {
        clickPen();
        mPenDrawingView.setEraserPaint();
    }

    @Click(R.id.play_imagebutton_trash)
    void clickTrash() {
        mPenDrawingView.clearCanvas();
        mStampDrawingView.clearCanvas();
        clickPen();
    }

    @Click(R.id.play_button_finish)
    void clickPreview() {
        if (!mProgressLayout.isRefreshing()) mProgressLayout.setRefreshing(true);
        mStampDrawingView.removeFrame();
        //背景を白く設定
        mDrawingLayout.setBackgroundColor(Color.WHITE);
        mDrawingLayout.setDrawingCacheEnabled(false);
        mDrawingLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = transformImage(Bitmap.createBitmap(mDrawingLayout.getDrawingCache()));
        bitmap = ImageUtil.resize(bitmap, 500, 500);
        SharedPreferences.Editor editor = SharedPreferencesUtil.getPreferences(getApplicationContext(), SharedPreferencesUtil.PrefKey.Drawing).edit();
        editor.putString("drawimage", ImageUtil.encodeImageBase64(bitmap));
        editor.commit();
        ConfilmActivity_.intent(this).sentenceId(mSentenceParseObject.getObjectId()).start();
    }

    private void setQuestion() {
        if (mSentenceParseObject == null) return;
        String text = mSentenceParseObject.getBody();

        if (mSentenceParseObject.getLang().equals(Language.English.code)) {
            String[] words = text.split(" ", 0);
            SparseArray<String> links = new SparseArray<>();
            for (int i = 0; i < words.length; i++) {
                links.append(i, words[i]);
            }
            mQuestionTextView.setText(
                    TextLinker.getLinkableText(text, links, new TextLinker.OnLinkClickListener() {
                        @Override
                        public void onLinkClick(int textId, String text) {
                            //TODO パーツの選択に飛ばす
                            //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                            PartsListActivity_.intent(mContext).tagName(text).startForResult(PART_LIST_RESULT_CODE);
                        }
                    })
            );
            mQuestionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }
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
            //画像を読み込み完了したので追加
            mStampDrawingView.addDecoItem(bitmap, Deco.DATA_TYPE_STAMP);
            clickMove();
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Toast.makeText(getApplicationContext(), getString(R.string.play_toast_faild_stamp_get), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    private ImageView getColorItemImageView(LayoutInflater inflater, PalleteColor color) {
        ImageView imageView = (ImageView) inflater.inflate(R.layout.item_color, null);
        imageView.setImageDrawable(PalleteColor.getGradientDrawable(color, getResources()));
        imageView.setTag(color);
        imageView.setOnClickListener(colorImageClickListener);
        imageView.setEnabled(true);
        return imageView;
    }

    private View.OnClickListener colorImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object object = v.getTag();
            if (object instanceof PalleteColor) {
                int color = Color.parseColor(((PalleteColor) object).colorCode);
                mPenDrawingView.setPenColor(color);
                clickPen();
                for (int i = 0; i < mColorSelectorLayout.getChildCount(); i++) {
                    mColorSelectorLayout.getChildAt(i).setEnabled(true);
                }
                v.setEnabled(false);
            }
        }
    };

    @Click(R.id.play_button_speak)
    void speechText() {
        if (mSentenceParseObject != null && mSentenceParseObject.getBody().length() > 0) {
            if (mTextToSpeech.isSpeaking()) {
                // 読み上げ中なら止める
                mTextToSpeech.stop();
            }
            Log.d(PlayActivity.class.getSimpleName(), "Speach Text" + mSentenceParseObject.getBody());
            // 読み上げ開始
            mTextToSpeech.speak(mSentenceParseObject.getBody(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (TextToSpeech.SUCCESS == status) {
            Locale locale = Locale.ENGLISH;
            if (mTextToSpeech.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                mTextToSpeech.setLanguage(locale);
            } else {
                Log.d(PlayActivity.class.getSimpleName(), "Error SetLocale");
            }
        } else {
            Log.d(PlayActivity.class.getSimpleName(), "Error Init");
        }
    }
}

