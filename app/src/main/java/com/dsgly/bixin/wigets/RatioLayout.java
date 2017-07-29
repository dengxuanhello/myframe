package com.dsgly.bixin.wigets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class RatioLayout extends RelativeLayout {

    public static final String TAG = RatioLayout.class.getSimpleName();

    // 以x轴方向为基准，进行缩放.
    public static final int RATIO_MODE_X = 1;
    // 以y轴方向为基准，进行缩放
    public static final int RATIO_MODE_Y = 2;

    private int mRatioX=4;
    private int mRatioY=3;
    private int mRatioMode=RATIO_MODE_X;

    public RatioLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        switch (mRatioMode) {
            case RATIO_MODE_X:
                height = width * mRatioY / mRatioX;
                break;
            case RATIO_MODE_Y:
                width = height * mRatioX / mRatioY;
                break;
            default:
                Log.w(TAG, "Unsupported ratio mode.");
                break;
        }
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    /**
     * setRatio can be used to reset the width-height ratio of this widget.
     *
     * @param x
     * @param y
     */
    public void setRatio(int x, int y) {
        if (mRatioX != x || mRatioY != y) {
            mRatioX = x;
            mRatioY = y;
            requestLayout();
        }
    }

    /**
     * setRatioMode will change the constraint relationship of width and height.
     *
     * @param mode You can chose {@link #RATIO_MODE_X} or {@link #RATIO_MODE_Y} to be the mode.
     *
     * @see #RATIO_MODE_X
     * @see #RATIO_MODE_Y
     */
    public void setRatioMode(int mode) {
        if (mRatioMode != mode) {
            mRatioMode = mode;
            requestLayout();
        }
    }
}