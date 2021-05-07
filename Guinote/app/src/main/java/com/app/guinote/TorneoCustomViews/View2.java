package com.app.guinote.TorneoCustomViews;


import android.app.Activity;
import android.content.Context;

import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.app.guinote.TorneoApp.App1;

/**
 * Created by Emil on 21/10/17.
 */

public class View2 extends ViewPager {

    private Context context;

    public View2(Context context) {
        super(context);
        this.context = context;
    }

    public View2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            int h = child.getMeasuredHeight();

            if (h > height) height = h;

            int screenHeight = App1.getInstance().getScreenHeight();
            if (screenHeight > height)
                height = screenHeight;
        }


        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int[] getScreenSIze() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int h = displaymetrics.heightPixels;
        int w = displaymetrics.widthPixels;

        int[] size = {w, h};
        return size;

    }
}