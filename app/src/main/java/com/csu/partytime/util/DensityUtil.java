package com.csu.partytime.util;

import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.csu.partytime.PartyApplication;

/**
 * Created by ${Chen_Xingchao} on 2015/4/6.
 */
public class DensityUtil {
    public static int pix2dp(int pix) {
        DisplayMetrics dm = PartyApplication.context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pix, dm);
    }
}
