package com.dsgly.bixin.wigets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

/**
 * Created by dengxuan on 2017/8/6.
 */

public class UploadVideoPlayerView extends LinearLayout {
    public UploadVideoPlayerView(Context context) {
        super(context);
    }

    public UploadVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }
}
