package com.dsgly.bixin.biz.view;

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

import java.io.File;
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
    private AsyncTask<String,Void,String> task;
    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
    private TextView sureTv;
    private TextView editCoverTv;
    private TextView editVideoTv;
    private EditText momentTextEt;
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
    }

    public static void startPublishMomentActivity(Context context,String videoPath){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, PublishMomentActivity.class);
            intent.putExtra(VIDEO_PATH,videoPath);
            //intent.putExtra(PIC_PATH,coverPath);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        videoPath = getIntent().getStringExtra(VIDEO_PATH);
        picPath = getIntent().getStringExtra(PIC_PATH);
        //choosePic();
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
        task = new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... params) {

                mediaMetadataRetriever.setDataSource(params[0]);
                final int thumbWidth = picWidth;
                final int thumbHeight = picHeight;
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
                    return file.getAbsolutePath();
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                picPath = s;
                Glide.with(PublishMomentActivity.this).load(picPath).into(coverImg);
            }
        };
        task.execute(videoPath);
    }


    private void setMoment(){
        if(TextUtils.isEmpty(momentTextEt.getText())){
            showToast("请填写动态");
            return;
        }
        RequestUtils.uploadFile(NetServiceMap.SendMoment.getHostPath() + NetServiceMap.SendMoment.getApi(), videoPath, picPath, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(e != null) {
                    Log.i("dxup", "error" + e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String str = response.body().string();
                    Log.i("dxup", response.message() + " , body " + str);

                } else {
                    Log.i("dxup" ,response.message() + " error : body " + response.body().string());
                }
            }
        });
    }
}
