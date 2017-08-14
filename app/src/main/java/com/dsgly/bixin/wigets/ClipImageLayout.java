package com.dsgly.bixin.wigets;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.dsgly.bixin.utils.BitmapHelper;


/**
 * Created by liqiang on 2016/4/25.
 * liliqiang@corp.netease.com
 */

public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;
    public static float SCALE = 1;//宽度和高度之比
    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding;
    public static int BORDER_WIDTH_DIP = 20;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);

        final android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        // 计算padding的px
        final int borderWidth = BitmapHelper.dip2px(BORDER_WIDTH_DIP);
        post(new Runnable() {
            @Override
            public void run() {
                if (getWidth() < getHeight()) {
                    mHorizontalPadding = borderWidth;
                } else {
                    mHorizontalPadding = (getWidth() - (getHeight() - 2 * borderWidth)) / 2;
                }
                mZoomImageView.setHorizontalPadding(mHorizontalPadding);
                mClipImageView.setHorizontalPadding(mHorizontalPadding);
                addView(mZoomImageView, lp);
                addView(mClipImageView, lp);
            }
        });

    }

    public void setEditImageBitmap(Bitmap raw) {
        mZoomImageView.setImageBitmap(raw);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public void setScale(float scale) {
        SCALE = scale;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }

}
