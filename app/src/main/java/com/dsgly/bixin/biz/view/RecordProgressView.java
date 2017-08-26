package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dengxuan on 2017/8/20.
 */

public class RecordProgressView extends View {

    private int maxProgress = 100; //单位秒
    private int currentProgress = 0;
    private int okProgress = 10; //单位秒
    private Paint mBgPaint;
    private Paint mProgressPaint;
    private Paint mOkPaint;
    private Rect bgRect;

    public RecordProgressView(Context context) {
        super(context);
        init();
    }

    public RecordProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.GRAY);
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.RED);
        mOkPaint = new Paint();
        mOkPaint.setAntiAlias(true);
        mOkPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawRect(0,0,width,height,mBgPaint);
        int progressWidth = width * currentProgress / maxProgress;
        canvas.drawRect(0,0,progressWidth,height,mProgressPaint);
        //画可以发布的点
        int okPoint = width * okProgress / maxProgress;
        canvas.drawRect(okPoint-1,0,okPoint+1,height,mOkPaint);
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
    }

    public void setOkProgress(int okProgress) {
        this.okProgress = okProgress;
        invalidate();
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }
}
