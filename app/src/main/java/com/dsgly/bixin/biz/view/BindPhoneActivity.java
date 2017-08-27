package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.BindPhoneParam;
import com.dsgly.bixin.net.requestParam.SendVcodeParam;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class BindPhoneActivity extends BaseActivity {

    private EditText mPhoneEt;
    private EditText mVcodeEt;
    private Button mConfirmBtn;
    private TextView mGetVcodeTv;

    public static void startBindPhoneActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, BindPhoneActivity.class);
            context.startActivity(intent);
        }
    }
    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.bind_phon_acitivity);
        mPhoneEt = (EditText) findViewById(R.id.user_phone);
        mVcodeEt = (EditText) findViewById(R.id.user_verify_code);
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
    public void onClick(View v) {
        if(v.equals(mConfirmBtn)){
            if(!TextUtils.isEmpty(mPhoneEt.getText())
                    &&!TextUtils.isEmpty(mVcodeEt.getText())){
                bindPhone(mPhoneEt.getText().toString(),mVcodeEt.getText().toString());
            }else {
                showToast("请输入手机号和验证码");
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

    private void bindPhone(String phone,String vcode){
        BindPhoneParam bindPhoneParam = new BindPhoneParam();
        bindPhoneParam.phone = phone;
        bindPhoneParam.code = vcode;
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.param = bindPhoneParam;
        networkParam.key = NetServiceMap.USERBINDPHONE;
        networkParam.progressMessage="正在绑定...";
        RequestUtils.startRequest(networkParam);
    }
}
