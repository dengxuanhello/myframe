package com.dsgly.bixin.biz.view;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.utils.MediaUtils;
import com.netease.svsdk.biz.*;
import com.netease.svsdk.constants.RequestCode;

/**
 * Created by dengxuan on 2017/8/6.
 */

public class VideoRecorderActivity extends BaseActivity {
    public static final String CHOOSED_VIDEO_PATH = "choosed_video_path";
    public static final int EDIT_VIDEO_REQUEST_CODE = 0x100;
    private MediaUtils mediaUtils;
    private int mProgress = 0;
    private ImageView startRecordbtn;
    private RelativeLayout recordLayout;
    private TextView timeTv;
    private RelativeLayout importVideo;
    private RelativeLayout fullScreenRecord;
    private RelativeLayout deleteVideo;
    private RelativeLayout completeRecord;
    private TextView editVideo;
    private RelativeLayout buttomAreaRl;
    private ImageView fullScreenIv;
    private RelativeLayout topActionRl;
    private RecordProgressView recordProgressView;
    private Boolean isFullScreenRecord = false;

    public static void startVideoRecordActivity(Context context,int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, VideoRecorderActivity.class);
            if(context instanceof Activity){
                ((Activity)context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.main_surface_view);
        timeTv = (TextView) findViewById(R.id.time_tv);
        importVideo = (RelativeLayout) findViewById(R.id.btn_import);
        importVideo.setOnClickListener(this);
        fullScreenRecord = (RelativeLayout) findViewById(R.id.full_screen_record);
        fullScreenRecord.setOnClickListener(this);
        deleteVideo = (RelativeLayout) findViewById(R.id.btn_delete);
        deleteVideo.setOnClickListener(this);
        completeRecord = (RelativeLayout) findViewById(R.id.complete_record);
        completeRecord.setOnClickListener(this);
        editVideo = (TextView) findViewById(R.id.edit_video);
        editVideo.setOnClickListener(this);
        buttomAreaRl = (RelativeLayout) findViewById(R.id.buttom_area_rl);
        fullScreenIv = (ImageView) findViewById(R.id.full_screen_record_iv);
        topActionRl = (RelativeLayout) findViewById(R.id.top_action_area);
        recordProgressView = (RecordProgressView) findViewById(R.id.record_progress_view);
        fullScreenIv.setOnClickListener(this);
        mediaUtils = new MediaUtils(this);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
        mediaUtils.setTargetDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
        mediaUtils.setTargetName("bixin_"+System.currentTimeMillis() + ".mp4");
        mediaUtils.setSurfaceView(surfaceView);
        startRecordbtn = (ImageView) findViewById(R.id.main_press_control);
        startRecordbtn.setTag("record");
        startRecordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("record".equals(startRecordbtn.getTag())) {
                    startRecord();
                }else {
                    stopRecord();
                }
            }
        });
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaUtils!=null){
                    mediaUtils.releaseMediaRecorder();
                    mediaUtils.releaseCamera();
                }
                finish();
            }
        });
        recordLayout = (RelativeLayout) findViewById(R.id.record_layout);
        findViewById(R.id.btn_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaUtils != null) {
                    mediaUtils.switchCamera();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(mediaUtils!=null){
            mediaUtils.releaseMediaRecorder();
            mediaUtils.releaseCamera();
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if(mediaUtils!=null){
            mediaUtils.releaseMediaRecorder();
            mediaUtils.releaseCamera();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(deleteVideo)){

        }else if(v.equals(completeRecord)){
            if(!TextUtils.isEmpty(mediaUtils.getTargetFilePath())){
                backForResult(mediaUtils.getTargetFilePath());
            }
        }else if(v.equals(importVideo)){
            ImportVideoActivity.startPickVideosActivity(this,RequestCode.REQ_FOR_PIC_VIDEO);
        }else if(v.equals(fullScreenRecord)){

        }else if(v.equals(editVideo)){
            //VideoEditActivity.startVideoEditActivity(this,mediaUtils.getTargetFilePath(),EDIT_VIDEO_REQUEST_CODE);
            Bundle bundle = new Bundle();
            bundle.putString("cut_video_source_path",mediaUtils.getTargetFilePath());
            VideoCutActivity.startVideoCutActivity(this,bundle,RequestCode.REQ_FOR_CUT_VIDEO);
        } else if(v.equals(fullScreenIv)){
            if(isFullScreenRecord) {
                buttomAreaRl.setBackground(getDrawable(R.color.black));
                topActionRl.setBackground(getDrawable(R.color.black));
                fullScreenIv.setImageDrawable(getDrawable(R.drawable.button_full_screen));
            }else {
                buttomAreaRl.setBackground(getDrawable(R.color.transparent));
                topActionRl.setBackground(getDrawable(R.color.transparent));
                fullScreenIv.setImageDrawable(getDrawable(R.drawable.screen_switch_to_small));
            }
            isFullScreenRecord = !isFullScreenRecord;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mediaUtils.isRecording()) {
                        mProgress = mProgress + 1;
                        timeTv.setText("00:"+parseTimeToString(mProgress*1000));
                        sendMessageDelayed(handler.obtainMessage(0), 1000);
                        recordProgressView.setCurrentProgress(mProgress);
                    }
                    break;
            }
        }
    };

    /**
     * @param startTime :毫秒
     */
    private String parseTimeToString(long startTime) {
        if (startTime <= 1000) {
            return "00:01";
        } else {
            String formatTime = "%s:%s";
            long second = startTime / 1000;
            if (second < 60) {
                return String.format(formatTime, "00", formatLongString(second));
            } else {
                long minite = second / 60;
                long secondMin = second % 60;
                return String.format(formatTime, formatLongString(minite), formatLongString(secondMin));
            }
        }
    }

    private String formatLongString(long time) {
        String result = String.valueOf(time);
        if (result.length() == 1) {
            result = "0" + result;
        }
        return result;
    }

    private void backForResult(String path){
        Intent intent = new Intent();
        intent.putExtra(CHOOSED_VIDEO_PATH,path);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void startRecord(){
        mProgress = 0;
        mediaUtils.record();
        handler.sendEmptyMessage(0);
        startRecordbtn.setTag("stop");
        timeTv.setText("");
        timeTv.setVisibility(View.VISIBLE);
        importVideo.setVisibility(View.GONE);
        fullScreenRecord.setVisibility(View.GONE);
        editVideo.setVisibility(View.GONE);
        deleteVideo.setVisibility(View.GONE);
        completeRecord.setVisibility(View.GONE);
    }

    private void stopRecord(){
        mediaUtils.stopRecordSave();
        startRecordbtn.setTag("record");
        editVideo.setVisibility(View.VISIBLE);
        deleteVideo.setVisibility(View.VISIBLE);
        completeRecord.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == EDIT_VIDEO_REQUEST_CODE){
            setResult(resultCode,data);
            finish();
        }else if(requestCode == RequestCode.REQ_FOR_CUT_VIDEO){
            if(resultCode == RESULT_OK){
                String videoPath = data.getStringExtra("cutVideo");
                Log.i("dx",videoPath);
                PublishMomentActivity.startPublishMomentActivity(this,videoPath);
            }
        }else {
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
