package com.dsgly.bixin.biz.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.RegisterPresenter;
import com.dsgly.bixin.net.NetworkParam;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class RegisterActivity extends BaseActivity {

    private RegisterPresenter presenter;

    public EditText mPhoneEt;
    public EditText mVcodeEt;
    public EditText mPwdEt;

    public Button registBtn;
    public TextView getVcodeBtn;

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.user_reg_activity);
        mPhoneEt = (EditText) findViewById(R.id.user_account);
        mVcodeEt = (EditText) findViewById(R.id.user_verify_code);
        mPwdEt = (EditText) findViewById(R.id.user_pwd);
        registBtn = (Button) findViewById(R.id.btn_register);
        getVcodeBtn = (TextView) findViewById(R.id.get_verify_code);
        registBtn.setOnClickListener(this);
        getVcodeBtn.setOnClickListener(this);
        mPhoneEt.setText("13521763794");
    }

    @Override
    public void createPresenter() {
        presenter = new RegisterPresenter();
        presenter.attachView(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(registBtn)){
            if(!TextUtils.isEmpty(mPhoneEt.getText().toString())
                    &&!TextUtils.isEmpty(mVcodeEt.getText().toString())
                    &&!TextUtils.isEmpty(mPwdEt.getText().toString())) {
                presenter.doRegist();
            }else {
                showToast("请输入手机号,短信验证码和密码");
            }
        }else if(v.equals(getVcodeBtn)){
            if(!TextUtils.isEmpty(mPhoneEt.getText().toString())) {
                presenter.sendVcode(mPhoneEt.getText().toString());
            }else {
                showToast("请输入手机号");
            }
        }
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        presenter.onMsgSearchComplete(param);
    }
}
