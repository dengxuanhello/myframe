package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.LoginPresenter;
import com.dsgly.bixin.constant.GlobalEnv;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.ChangePwdParam;
import com.dsgly.bixin.net.requestParam.SendVcodeParam;
import com.dsgly.bixin.net.responseResult.GetPhoneResult;
import com.dsgly.bixin.utils.CommonUtils;

import java.util.HashMap;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class UpdatePwdActivity extends BaseActivity {

    private EditText mPhoneEt;
    private EditText mVcodeEt;
    private Button mConfirmBtn;
    private TextView mGetVcodeTv;
    private EditText mNewPwdEt;

    public static void startUpdatePwdActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, UpdatePwdActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.update_pwd_activity);
        mPhoneEt = (EditText) findViewById(R.id.user_phone);
        mVcodeEt = (EditText) findViewById(R.id.user_verify_code);
        mNewPwdEt = (EditText) findViewById(R.id.user_pwd);
        mConfirmBtn = (Button) findViewById(R.id.btn_confirm);
        mGetVcodeTv = (TextView) findViewById(R.id.get_verify_code);
        mConfirmBtn.setOnClickListener(this);
        mGetVcodeTv.setOnClickListener(this);
        findViewById(R.id.back_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        getPhone();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mConfirmBtn)){
            if(!TextUtils.isEmpty(mPhoneEt.getText())
                    &&!TextUtils.isEmpty(mVcodeEt.getText())
                    &&!TextUtils.isEmpty(mNewPwdEt.getText())){
                updatePwd(mPhoneEt.getText().toString(),mVcodeEt.getText().toString(),mNewPwdEt.getText().toString());
            }else {
                showToast("请输入手机号，验证码和新密码");
            }
        }else if(v.equals(mGetVcodeTv)){
            if(!TextUtils.isEmpty(mPhoneEt.getText())){
                sendVcode(mPhoneEt.getText().toString());
            }else {
                showToast("请输入手机号");
            }
        }
    }

    private void sendVcode(String phone){
        SendVcodeParam param = new SendVcodeParam();
        param.phone = phone;
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.param = param;
        networkParam.key = NetServiceMap.SendVCodeMap;
        RequestUtils.startRequest(networkParam);
    }

    private void updatePwd(String phone,String vcode,String pwd){
        ChangePwdParam changePwdParam = new ChangePwdParam();
        changePwdParam.appVersion = GlobalEnv.getVersionName();
        changePwdParam.code = vcode;
        changePwdParam.newPassword = pwd;
        changePwdParam.phoneModel = Build.MODEL;
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.param = changePwdParam;
        networkParam.key = NetServiceMap.ChangePwd;
        networkParam.headers = new HashMap<String, String>();
        networkParam.progressMessage = "正在修改密码...";
        networkParam.headers.put(LoginPresenter.TOKEN_PARAM, CommonUtils.createRandom(false,16));
        RequestUtils.startRequest(networkParam);
    }

    private void getPhone(){
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.key = NetServiceMap.PHONE;
        RequestUtils.startGetRequest(networkParam);
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.PHONE){
            if(param.baseResult == null){
                return;
            }
            if("200".equals(param.baseResult.code)
                    && param.baseResult instanceof  GetPhoneResult){
                GetPhoneResult result = (GetPhoneResult) param.baseResult;
                if(result.data != null) {
                    mPhoneEt.setText(result.data.phone);
                    mPhoneEt.setEnabled(false);
                }
            }
        }else if(param.key == NetServiceMap.ChangePwd){
            if(param.baseResult == null){
                return;
            }
            if("200".equals(param.baseResult.code)){
                showToast("修改成功");
                finish();
            }
        }else if(param.key == NetServiceMap.SendVCodeMap){
            if(param.baseResult == null){
                return;
            }
            if("200".equals(param.baseResult.code)){
                showToast("验证码已发送");
            }
        }
    }
}
