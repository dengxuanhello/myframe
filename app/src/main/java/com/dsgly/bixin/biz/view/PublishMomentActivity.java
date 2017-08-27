package com.dsgly.bixin.biz.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.utils.ImageUtil;
import com.dsgly.bixin.utils.PathUtil;
import com.netease.svsdk.biz.VideoCutActivity;
import com.netease.svsdk.constants.RequestCode;
import com.netease.svsdk.tools.ViewUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.dsgly.bixin.biz.view.CompleteProfileActivity.REQUEST_CODE_FOR_ClIP_PIC;

/**
 * Created by dengxuan on 2017/8/23.
 */

public class PublishMomentActivity extends BaseActivity {
    private static final String VIDEO_PATH = "cut_video_des_path";
    private static final String PIC_PATH = "pic_path";

    private String videoPath;
    private String picPath;
    private ImageView coverImg;
    private AsyncTask<String,Void,ExtractInfo> task;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    private TextView sureTv;
    private TextView editCoverTv;
    private TextView editVideoTv;
    private EditText momentTextEt;
    private TextView mTimeTv;
    private TextView mVideoSizeTv;
    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.publish_moment_activity);
        sureTv = (TextView) findViewById(R.id.sure);
        sureTv.setOnClickListener(this);
        coverImg = (ImageView) findViewById(R.id.cover_image);
        editCoverTv = (TextView) findViewById(R.id.edit_cover_tv);
        editVideoTv = (TextView) findViewById(R.id.edit_video_tv);
        editCoverTv.setOnClickListener(this);
        editVideoTv.setOnClickListener(this);
        momentTextEt = (EditText) findViewById(R.id.monet_text_et);
        mTimeTv = (TextView) findViewById(R.id.time_tv);
        mVideoSizeTv = (TextView) findViewById(R.id.size_tv);
    }

    public static void startPublishMomentActivity(Context context,String videoPath,int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, PublishMomentActivity.class);
            intent.putExtra(VIDEO_PATH,videoPath);
            if(context instanceof Activity){
                ((Activity) context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void initData() {
        super.initData();
        videoPath = getIntent().getStringExtra(VIDEO_PATH);
        picPath = getIntent().getStringExtra(PIC_PATH);
        if(TextUtils.isEmpty(videoPath)){
            finish();
            return;
        }
        File file = new File(videoPath);
        int fSize = 0;
        if (file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                fSize = fis.available();
                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String fileSize = fSize/1024/1024+"MB";
        mVideoSizeTv.setText(fileSize);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getVideoThumb(500,500);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(sureTv)){
            setMoment();
        }else if(v.equals(editVideoTv)){
            Bundle bundle = new Bundle();
            bundle.putString("cut_video_source_path",videoPath);
            VideoCutActivity.startVideoCutActivity(this,bundle, RequestCode.REQ_FOR_CUT_VIDEO);
        }else if(v.equals(editCoverTv)){
            if(TextUtils.isEmpty(picPath)){
                choosePic();
            }else {
                ClipPictureActivity.launchForResult(this, REQUEST_CODE_FOR_ClIP_PIC, null, 1000, 1000, 1f,picPath);
            }
        }
    }

    public void choosePic(){
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, CompleteProfileUploadVideoActivity.REQUEST_CODE_FOR_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CompleteProfileUploadVideoActivity.REQUEST_CODE_FOR_PIC){
            if(RESULT_OK == resultCode){
                Uri selectedVideo = data.getData();
                picPath = ImageUtil.getRealFilePath(this,selectedVideo);
                ClipPictureActivity.launchForResult(this, REQUEST_CODE_FOR_ClIP_PIC, data, 1000, 1000, 1f,picPath);
            }
        }else if(requestCode == RequestCode.REQ_FOR_CUT_VIDEO){
            if(resultCode == RESULT_OK){
                if(data != null){
                    String videoPath = data.getStringExtra(VIDEO_PATH);
                    if(!TextUtils.isEmpty(videoPath)){
                        this.videoPath = videoPath;
                    }
                }
            }
        } else if(requestCode == REQUEST_CODE_FOR_ClIP_PIC){
            if(resultCode == RESULT_OK && data != null) {
                String picPath = data.getStringExtra(ClipPictureActivity.INTENT_OUT_FILE_NAME);
                Log.i("dx", picPath);
                if(!TextUtils.isEmpty(picPath)) {
                    this.picPath = picPath;
                    Glide.with(this).load(picPath).into(coverImg);
                }
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void getVideoThumb(final int picWidth,final int picHeight){
        task = new AsyncTask<String,Void,ExtractInfo>(){
            @Override
            protected ExtractInfo doInBackground(String... params) {
                ExtractInfo info = new ExtractInfo();
                mediaMetadataRetriever.setDataSource(params[0]);
                final int thumbWidth = picWidth;
                final int thumbHeight = picHeight;
                String duration = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                try {
                    info.duration = ViewUtil.parseTimeToString(Integer.parseInt(duration));
                }catch (Exception e){
                    e.printStackTrace();
                }
                Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_NEXT_SYNC);
                try {
                    bitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, false);
                 } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaMetadataRetriever.release();
                if(bitmap != null) {
                    File file = new File(PathUtil.getTemplateCachePath(), PathUtil.getNameByTime() + ".png");
                    ImageUtil.saveBitmapFile2(bitmap, file);
                    info.path = file.getAbsolutePath();
                }

                return info;
            }

            @Override
            protected void onPostExecute(ExtractInfo extractInfo) {
                if(extractInfo!=null) {
                    if(!TextUtils.isEmpty(extractInfo.path)) {
                        picPath = extractInfo.path;
                        Glide.with(PublishMomentActivity.this).load(picPath).into(coverImg);
                    }
                    if(!TextUtils.isEmpty(extractInfo.duration)) {
                        mTimeTv.setText(extractInfo.duration);
                    }
                }
            }
        };
        task.execute(videoPath);
    }


    private void setMoment(){
        if(TextUtils.isEmpty(momentTextEt.getText().toString())){
            showToast("请填写动态");
            return;
        }
        showProgressDialog("正在发布请稍后");
        RequestUtils.uploadFile(NetServiceMap.SendMoment.getHostPath() + NetServiceMap.SendMoment.getApi(),momentTextEt.getText().toString(), videoPath, picPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                if(e != null) {
                    Log.i("dxup", "error" + e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("dxup", response.message() + " , body " + str);
                    if(str.contains("200")){
                        setResult(RESULT_OK);
                        finish();
                    }
                } else {
                    Log.i("dxup" ,response.message() + " error : body " + response.body().string());
                }
            }
        });
    }

    private class ExtractInfo{
        String path;
        String duration;
    }
}
