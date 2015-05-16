package com.zeroone_creative.basicapplication.model.enumerate;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.zeroone_creative.basicapplication.R;

/**
 * Created by shunhosaka on 15/05/17.
 */
public enum PalleteColor {
    RED("#F44336"),
    PINK("#FF4081"),
    PURPLE("#9C27B0"),
    DEEPPURPLE("#7C4DFF"),
    INDIGO("#303F9F"),
    BLUE("#448AFF"),
    CYAN("#00BCD4"),
    TEAL("#009688"),
    GREEN("#4CAF50"),
    LIME("#CDDC39"),
    YELLOW("#FFEB3B"),
    AMBER("#FFC107"),
    ORANGE("#F57C00"),
    DEEPORANGE("#FF5722"),
    BROWN("#795548"),
    GREY("#9E9E9E"),
    BLUEGRAW("#607D8B"),
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    ;
    public String colorCode;
    PalleteColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public static GradientDrawable getGradientDrawable(PalleteColor palleteColor, Resources resources) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);
        drawable.setSize(resources.getDimensionPixelSize(R.dimen.play_footer_color_icon), resources.getDimensionPixelSize(R.dimen.play_footer_color_icon));
        int color = Color.parseColor(palleteColor.colorCode);

        drawable.setColor(color);
        return drawable;
    }



}
