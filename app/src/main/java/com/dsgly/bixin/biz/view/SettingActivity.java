package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.utils.UCUtils;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class SettingActivity extends BaseActivity {

    private LinearLayout mBindPhoneLl;
    private LinearLayout mUpdatePwdLl;
    private LinearLayout mRemoveChcheLl;
    private LinearLayout mHelpAndFeedbackLl;
    private Button mExitBtn;

    public static void startSettingActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, SettingActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.setting_actvity);
        mBindPhoneLl = (LinearLayout) findViewById(R.id.bind_phone_ll);
        mBindPhoneLl.setOnClickListener(this);
        mUpdatePwdLl = (LinearLayout) findViewById(R.id.update_pwd_ll);
        mUpdatePwdLl.setOnClickListener(this);
        mRemoveChcheLl = (LinearLayout) findViewById(R.id.remove_cache_ll);
        mRemoveChcheLl.setOnClickListener(this);
        mHelpAndFeedbackLl = (LinearLayout) findViewById(R.id.help_and_feedback_ll);
        mHelpAndFeedbackLl.setOnClickListener(this);
        mExitBtn = (Button) findViewById(R.id.user_exit);
        mExitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mBindPhoneLl)){
            BindPhoneActivity.startBindPhoneActivity(this);
        }else if(v.equals(mUpdatePwdLl)){
            UpdatePwdActivity.startUpdatePwdActivity(this);
        }else if(v.equals(mRemoveChcheLl)){
            showToast("清除成功");
        }else if(v.equals(mHelpAndFeedbackLl)){

        }else if(v.equals(mExitBtn)){
            UCUtils.getInstance().removeUserInfo();
            goLoginPage();
        }
    }

    private void goLoginPage(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
