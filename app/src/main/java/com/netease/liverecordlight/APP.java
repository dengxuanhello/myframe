package com.netease.liverecordlight;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.netease.liverecordlight.utils.CrashHandler;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by bjdengxuan1 on 2017/6/21.
 */

public class APP extends Application{
    private static APP INSTANCE;

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
    }
    public static APP getApp() {
        return INSTANCE;
    }
}
