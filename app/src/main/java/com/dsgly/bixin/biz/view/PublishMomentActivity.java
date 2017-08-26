package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.utils.ImageUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dengxuan on 2017/8/23.
 */

public class PublishMomentActivity extends BaseActivity {
    private static final String VIDEO_PATH = "video_path";
    private static final String PIC_PATH = "pic_path";

    private String videoPath;
    private String picPath;

    private TextView sureTv;
    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.publish_moment_activity);
        sureTv = (TextView) findViewById(R.id.sure);
        sureTv.setOnClickListener(this);
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
        choosePic();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CompleteProfileUploadVideoActivity.REQUEST_CODE_FOR_PIC){
            if(RESULT_OK == resultCode){
                Uri selectedVideo = data.getData();
                picPath = ImageUtil.getRealFilePath(this,selectedVideo);
                //ClipPictureActivity.launchForResult(this, REQUEST_CODE_FOR_ClIP_PIC, data, 1000, 1000, 1f,fileName);
            }
        }
    }
}
