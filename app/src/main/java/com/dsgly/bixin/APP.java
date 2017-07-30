package com.dsgly.bixin;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.mob.MobApplication;
import com.dsgly.bixin.constant.Config;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.utils.CrashHandler;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;

/**
 * Created by bjdengxuan1 on 2017/6/21.
 */

public class APP extends MobApplication {
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
    }
    public static APP getApp() {
        return INSTANCE;
    }
    public static Context getContext() {
        return INSTANCE;
    }
}
