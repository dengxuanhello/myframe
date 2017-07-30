package com.dsgly.bixin.biz.base;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.view.LoginActivity;
import com.dsgly.bixin.constant.Config;
import com.dsgly.bixin.net.RequestUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;

/**
 * Created by dengxuan on 2017/7/30.
 */

public class SplashActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                disPatchLogic();
            }
        },1500);
    }

    @Override
    public void initViews() {
        setContentView(R.layout.splash_layout);
    }

    private void initComponents(){
        RequestUtils.initOkhttp();
        //注册信鸽服务
        // 开启logcat输出，方便debug，发布时请关闭
        XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushManager.registerPush(this, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.i("dx","注册成功，设备token为：" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.i("dx","xinge faile"+o+s);
            }
        });
        initQQImSDK();
    }

    private void initQQImSDK(){
        //初始化IMSDK
        InitBusiness.start(getApplicationContext(),3);
        LoginBusiness.loginIm("dengxuan1", Config.QQ_SDK_SIG, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                Log.i("dx",s);
            }

            @Override
            public void onSuccess() {
                Log.i("dx","login success");
                MessageEvent.getInstance();
            }
        });

        /*Foreground.init(this);
        TIMSdkConfig config = new TIMSdkConfig(Config.QQ_IMSDK_SDK_APPID);
        config.enableLogPrint(true)
                .setLogLevel(TIMLogLevel.INFO);
        TIMManager.getInstance().init(this,config);
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }*/
    }

    private void disPatchLogic(){
        Intent intent = new Intent();
        intent.setClass(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
