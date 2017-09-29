package com.dsgly.bixin.biz.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjsonex.JSON;
import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.CompleteProfilePresenter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.responseResult.BaseResult;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.ImageUtil;
import com.dsgly.bixin.utils.UCUtils;
import com.tencent.qcloud.ui.CircleImageView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfileActivity extends BaseActivity {
    public static int REQUEST_ALBUM_CODE = 0x1000;
    public static int REQUEST_CODE_FOR_ClIP_PIC = 0x1001;
    public static int REQUEST_CODE_FOR_COMPLETE_VIDEO_AND_PHOTO = 0x1002;
    private CompleteProfilePresenter presenter;

    public EditText mNicknameEt;
    public TextView mGenderTv;
    public TextView mDateTextView;
    public TextView mHeightTextView;
    public EditText mSchoolView;
    public EditText mDescView;
    public EditText mIdealPartnerView;
    public LinearLayout mChooseAvatarLl;
    public Button mNextBtn;
    public ImageView mUserAvatar;
    public CircleImageView mCicleImageV;

    public static void startCompleteProfileActivity(Context context,int requestCode){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, CompleteProfileActivity.class);
            if(context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent,requestCode);
            }else {
                context.startActivity(intent);
            }
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
        mNicknameEt = (EditText) findViewById(R.id.et_nickname);
        mGenderTv = (TextView) findViewById(R.id.et_gender);
        mGenderTv.setOnClickListener(this);
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
        findViewById(R.id.back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        UserInfo userInfo = UCUtils.getInstance().getUserInfo();
        if(userInfo != null){
            mNicknameEt.setText(userInfo.nickName);
            mGenderTv.setText(userInfo.gender == 1 ? "男" : (userInfo.gender == 2 ? "女" : null));
            mHeightTextView.setText(String.valueOf(userInfo.height));
            mSchoolView.setText(userInfo.college);
            mSchoolView.setTag(userInfo.collegeId);
            if(!TextUtils.isEmpty(userInfo.headImgThumbUrl)) {
                Glide.with(this).load(userInfo.headImgThumbUrl).into(mCicleImageV);
                mCicleImageV.setVisibility(View.VISIBLE);
                mUserAvatar.setVisibility(View.GONE);
            }
            mDescView.setText(userInfo.description);
            mIdealPartnerView.setText(userInfo.idealPartnerDescription);
            mDateTextView.setText(userInfo.birthYear+"-"+userInfo.birthMonth+"-"+userInfo.birthDay);
        }
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
        } else if (v.equals(mGenderTv)) {
            presenter.showGenderPicker();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
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
                String hostPath = NetServiceMap.UploadUserAvatar.getHostPath() + NetServiceMap.UploadUserAvatar.getApi();
                RequestUtils.uploadUserAvatar(hostPath, picPath, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("dxavatar","failed");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response != null && response.body()!=null) {
                            String body = response.body().string();
                            Log.i("dxavatar", body);
                            BaseResult result = JSON.parseObject(body,BaseResult.class);
                            if(result != null && "200".equals(result.code)){
                            }
                        }
                    }
                });


            }
        }else if(requestCode == REQUEST_CODE_FOR_COMPLETE_VIDEO_AND_PHOTO){
            setResult(RESULT_OK);
            finish();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
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
