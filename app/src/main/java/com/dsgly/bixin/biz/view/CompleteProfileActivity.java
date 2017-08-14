package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.CompleteProfilePresenter;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.utils.ImageUtil;
import com.tencent.qcloud.ui.CircleImageView;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfileActivity extends BaseActivity {
    public static int REQUEST_ALBUM_CODE = 0x1000;
    public static int REQUEST_CODE_FOR_ClIP_PIC = 0x1001;
    private CompleteProfilePresenter presenter;

    public TextView mDateTextView;
    public TextView mHeightTextView;
    public EditText mSchoolView;
    public EditText mDescView;
    public EditText mIdealPartnerView;
    public LinearLayout mChooseAvatarLl;
    public Button mNextBtn;
    public ImageView mUserAvatar;
    public CircleImageView mCicleImageV;

    public static void startCompleteProfileActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, CompleteProfileActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void createPresenter() {
        presenter = new CompleteProfilePresenter();
        presenter.attachView(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.complete_profile_activity);
        mDateTextView = (TextView) findViewById(R.id.date_info_tv);
        mDateTextView.setOnClickListener(this);
        mHeightTextView = (TextView) findViewById(R.id.height_choose_tv);
        mHeightTextView.setOnClickListener(this);
        mChooseAvatarLl = (LinearLayout) findViewById(R.id.choose_avatar);
        mChooseAvatarLl.setOnClickListener(this);
        mUserAvatar = (ImageView) findViewById(R.id.user_avatar);
        mCicleImageV = (CircleImageView) findViewById(R.id.cicleImageV);
        mNextBtn = (Button) findViewById(R.id.complete_next);
        mSchoolView = (EditText) findViewById(R.id.gratuated_school);
        mDescView = (EditText) findViewById(R.id.self_introduce);
        mIdealPartnerView = (EditText) findViewById(R.id.idealPartnerDescription);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDateTextView)){
            presenter.showDatePicker();
        }else if(v.equals(mHeightTextView)){
            presenter.showHeightPicker();
        }else if(v.equals(mChooseAvatarLl)){
            presenter.choosePic();
        }else if(v.equals(mNextBtn)){
            presenter.updateUserInfo();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ALBUM_CODE){
            if(RESULT_OK == resultCode){
                Uri selectedVideo = data.getData();
                String fileName = ImageUtil.getRealFilePath(this,selectedVideo);
                ClipPictureActivity.launchForResult(this, REQUEST_CODE_FOR_ClIP_PIC, data, 1000, 1000, 1f,fileName);
            }
        }else if(requestCode == REQUEST_CODE_FOR_ClIP_PIC){
            if (data != null) {
                String picPath = data.getStringExtra(ClipPictureActivity.INTENT_OUT_FILE_NAME);
                Log.i("dx",picPath);
                Glide.with(this).load(picPath).into(mCicleImageV);
                mCicleImageV.setVisibility(View.VISIBLE);
                mUserAvatar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        presenter.onMsgSearchComplete(param);
    }

    @Override
    public void onNetError(NetworkParam param) {
        super.onNetError(param);
        showToast("网络请求失败");
    }
}
