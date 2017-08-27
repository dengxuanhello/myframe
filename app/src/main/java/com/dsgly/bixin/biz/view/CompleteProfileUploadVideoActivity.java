package com.dsgly.bixin.biz.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.CompleteProfileUploadVideoPresenetr;
import com.dsgly.bixin.net.responseResult.GalleryResult;
import com.dsgly.bixin.wigets.PicGridView;
import com.dsgly.bixin.wigets.PicGridViewAdapter;
import com.dsgly.bixin.wigets.WrapHeightLayoutManager;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by dengxuan on 2017/8/6.
 */

public class CompleteProfileUploadVideoActivity extends BaseActivity implements PicGridViewAdapter.OnItemChooseListener, SurfaceHolder.Callback {
    public static int REQUEST_CODE_FOR_VIDEO = 0x10;
    public static int REQUEST_CODE_FOR_PIC = 0x11;
    private CompleteProfileUploadVideoPresenetr presenter;
    public SimpleExoPlayer simpleExoPlayer;
    public Button mUploadBtn;
    public Button mDoneBtn;
    public SurfaceView simpleExoPlayerView;
    public ImageView moreBtn;
    public ImageView backBtn;
    public ImageView playBtn;
    public String videoUrl;
    public PicGridView photosView;
    public PicGridViewAdapter mPicGridViewAdapter;
    public List<GalleryResult.GalleryInfo> galleryInfoList;
    public static void startCompleteProfileUploadVideoActivity(Context context,int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, CompleteProfileUploadVideoActivity.class);
            if(context instanceof Activity){
                ((Activity) context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void createPresenter() {
        presenter = new CompleteProfileUploadVideoPresenetr();
        presenter.attachView(this);
    }

    @Override
    protected void onPause() {
        simpleExoPlayer.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!TextUtils.isEmpty(videoUrl) && simpleExoPlayer != null){
            presenter.prepareDataSource(videoUrl);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.complete_profile_upload_video_activity);
        mUploadBtn = (Button) findViewById(R.id.user_upload_video);
        mUploadBtn.setOnClickListener(this);
        simpleExoPlayerView = (SurfaceView) findViewById(R.id.player_video);
        simpleExoPlayerView.getHolder().addCallback(this);
        mDoneBtn = (Button) findViewById(R.id.complete_btn);
        mDoneBtn.setOnClickListener(this);
        presenter.setUpExoPlayer(this);
        //simpleExoPlayerView.setPlayer(simpleExoPlayer);
        moreBtn = (ImageView) findViewById(R.id.more_icon);
        moreBtn.setOnClickListener(this);
        playBtn = (ImageView) findViewById(R.id.main_page_item_img_btn);
        playBtn.setOnClickListener(this);
        photosView = (PicGridView) findViewById(R.id.photos);
        photosView.setLayoutManager(new GridLayoutManager(this, 3));
    }

    @Override
    public void initData() {
        super.initData();
        galleryInfoList = new ArrayList<GalleryResult.GalleryInfo>();
        galleryInfoList.add(new GalleryResult.GalleryInfo("add"));
        mPicGridViewAdapter = new PicGridViewAdapter(galleryInfoList,this);
        mPicGridViewAdapter.setItemChooseListener(this);
        photosView.setAdapter(mPicGridViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mUploadBtn)){
            if(videoUrl != null){
                presenter.uploadVideo(videoUrl);
            }
        }else if(v.equals(moreBtn)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED
                        ||ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                        ||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString(VideoRecorderActivity.FROM_SOURCE,VideoRecorderActivity.FROM_CompleteProfileUploadVideoActivity);
                    VideoRecorderActivity.startVideoRecordActivity(this,bundle,REQUEST_CODE_FOR_VIDEO);
                }
            }else {
                Bundle bundle = new Bundle();
                bundle.putString(VideoRecorderActivity.FROM_SOURCE,VideoRecorderActivity.FROM_CompleteProfileUploadVideoActivity);
                VideoRecorderActivity.startVideoRecordActivity(this,bundle,REQUEST_CODE_FOR_VIDEO);
            }
        }else if(v.equals(playBtn)){
            if(simpleExoPlayer != null){
                simpleExoPlayer.seekTo(0);
                simpleExoPlayer.setPlayWhenReady(true);
            }
        }else if(v.equals(mDoneBtn)){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if(simpleExoPlayer != null){
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_FOR_VIDEO){
            if(resultCode == RESULT_OK){
                if(data != null) {
                    videoUrl = data.getStringExtra(VideoRecorderActivity.CHOOSED_VIDEO_PATH);
                    if(simpleExoPlayer!=null){
                        presenter.prepareDataSource(videoUrl);
                    }
                }
            }
        }else if(requestCode == REQUEST_CODE_FOR_PIC){
            if(resultCode == RESULT_OK){
                ArrayList<String> stringArrayListExtra = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                if(stringArrayListExtra!=null && stringArrayListExtra.size()>0){
                    galleryInfoList.clear();
                }
                for(String s : stringArrayListExtra){
                    Log.i("dx",s);
                    GalleryResult.GalleryInfo galleryInfo = new GalleryResult.GalleryInfo(s);
                    galleryInfo.picThumb = s;
                    galleryInfo.picOrigin = s;
                    galleryInfoList.add(galleryInfo);
                    mPicGridViewAdapter.setDataList(galleryInfoList);
                    mPicGridViewAdapter.notifyDataSetChanged();
                    photosView.requestLayout();
                }
            }
        }
    }

    @Override
    public void onItemChoosed(GalleryResult.GalleryInfo data) {
        if(data == null){
            return;
        }
        if("add".equals(data.pic)){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Permission was added in API Level 16
                 if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                     MultiImageSelector.create(this)
                             .start(this, REQUEST_CODE_FOR_PIC);
                 }else {
                     requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                 }
            }else {
                MultiImageSelector.create(this)
                        .start(this, REQUEST_CODE_FOR_PIC);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        simpleExoPlayer.setVideoSurface(holder.getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
