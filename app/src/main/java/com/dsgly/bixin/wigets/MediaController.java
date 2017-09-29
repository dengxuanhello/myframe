package com.dsgly.bixin.wigets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dsgly.bixin.R;

import java.util.Locale;

/**
 * 只依附于VideoView存在，不直接与fragment交互
 * Created by madong on 2017/2/22.
 */

public class MediaController extends FrameLayout {
    private static final String TAG = "MediaController";

    private MediaPlayerControl mPlayer;
    private Context mContext;
    private PopupWindow mWindow;
    private int mAnimStyle;
    private View mAnchor;
    private View mRoot;
    private SeekBar mSeekBar;
    private TextView mEndTime, mCurrentTime;
    private TextView mFileName;
    private String mTitle;
    private long mDuration;
    private boolean mShowing = true;
    private boolean mDragging;
    private boolean mInstantSeeking = false; //false则仅在拖动手指抬起后seek
    private static final int sDefaultTimeout = 3000;//3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mFromXml = false;
    private ImageView mPauseButton;
    private ImageView mSetPlayerScaleButton;
//    private ImageView mSnapshotButton;
//    private ImageView mMuteButton;
    private View livingView;

    private boolean mute_flag = false;
    private boolean mPaused = false;
    private boolean mIsFullScreen = false;

    private int mVideoScalingMode = VIDEO_SCALING_MODE_FIT;
    public static final int VIDEO_SCALING_MODE_NONE = 0;
    public static final int VIDEO_SCALING_MODE_FIT  = 1;
    public static final int VIDEO_SCALING_MODE_FILL = 2;
    public static final int VIDEO_SCALING_MODE_FULL = 3;
    private OnControllerListener onControllerListener; // 显示隐藏统一交由外部处理

    //通过Context对象和AttributeSet对象来创建MediaController对象
    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mFromXml = true;
        initController(context);
        makeControllerView(); // madong
    }

    //通过Context来创建MediaController对象
    public MediaController(Context context) {
        super(context);
        if (!mFromXml && initController(context))
            initFloatingWindow();
    }

    private boolean initController(Context context) {
        mContext = context;
        return true;
    }

    //从XML加载完所有子视图后调用。初始化控制视图（调用initControllerView方法，设置事件、绑定控件和设置默认值）
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        if (mRoot != null)
            initControllerView(mRoot);
    }

    // 非xml加载布局，需自行生成view
    private void initFloatingWindow() {
        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setBackgroundDrawable(null);
        mWindow.setOutsideTouchable(true);
        mAnimStyle = android.R.style.Animation;
    }

    /**
     * 设置MediaController绑定到一个视图上。例如可以是一个VideoView对象，或者是你的activity的主视图。
     *
     * @param view
     *            view	可见时绑定的视图
     */
    public void setAnchorView(View view) {
        mAnchor = view;
        if (!mFromXml) {
            removeAllViews();
            mRoot = makeControllerView();
            mWindow.setContentView(mRoot);
            mWindow.setWidth(LayoutParams.MATCH_PARENT);
            mWindow.setHeight(LayoutParams.WRAP_CONTENT);
        }
        initControllerView(mRoot);
    }

    /**
     * Create the view that holds the widgets that control playback. Derived
     * classes can override this to create their own.
     *
     * @return The controller view.
     */
    protected View makeControllerView() {
        if (mContext == null) {
            return null;
        }
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_player_controller, this);
    }

    private void initControllerView(View v) {
        mPauseButton = (ImageView) v.findViewById(R.id.iv_pause); //播放暂停按钮
        if (mPauseButton != null) {
            if (mPaused) {
                mPauseButton.setImageResource(R.drawable.luobo_ic_controller_play);
                mPlayer.pause();
            }
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mSetPlayerScaleButton = (ImageView) v.findViewById(R.id.iv_screen_switch);  //画面显示模式按钮
        if (mSetPlayerScaleButton != null) {
            mSetPlayerScaleButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeOrientation();
                }
            });
        }
        /*if(mSetPlayerScaleButton != null) {
            if (mPlayer.isHardware() && mPlayer.isInBackground()) {
                switch(mVideoScalingMode)
                {
                    case VIDEO_SCALING_MODE_FIT:
                        mVideoScalingMode = VIDEO_SCALING_MODE_FIT;
                        mSetPlayerScaleButton.setImageResource(R.drawable.nemediacontroller_scale01);
                        break;
                    case VIDEO_SCALING_MODE_NONE:
                        mVideoScalingMode = VIDEO_SCALING_MODE_NONE;
                        mSetPlayerScaleButton.setImageResource(R.drawable.nemediacontroller_scale02);
                        break;
                    default:
                        mVideoScalingMode = VIDEO_SCALING_MODE_NONE;
                }
                mPlayer.setVideoScalingMode(mVideoScalingMode);
            }

            mSetPlayerScaleButton.requestFocus();
            mSetPlayerScaleButton.setOnClickListener(mSetPlayerScaleListener);
        }*/
