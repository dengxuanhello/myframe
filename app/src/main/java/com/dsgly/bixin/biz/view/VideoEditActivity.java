package com.dsgly.bixin.biz.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;

import java.io.File;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;

/**
 * Created by dengxuan on 2017/8/7.
 */

public class VideoEditActivity extends BaseActivity implements OnTrimVideoListener, OnK4LVideoListener {

    private K4LVideoTrimmer k4LVideoTrimmer;
    private String videoUrl;

    public static void startVideoEditActivity(Context context, String videoUrl, int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, VideoEditActivity.class);
            intent.putExtra(VideoRecorderActivity.CHOOSED_VIDEO_PATH,videoUrl);
            if(context instanceof Activity){
                ((Activity)context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void initViews() {
        if(!judgeData()){
            finish();
            return;
        }
        setContentView(R.layout.edit_video_activity);
        k4LVideoTrimmer = (K4LVideoTrimmer) findViewById(R.id.timeLine);
        if (k4LVideoTrimmer != null) {
            k4LVideoTrimmer.setMaxDuration(30);
            String savePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath()+ File.separator+"trimmer";
            k4LVideoTrimmer.setDestinationPath(savePath);
            k4LVideoTrimmer.setVideoURI(Uri.parse(videoUrl));
            k4LVideoTrimmer.setVideoInformationVisibility(true);
            k4LVideoTrimmer.setOnTrimVideoListener(this);
            k4LVideoTrimmer.setOnK4LVideoListener(this);
        }
    }

    private boolean judgeData(){
        if(getIntent() == null){
            return false;
        }
        videoUrl = getIntent().getStringExtra(VideoRecorderActivity.CHOOSED_VIDEO_PATH);
        if(!TextUtils.isEmpty(videoUrl)){
            return true;
        }
        return false;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what != Video_onTrimStarted){
                closeProgressDialog();
            }
            switch (msg.what){
                case Video_onTrimStarted:
                    showProgressDialog("正在处理中...");
                    break;
                case Video_getResult:
                    Uri uri = (Uri) msg.obj;
                    backForResult(uri.toString());
                    break;
                case Video_cancelAction:
                    break;
                case Video_onError:
                    break;
                case Video_onVideoPrepared:

                    break;
            }
        }
    };

    private static final int Video_onTrimStarted = 0;
    private static final int Video_getResult = 1;
    private static final int Video_cancelAction = 2;
    private static final int Video_onError = 3;
    private static final int Video_onVideoPrepared = 4;

    @Override
    public void onTrimStarted() {
        handler.sendEmptyMessage(Video_onTrimStarted);
    }

    @Override
    public void getResult(Uri uri) {
        Message message = new Message();
        message.what = Video_getResult;
        message.obj = uri;
        handler.sendMessage(message);
    }

    @Override
    public void cancelAction() {
        handler.sendEmptyMessage(Video_cancelAction);
    }

    @Override
    public void onError(String message) {
        handler.sendEmptyMessage(Video_onError);
    }

    @Override
    public void onVideoPrepared() {
        handler.sendEmptyMessage(Video_onVideoPrepared);
    }

    private void backForResult(String path){
        Intent intent = new Intent();
        intent.putExtra(VideoRecorderActivity.CHOOSED_VIDEO_PATH,path);
        setResult(RESULT_OK,intent);
        finish();
    }
}
