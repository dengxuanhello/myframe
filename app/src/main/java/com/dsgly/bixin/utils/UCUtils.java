package com.dsgly.bixin.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjsonex.JSON;
import com.alibaba.fastjsonex.serializer.SerializerFeature;
import com.dsgly.bixin.APP;
import com.dsgly.bixin.constant.Config;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.storage.CacheUtils;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.MessageEvent;

/**
 * Created by dengxuan on 2017/8/1.
 */

public class UCUtils {
    private static final String USER_DATA = "user_login_data";
    private static UCUtils instance;
    private CacheUtils cacheUtils;

    public static String mid = "";

    public static UCUtils getInstance() {
        if(instance == null){
            instance = new UCUtils();
        }
        return instance;
    }

    private void createCacheUtils(){
        if(cacheUtils == null){
            cacheUtils = new CacheUtils(APP.getContext());
        }
    }

    public void saveUserinfo(UserInfo data){
        createCacheUtils();
        SerializerFeature[] featureArr = { SerializerFeature.WriteClassName };
        String userInfo = JSON.toJSONString(data,featureArr);
        cacheUtils.saveCacheToDisk(USER_DATA,userInfo);
    }

    public boolean isUserValid(){

        return false;
    }

    public UserInfo getUserInfo(){
        createCacheUtils();
        String userInfoStr = cacheUtils.readCacheFromDisk(USER_DATA);
        if(TextUtils.isEmpty(userInfoStr)){
            return null;
        }
        UserInfo userInfo = (UserInfo) JSON.parse(userInfoStr);
        if(userInfo != null){
            return userInfo;
        }
        return null;
    }

    public void removeUserInfo(){
        createCacheUtils();
        cacheUtils.saveCacheToDisk(USER_DATA,"");
    }

    public void loginQqIM(String userid){
        LoginBusiness.loginIm(userid, Config.QQ_SDK_SIG, new TIMCallBack() {
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
    }
}
