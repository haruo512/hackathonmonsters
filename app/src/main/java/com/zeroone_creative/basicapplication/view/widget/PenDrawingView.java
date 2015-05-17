package com.zeroone_creative.basicapplication.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.zeroone_creative.basicapplication.R;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class PenDrawingView extends ImageView {

    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBitmap;
    private float x1, y1;
    private int mPenColor = Color.BLACK;

    public PenDrawingView(Context context) {
        super(context);
        initialize();
    }

    public PenDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        createdInitialize();
    }

    /**
     * Initialize to Frame Paint
     */
    private void initialize() {
        Resources resources = getResources();
        //android.graphics.Paintクラスのインタンスを生成
        mPaint = new Paint();
        //グラフィックのスタイルを設定
        mPaint.setStyle(Paint.Style.STROKE);
        //線と線のつなぎ目を丸く設定
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        //線の端を丸くC設定
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        setPenPaint();
        //Pathのインスタンスを生成
        mPath = new Path();
    }



    private void createdInitialize() {
        if (mBitmap == null) {
            //任意の大きさで新規のビットマップを作成する。指定の配列をフレームバッファ(ピクセル列データ)として読み込んでビットマップを作成する、 pngやjpgなどの画像ファイルのデータから描画用画像を生成する、などいくつかの機能がオーバーロードされています
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            //新しいCanvasに、その保存先のBitmapをセット
            mCanvas = new Canvas(mBitmap);
            //画像を白く塗りつぶす
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            //ImageViewにビットマップを表示
            setImageBitmap(mBitmap);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isFocusable()) return false;
        //タッチされた場所を取得する
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            //指が画面に触れた時のイベント
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.quadTo(x1, y1, x, y);
                mCanvas.drawPath(mPath, mPaint);
                break;
            case MotionEvent.ACTION_UP:
                mPath.quadTo(x1, y1, x, y);
                mCanvas.drawPath(mPath, mPaint);
                break;
        }
        //触れられた場所を記憶しておくよ
        x1 = x;
        y1 = y;
        setImageBitmap(mBitmap);
        return true;
    }

    public void clearCanvas() {
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        setImageBitmap(mBitmap);
    }

    public void setPenColor(int color) {
        mPenColor = color;
        setPenPaint();
    }

    public void setPenPaint() {
        //線の幅を設定
        mPaint.setStrokeWidth(getResources().getDimension(R.dimen.play_drawing_pen_width));
        mPaint.setColor(mPenColor);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
    }

    public void setEraserPaint() {
        mPenColor = mPaint.getColor();
        //TODO
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

}