/*
        mSnapshotButton = (ImageView) v.findViewById(R.id.snapShot);  //截图按钮
        if (mSnapshotButton != null) {
            mSnapshotButton.requestFocus();
            mSnapshotButton.setOnClickListener(mSnapShotListener);
        }

        mMuteButton = (ImageView) v.findViewById(R.id.video_player_mute);  //静音按钮
        if (mMuteButton != null) {
            if (mPlayer.isHardware() && mPlayer.isInBackground()) {
                if (mute_flag) {
                    mMuteButton.setImageResource(R.drawable.nemediacontroller_mute01);
                    mPlayer.setMute(true);
                }
            }
            mMuteButton.setOnClickListener(mMuteListener);
        }
*/
        mSeekBar = (SeekBar) v.findViewById(R.id.player_controller_seek_bar);  //进度条
        if (mSeekBar != null) {
//            if (mSeekBar instanceof SeekBar) {
//                SeekBar seeker = (SeekBar) mSeekBar;
            mSeekBar.setOnSeekBarChangeListener(mSeekListener);
            mSeekBar.setThumbOffset(1);
//            }
            mSeekBar.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.player_controller_time_total); //总时长
        mCurrentTime = (TextView) v.findViewById(R.id.player_controller_time_current); //当前播放位置
        livingView = v.findViewById(R.id.tv_living_now);

//        if(mPlayer.getMediaType().equals("localaudio")) {
//        	mSetPlayerScaleButton.setVisibility(View.INVISIBLE); //播放音乐不需要设置显示模式，该按钮不显示
//        	mSnapshotButton.setVisibility(View.INVISIBLE);       //播放音乐不需要截图，该按钮不显示
//        	show();
//        }

