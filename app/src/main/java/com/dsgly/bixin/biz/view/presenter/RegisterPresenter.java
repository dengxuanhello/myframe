package com.dsgly.bixin.biz.view.presenter;

import android.os.Build;
import android.util.Log;

import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.RegisterActivity;
import com.dsgly.bixin.constant.GlobalEnv;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.RegistParam;
import com.dsgly.bixin.net.requestParam.SendVcodeParam;
import com.dsgly.bixin.net.responseResult.RegistResult;
import com.dsgly.bixin.utils.RSAUtils;

/**
 * Created by dengxuan on 2017/7/29.
 */

public class RegisterPresenter extends BasePresenter<RegisterActivity> {

    public void doRegist(){
        getPKey(mvpView.mPhoneEt.getText().toString(),mvpView);
    }

    public void sendVcode(String phone){
        SendVcodeParam param = new SendVcodeParam();
        param.phone = phone;
        NetworkParam networkParam = new NetworkParam(mvpView);
        networkParam.param = param;
        networkParam.key = NetServiceMap.SendVCodeMap;
        RequestUtils.startRequest(networkParam);
    }

    public void onMsgSearchComplete(NetworkParam param){
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.GetPKeyServiceMap){
            String pkey = param.originResponseBody;
            RegistParam registParam = new RegistParam();
            registParam.phone = mvpView.mPhoneEt.getText().toString();
            registParam.appVersion= GlobalEnv.getVersionName();
            registParam.phoneModel= Build.MODEL;
            registParam.code = mvpView.mVcodeEt.getText().toString();
            pkey = pkey.replace("\r","").replace("\n","").replace("\t","");
            registParam.password = RSAUtils.encrypt(pkey,System.currentTimeMillis() + mvpView.mPwdEt.getText().toString());
            NetworkParam networkParam = new NetworkParam(mvpView);
            networkParam.key = NetServiceMap.RegistServiceMap;
            networkParam.param = registParam;
            RequestUtils.startRequest(networkParam);
        }else if(param.key == NetServiceMap.SendVCodeMap){
            if(param.baseResult == null){
                return;
            }
            if("200".equals(param.baseResult.code)){
                mvpView.showToast("短信验证码已发送");
            }else {
                mvpView.showToast(param.baseResult.msg);
            }
        }else if(param.key == NetServiceMap.RegistServiceMap){
            if(param.baseResult != null && param.baseResult instanceof RegistResult) {
                if ("200".equals(param.baseResult.code)) {
                    mvpView.showToast("注册成功");
                } else {
                    mvpView.showToast(((RegistResult)param.baseResult).msg);
                }
            }else {
                mvpView.showToast("网络错误，请重试");
            }
        }
    }
}
