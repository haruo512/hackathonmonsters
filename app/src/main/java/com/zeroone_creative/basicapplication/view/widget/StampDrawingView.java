package com.zeroone_creative.basicapplication.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.zeroone_creative.basicapplication.R;
import com.zeroone_creative.basicapplication.model.system.AppConfig;
import com.zeroone_creative.basicapplication.model.viewobject.Deco;

import java.util.ArrayList;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class StampDrawingView extends View {

    private Paint mFramePaint;
    private Bitmap mOpenBitmap;
    private Bitmap mDeleteBitmap;
    private int mTouchType = Deco.TYPE_NONE;

    private ArrayList<Deco> mDecos = new ArrayList<>();
    //タッチしているオブジェクトのindex.
    private int mFocusObjectIndex = -1;

    //Points
    private Point mLastPoint = null;

    public StampDrawingView(Context context) {
        super(context);
        initialize();
    }

    public StampDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    /**
     * Initialize to Frame Paint
     */
    private void initialize() {
        Resources resources = getResources();
        mFramePaint = new Paint();
        mFramePaint.setStrokeWidth(resources.getDimensionPixelSize(R.dimen.drawing_view_frame_width));
        mFramePaint.setColor(Color.parseColor("#dcdddd"));

        mOpenBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_open);
        mDeleteBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_delete);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Deco deco : mDecos) {
            deco.draw(canvas);
        }
        if (mFocusObjectIndex >= 0 && mFocusObjectIndex < mDecos.size()) {
            mDecos.get(mFocusObjectIndex).drawFrame(canvas, mDeleteBitmap, mOpenBitmap, mFramePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFocusable()) return false;
        Point touchPoint = new Point((int) event.getX(), (int) event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //ボタンを押したとき
                judgeTouchPoint(touchPoint);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchType == Deco.TYPE_TAP || mTouchType == Deco.TYPE_MOVE) {
                    mTouchType = Deco.TYPE_MOVE;
                    //移動処理
                    try {
                        Deco focusDeco = mDecos.get(mFocusObjectIndex);
                        //差分から移動を行う
                        focusDeco.x += (touchPoint.x - mLastPoint.x);
                        focusDeco.y += (touchPoint.y - mLastPoint.y);
                        mDecos.set(mFocusObjectIndex, focusDeco);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                } else if (mTouchType == Deco.TYPE_CHANGE) {
                    try {
                        Deco focusDeco = mDecos.get(mFocusObjectIndex);
                        focusDeco.setChange(touchPoint);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //Log.d("CakeDecoView", "judgeTouchPoint touchType:"+Integer.toString(mTouchType));
                if (mTouchType == Deco.TYPE_DELETE) {
                    try {
                        Deco focusDeco = mDecos.get(mFocusObjectIndex);
                        if (focusDeco.getTouchType(touchPoint) == Deco.TYPE_DELETE) {
                            mDecos.remove(mFocusObjectIndex);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
                //タッチのタイプを初期化する
                mTouchType = Deco.TYPE_NONE;
                break;
        }
        mLastPoint = touchPoint;
        //画面の更新
        invalidate();
        return true;
    }

    /**
     * Add new Deco Item
     *
     * @param bitmap
     */
    public void addDecoItem(Bitmap bitmap, int type) {
        float scale = 1.0f;
        if (Math.max(bitmap.getWidth(), bitmap.getHeight()) > 0) {
            scale = (float) Math.max(getWidth() / 2, getHeight() / 2) / (float) Math.max(bitmap.getWidth(), bitmap.getHeight());
        }
        Log.d(getClass().getSimpleName(), "AddDecoItem" + Float.toString(scale));
        this.addDecoItem(new Deco(bitmap, getWidth() / 2, getHeight() / 2, bitmap.getWidth() * scale, bitmap.getHeight() * scale, 0, type));
    }

    /**
     * Add new Deco Item
     *
     * @param deco
     */
    public void addDecoItem(Deco deco) {
        if (mDecos != null) {
            mDecos.add(deco);
        }
        //Update
        invalidate();
    }

    /**
     * Set mTouchType and mFocusObjectIndex
     *
     * @param touch
     */
    private void judgeTouchPoint(Point touch) {
        //タッチ状態の初期化
        mTouchType = Deco.TYPE_NONE;
        //オブジェクトのチェック
        for (int i = mDecos.size() - 1; i >= 0; i--) {
            int touchType = mDecos.get(i).getTouchType(touch);
            if (AppConfig.DEBUG) {
                Log.d("CakeDecoView", "judgeTouchPoint touchType:" + Integer.toString(touchType));
            }
            //タップしていた場合
            if (touchType != Deco.TYPE_NONE) {
                if (mFocusObjectIndex == i) {
                    //そのオブジェクトにすでにフォーカスがあたっていた場合。
                    mTouchType = touchType;
                } else {
                    //フォーカスがあたっていなかった場合はムーブ
                    mTouchType = Deco.TYPE_MOVE;
                }
                mFocusObjectIndex = i;
                //処理終了
                return;
            } else if (mFocusObjectIndex == i) {
                //フォーカスを解除する
                mFocusObjectIndex = -1;
            }
        }
    }

    public void removeFrame() {
        mFocusObjectIndex = -1;
        invalidate();
    }

    public int getDecosCount() {
        return mDecos.size();
    }

    /**
     * 表示の初期化処理
     */
    public void clearCanvas() {
        for (int i = 0; i < mDecos.size(); i++) {
            mDecos.remove(i);
        }
        invalidate();
    }

}
