package com.netease.liverecordlight;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.netease.liverecordlight.QQIM.utils.Foreground;
import com.netease.liverecordlight.constant.Config;
import com.netease.liverecordlight.utils.CrashHandler;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;
/*
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
*/


/**
 * Created by bjdengxuan1 on 2017/6/21.
 */

public class APP extends Application{
    private static APP INSTANCE;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        CrashHandler mCustomCrashHandler = CrashHandler.getInstance();
        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
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
        //initQqImsdk();
        initQQImSDK2();
    }
    public static APP getApp() {
        return INSTANCE;
    }
    public static Context getContext() {
        return INSTANCE;
    }

    /*private void initQqImsdk(){
        TIMSdkConfig config = new TIMSdkConfig(Config.QQ_IMSDK_SDK_APPID);
        config.enableLogPrint(true)
                .setLogLevel(TIMLogLevel.INFO);
        TIMManager.getInstance().init(this,config);
    }*/

    private void initQQImSDK2(){
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
}
