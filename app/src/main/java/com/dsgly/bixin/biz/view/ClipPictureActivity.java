package com.dsgly.bixin.biz.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;


import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.utils.ImageUtil;
import com.dsgly.bixin.utils.PathUtil;
import com.dsgly.bixin.wigets.ClipImageLayout;

import java.io.File;

public class ClipPictureActivity extends BaseActivity {
    private ClipImageLayout mClipView;

    private Intent mBitmapData;
    private int mOrientation;
    private int mMaxWidth, mMaxHeight;
    public static final String INTENT_OUT_FILE_NAME = "out_bitmap";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_picture);
        findViewById(R.id.btSave).setOnClickListener(this);
        findViewById(R.id.cancel_action).setOnClickListener(this);
        mClipView = (ClipImageLayout) this.findViewById(R.id.clip_image);
        mOrientation = getIntent().getIntExtra("orientation", ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mBitmapData = getIntent().getParcelableExtra("data");
        mMaxWidth = getIntent().getIntExtra("maxWidth", 1000);
        mMaxHeight = getIntent().getIntExtra("maxHeight", 1000);
        mMaxHeight = getIntent().getIntExtra("maxHeight", 1000);
        mClipView.setScale(getIntent().getFloatExtra("scale", 1));
        switch (mOrientation) {
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
        setClipBitmap();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.cancel_action){
            finish();
        }else if(v.getId() == R.id.btSave){
            saveAndBack();
        }
    }

    private void setClipBitmap() {
        String imagePath = getIntent().getStringExtra("filePath");
        Bitmap selectedImage = BitmapFactory.decodeFile(imagePath);
        if (selectedImage == null) {
            showToast("图片选取失败");
            return;
        }
        mClipView.setEditImageBitmap(selectedImage);
    }

    private void saveAndBack() {
        //AlertUtil.showProgressDialog(this, R.string.please_wait);
        showProgressDialog("正在处理...");
        Bitmap clipBitmap = mClipView.clip();
        new AsyncTask<Bitmap, Void, String>() {
            @Override
            protected String doInBackground(Bitmap... params) {
                File file = new File(PathUtil.getTemplateCachePath(), PathUtil.getNameByTime() + ".png");
                //原始图片可能太大，缩小一下,300X300足够用了
                Bitmap scaleBitmap = ImageUtil.scaleBitmap(params[0], mMaxWidth, mMaxHeight);
                ImageUtil.saveBitmapFile2(scaleBitmap, file);
                return file.getAbsolutePath();
            }

            @Override
            protected void onPostExecute(String filePath) {
                super.onPostExecute(filePath);
                closeProgressDialog();
                Intent intent = new Intent();
                intent.putExtra(INTENT_OUT_FILE_NAME, filePath);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }.execute(clipBitmap);
    }

    /**
     * 从activity启动
     *
     * @param activity
     * @param requestCode
     * @param data
     * @param maxWidth
     * @param maxHeight
     * @param scale
     */
    public static void launchForResult(Activity activity, int requestCode, Intent data, int maxWidth, int maxHeight, float scale,String filePath) {
        Intent intent = new Intent(activity, ClipPictureActivity.class);
        intent.putExtra("maxWidth", maxWidth);
        intent.putExtra("maxHeight", maxHeight);
        intent.putExtra("data", data);
        intent.putExtra("orientation", activity.getRequestedOrientation());
        intent.putExtra("scale", scale);
        intent.putExtra("filePath",filePath);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 从fragment启动
     *
     * @param activity
     * @param fragment
     * @param requestCode
     * @param data
     * @param maxWidth
     * @param maxHeight
     * @param scale       宽度和高度之比
     */
    public static void launchForResult(Activity activity, Fragment fragment, int requestCode, Intent data, int maxWidth, int maxHeight, float scale) {
        Intent intent = new Intent(activity, ClipPictureActivity.class);
        intent.putExtra("maxWidth", maxWidth);
        intent.putExtra("maxHeight", maxHeight);
        intent.putExtra("data", data);
        intent.putExtra("orientation", activity.getRequestedOrientation());
        intent.putExtra("scale", scale);
        fragment.startActivityForResult(intent, requestCode);
    }
}