//        if (mPlayer.isHardware()) {
//        	mSnapshotButton.setVisibility(View.INVISIBLE); //硬件解码不支持截图，该按钮不显示
//        }
    }

    // 切换横竖屏
    private void changeOrientation() {

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            ((Activity) getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    //设置MediaPlayer使之与要绑定的控件绑定在一起其参数是一个MediaController.MediaPlayerControl 静态接口的对象，
    //(而VideoView是MediaController.MediaPlayerControl静态接口的子实现类，这就使得我们可以更好的控制我们的视频播放进度)
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();

        // madong,目前用法没调用show方法，直接在此处开启
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

    }

    // 目前用法没调用show方法，直接调用此方法
    public void update() {
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
    }

    public void setOnControllerListener(OnControllerListener onControllerListener) {
        this.onControllerListener = onControllerListener;
    }

    public void updateScreenMode(boolean isFullScreen) {
        if (mSetPlayerScaleButton != null) {
            mSetPlayerScaleButton.setImageResource(isFullScreen ? R.drawable.luobo_ic_player_switch_to_small : R.drawable.luobo_ic_player_switch_large);
        }
    }

    /**
     * Control the action when the seekbar dragged by user
     *
     * @param seekWhenDragging
     *            True the media will seek periodically
     */
    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    //显示MediaController。默认显示3秒后自动隐藏。
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Set the content of the file_name TextView
     * 设置视频文件名称。
     *
     * @param name
     */
    public void setFileName(String name) {
        mTitle = name;
        if (mFileName != null)
            mFileName.setText(mTitle);
    }

    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause())
                mPauseButton.setEnabled(false);
        } catch (IncompatibleClassChangeError ex) {
            ex.printStackTrace();
        }
    }

    public void setAnimationStyle(int animationStyle) {
        mAnimStyle = animationStyle;
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout
     *            The timeout in milliseconds. Use 0 to show the controller
     *            until hide() is called.
     */
    @SuppressLint({ "InlinedApi", "NewApi" })
    public void show(int timeout) {
        if (!mShowing && (mFromXml || (mAnchor != null && mAnchor.getWindowToken() != null))) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH){
                if (mAnchor != null) {
                    mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
            }
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            disableUnsupportedButtons();

            if (mFromXml) {
                setVisibility(View.VISIBLE);
            } else {
                int[] location = new int[2];

                mAnchor.getLocationOnScreen(location);
                Rect anchorRect = new Rect(location[0], location[1],
                        location[0] + mAnchor.getWidth(), location[1]
                        + mAnchor.getHeight());

                mWindow.setAnimationStyle(mAnimStyle);
                mWindow.showAtLocation(mAnchor, Gravity.BOTTOM, anchorRect.left, 0);
            }
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT), timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    @SuppressLint({ "InlinedApi", "NewApi" })
    //隐藏MediaController。
    public void hide() {
        if (!mFromXml && mAnchor == null)
            return;

        if (mShowing) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && mAnchor != null){
                mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                if (mFromXml)
                    setVisibility(View.GONE);
                else
                    mWindow.dismiss();
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "MediaController already removed");
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    public void hideSwitch() {
        mSetPlayerScaleButton.setVisibility(GONE);
    }

    public interface OnShownListener {
        public void onShown();
    }

    private OnShownListener mShownListener;

    //注册一个回调函数，在MediaController显示后被调用。
    public void setOnShownListener(OnShownListener l) {
        mShownListener = l;
    }

    public interface OnHiddenListener {
        public void onHidden();
    }

    private OnHiddenListener mHiddenListener;

    //注册一个回调函数，在MediaController隐藏后被调用。
    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
            }
        }
    };

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        long position = mPlayer.getCurrentPosition();
        long duration = mPlayer.getDuration();

        //====为了解决播放器的一个Bug：拖动进度条，或者暂停之后，再继续播放的时候，mPlayer.getCurrentPosition()不准确，导致进度条会出瞬间现往回走一下=====
        // madong，新版本不需要
        /*if (position > mCurrPosition) {//只允许向前走
            mCurrPosition = position;
        }*/
        //=====END========
        if (mSeekBar != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mSeekBar.setProgress((int) pos);
            } else {
                mSeekBar.setProgress(0);
            }
            // +1为了解决最大99的问题
            int percent = mPlayer.getBufferPercentage();
            if (percent >= 95) {
                percent = 100;
            }
            mSeekBar.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (mEndTime != null) {
            mEndTime.setText(stringForTime(mDuration));
        }
        if (mCurrentTime != null) {
            mCurrentTime.setText(stringForTime(position));
        }

        return position;
    }

    private static String stringForTime(long position) {
        if (position <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) ((position / 1000.0)+0.5);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //show(sDefaultTimeout);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                show(0); // show until hide is called
                if (onControllerListener != null) {
                    onControllerListener.showIgnoreTimeout();
                }
                break;
            case MotionEvent.ACTION_UP:
//                show(sDefaultTimeout); // start timeout
                if (onControllerListener != null) {
                    onControllerListener.hideAfterTimeout();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
//                hide();
                if (onControllerListener != null) {
                    onControllerListener.hideAfterTimeout();
                }
                break;
            default:
                break;
        }
        return true;
    }
