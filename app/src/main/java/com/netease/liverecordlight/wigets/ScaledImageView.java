package com.netease.liverecordlight.wigets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class ScaledImageView extends ImageView {
    //宽高比3：4
    private int mRatioX=3;
    private int mRatioY=4;

    public ScaledImageView(Context context) {
        super(context);
    }

    public ScaledImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        //int height = getMeasuredHeight();
        int height = width * mRatioY / mRatioX;
        super.onMeasure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }
}
