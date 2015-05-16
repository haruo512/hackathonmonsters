package com.zeroone_creative.basicapplication.model.viewobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import com.zeroone_creative.basicapplication.model.system.AppConfig;

/**
 * Created by shunhosaka on 15/05/16.
 */
public class Deco {
    public static final float MENU_BUTTON_SIZE = 100.0f;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_TAP = 1;
    public static final int TYPE_MOVE = 2;
    public static final int TYPE_CHANGE = 3;
    public static final int TYPE_DELETE = 4;

    public static final int DATA_TYPE_STAMP = 1;

    private Paint paint = null;
    public float x, y;
    public float width, height;
    public int rotation;
    public Bitmap bitmap;
    public int type = DATA_TYPE_STAMP;

    public Deco(Bitmap bitmap, float x, float y, float width, float height, int rotation, int type) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
        this.type = type;
        if(AppConfig.DEBUG) {
            Log.d("Deco", "Constructor Value:" + "(x:" + Float.toString(x) + ", y:" + Float.toString(y) + ")");
            Log.d("Deco", "Constructor Value:" + "(width:" + Float.toString(width) + ", height:" + Float.toString(height) + ")");
            Log.d("Deco", "Constructor Value:" + "(rotation:" + Integer.toString(rotation) + ")");
        }
    }

    /**
     * Draw this object
     * @param canvas
     */
    public void draw(Canvas canvas) {
        if (this.width == 0 || this.height == 0) {
            this.width = bitmap.getWidth() / 2;
            this.height = bitmap.getHeight() / 2;
        }
        canvas.drawBitmap(bitmap, getMatrix(), paint);
    }

    /**
     * ビューの周りの灰色のフレームを描画するクラス
     */
    public void drawFrame(Canvas canvas, Bitmap leftUpBitmap, Bitmap rightUpBitmap, Paint framePaint) {
        Point[] points = getRectPoints();
        for (int i = 0; i < points.length; i++) {
            canvas.drawLine(points[i].x, points[i].y, points[(i + 1) % points.length].x, points[(i + 1) % points.length].y, framePaint);
        }
        float halfImageSize = Deco.MENU_BUTTON_SIZE / 2;
        Paint paint = new Paint();
        Matrix deleteMatrix = new Matrix();
        deleteMatrix.setScale(Deco.MENU_BUTTON_SIZE / leftUpBitmap.getWidth(), Deco.MENU_BUTTON_SIZE / leftUpBitmap.getHeight());
        deleteMatrix.postRotate(rotation, halfImageSize, halfImageSize);
        deleteMatrix.postTranslate(points[0].x - halfImageSize, points[0].y - halfImageSize);
        canvas.drawBitmap(leftUpBitmap, deleteMatrix, paint);

        Matrix openMatrix = new Matrix();
        openMatrix.setScale(Deco.MENU_BUTTON_SIZE / rightUpBitmap.getWidth(), Deco.MENU_BUTTON_SIZE / rightUpBitmap.getHeight());
        openMatrix.postRotate(rotation, halfImageSize, halfImageSize);
        openMatrix.postTranslate(points[1].x - halfImageSize, points[1].y - halfImageSize);
        canvas.drawBitmap(rightUpBitmap, openMatrix, paint);
    }

    /**
     * @return Matrix
     */
    private Matrix getMatrix() {
        Matrix matrix = new Matrix();
        matrix.setScale(width / bitmap.getWidth(), height / bitmap.getHeight());
        matrix.postRotate(rotation, width / 2, height / 2);
        matrix.postTranslate(x - width / 2, y - height / 2);
        return  matrix;
    }

    /**
     * タッチしているかを返す。
     * @param touch is touch CakeDecoView point
     * @return
     */
    public int getTouchType(Point touch) {
        Point[] points = getRectPoints();
        int halfImageSize = (int) (MENU_BUTTON_SIZE / 2);
        if (Math.sqrt((touch.x - points[0].x) * (touch.x - points[0].x) + (touch.y - points[0].y) * (touch.y - points[0].y)) < halfImageSize) {
            //points[0] -> delete
            return TYPE_DELETE;
        } else if (Math.sqrt((touch.x - points[1].x) * (touch.x - points[1].x) + (touch.y - points[1].y) * (touch.y - points[1].y)) < halfImageSize) {
            //points[1] -> change
            return TYPE_CHANGE;
        } else if(crossingNumber(points, touch)) {
            return TYPE_TAP;
        } else {
            return TYPE_NONE;
        }
    }

    public Point[] getRectPoints() {
        //ラジアン角に変換
        Double radian = rotation* Math.PI / 180;
        double coordinateX1 = width / 2 * Math.cos(radian);
        double coordinateX2 = height / 2 * Math.sin(radian);
        double coordinateY1 = width / 2 * Math.sin(radian);
        double coordinateY2 = height / 2 * Math.cos(radian);
        //頂点の角ポイント
        Point[] points = new Point[4];
        points[0] = new Point((int) (-coordinateX1 + coordinateX2 + x), (int) (-coordinateY1 - coordinateY2 + y));
        points[1] = new Point((int) (coordinateX1 + coordinateX2 + x), (int) (coordinateY1 - coordinateY2 + y));
        points[2] = new Point((int) (coordinateX1 - coordinateX2 + x), (int) (coordinateY1 + coordinateY2 + y));
        points[3] = new Point((int) (-coordinateX1 - coordinateX2 + x), (int) (-coordinateY1 + coordinateY2 + y));
        if(AppConfig.DEBUG) {
            for(Point point : points) {
                Log.d("getRectPoints","Point: "+point.toString());
            }
        }
        return points;
    }

    /**
     * Set new object size and rotation
     * @param touch (touch is RightTop point new Object size)
     * TODO
     */
    public void setChange(Point touch) {
        //拡張点のポイントを求める
        Point[] points = getRectPoints();
        Double radian = Math.toRadians(rotation);
        //最初の角度
        double firstRotaition = getRadian(x, y, points[1].x, points[1].y) * 180d / Math.PI;
        //新しい角度
        double newRotaition = getRadian(x, y, (double) touch.x, (double) touch.y)  * 180d / Math.PI;
        //差分を以前の角度に足す
        rotation -= (int) (newRotaition - firstRotaition);

        if(AppConfig.DEBUG) {
            Log.d("Deco", "Deco rotation:" + Integer.toString(rotation));
        }
        // 大きさ
        double currentLength = Math.sqrt(width * width + height * height) / 2;
        double newLength = Math.sqrt((touch.x - x) * (touch.x - x) + (touch.y - y) * (touch.y - y));
        double scale = newLength / currentLength;
        width = width * (float)scale;
        height = height * (float)scale;
    }

    protected double getRadian(double x, double y, double x2, double y2) {
        double radian = Math.atan2(x2 - x, y2 - y);
        return radian;
    }

    /**
     * Crossing Number Algorithm
     * @param polygon
     * @param touch
     * @return
     */
    boolean crossingNumber(Point[] polygon, Point touch) {
        int cnt = 0;
        for (int i = 0; i < 4; ++i) {
            final int x1 = polygon[(i + 1) % 4].x - polygon[i].x;
            final int y1 = polygon[(i + 1) % 4].y - polygon[i].y;
            final int x2 = touch.x - polygon[i].x;
            final int y2 = touch.y - polygon[i].y;
            if (x1 * y2 - x2 * y1 < 0) {
                ++cnt;
            } else {
                --cnt;
            }
        }
        if(AppConfig.DEBUG) {
            Log.d("Deco","Touch Point: "+touch.toString());
            Log.d("Deco", "CrossingNumber CN:" + Integer.toString(cnt));
        }
        return cnt == 4 || cnt == -4;
    }
}