/*
    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }
*/
/* 静音按钮
    private View.OnClickListener mMuteListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (!mute_flag) {
                mMuteButton.setImageResource(R.drawable.nemediacontroller_mute01);
                mPlayer.setMute(true);
                mute_flag = true;
            }
            else {
                mMuteButton.setImageResource(R.drawable.nemediacontroller_mute02);
                mPlayer.setMute(false);
                mute_flag = false;
            }
        }
    };
*/
/*
    private View.OnClickListener mSetPlayerScaleListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mIsFullScreen) {
                mVideoScalingMode = VIDEO_SCALING_MODE_NONE;
                mSetPlayerScaleButton.setImageResource(R.drawable.luobo_ic_player_switch_large);
                mIsFullScreen = false;
            }
            else {
                mVideoScalingMode = VIDEO_SCALING_MODE_FIT;
                mSetPlayerScaleButton.setImageResource(R.drawable.luobo_ic_player_switch_to_small);
                mIsFullScreen = true;
            }

            try {
                mPlayer.setVideoScalingMode(mVideoScalingMode);
            } catch (NumberFormatException e) {

            }
        }
    };

    private View.OnClickListener mSnapShotListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if(mPlayer.getMediaType().equals("localaudio") || mPlayer.isHardware()) {
                AlertDialog alertDialog;
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("注意");
                if (mPlayer.getMediaType().equals("localaudio"))
                    alertDialogBuilder.setMessage("音频播放不支持截图！");
                else if (mPlayer.isHardware())
                    alertDialogBuilder.setMessage("硬件解码不支持截图！");
                alertDialogBuilder.setCancelable(false)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ;
                            }
                        });
                alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return;
            }

            mPlayer.getSnapshot();
        }
    };
*/
    private OnClickListener mPauseListener = new OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
//            show(sDefaultTimeout);
        }
    };

    // 1秒循环一次
    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.luobo_ic_player_pause);
        }
        else {
            mPauseButton.setImageResource(R.drawable.luobo_ic_controller_play);
        }

    }

    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer.manualPause(true);
            mPaused = true;
        }
        else {
            mPlayer.start();
            mPlayer.manualPause(false);
            mPaused = false;
        }
        updatePausePlay();
    }

    private Runnable lastRunnable;
    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
//            show(3600000);  //madong,直接remove fade out消息
            mDragging = true;

            mHandler.removeMessages(SHOW_PROGRESS);

            // 拖动过程中不隐藏
//            mHandler.removeMessages(FADE_OUT);
            if (onControllerListener != null) {
                onControllerListener.showIgnoreTimeout();
            }
        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {

            final long newposition = (mDuration * progress) / 1000;
            if (fromuser) {
                String time = stringForTime(newposition);
                if (mInstantSeeking) {
                    mHandler.removeCallbacks(lastRunnable);
                    lastRunnable = new Runnable() {
                        @Override
                        public void run() {
                            mPlayer.seekTo(newposition);
                        }
                    };
                    mHandler.postDelayed(lastRunnable, 200);
                }

                if (mCurrentTime != null)
                    mCurrentTime.setText(time);
            }

        }

        public void onStopTrackingTouch(SeekBar bar) {
                if (!mInstantSeeking)
                    mPlayer.seekTo((mDuration * bar.getProgress()) / 1000);

//            show(sDefaultTimeout);  直接恢复FADE_OUT消息
            mHandler.removeMessages(SHOW_PROGRESS);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);

            // madong 拖动结束恢复
//            mHandler.sendEmptyMessageDelayed(FADE_OUT, sDefaultTimeout);
            if (onControllerListener != null) {
                onControllerListener.hideAfterTimeout();
            }
        }
    };

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mSeekBar != null) {
            mSeekBar.setEnabled(enabled);
        }
/*
        if (mSetPlayerScaleButton != null) {
            mSetPlayerScaleButton.setEnabled(enabled);
        }

        if (mSnapshotButton != null) {
            mSnapshotButton.setEnabled(enabled);
        }
        if (mMuteButton != null) {
            mMuteButton.setEnabled(enabled);
        }
*/

        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDetachedFromWindow();
    }

    public void seekTo(long startPoint) {
        mPlayer.seekTo(startPoint * 1000);
    }

    /**
     * 将毫秒值转换成progressBar的进度
     */
    private int progressBarValue(long position) {
        long duration = mPlayer == null ? 0 : mPlayer.getDuration();
        return duration <= 0 ? -1 : (int) ((position * 1000 * 1000) / duration);
    }

    public boolean isEnabled() {
        return mPlayer.getDuration() >= 0;
    }

    public interface MediaPlayerControl {
        void start();

        void pause();

        long getDuration();

        long getCurrentPosition();

        void seekTo(long pos);

        boolean isPlaying();

        void manualPause(boolean paused);

        int getBufferPercentage();

        boolean canPause();

    }

    public static interface OnControllerListener {
        void showIgnoreTimeout();

        void hideAfterTimeout();

    }

}
