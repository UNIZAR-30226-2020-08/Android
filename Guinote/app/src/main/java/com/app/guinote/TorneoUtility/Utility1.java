package com.app.guinote.TorneoUtility;

import android.util.DisplayMetrics;

import com.app.guinote.TorneoApp.App1;

public class Utility1 {
    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = App1.getInstance().getBaseContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}