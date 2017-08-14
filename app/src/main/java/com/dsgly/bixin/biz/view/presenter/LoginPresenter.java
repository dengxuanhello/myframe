package com.dsgly.bixin.biz.view.presenter;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BasePresenter;
import com.dsgly.bixin.biz.view.HomeActivity;
import com.dsgly.bixin.biz.view.LoginActivity;
import com.dsgly.bixin.biz.view.RegisterActivity;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.LoginParam;
import com.dsgly.bixin.net.responseResult.GetUserInfoResult;
import com.dsgly.bixin.net.responseResult.LoginResult;
import com.dsgly.bixin.utils.CommonUtils;
import com.dsgly.bixin.utils.RSAUtils;
import com.dsgly.bixin.utils.UCUtils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by dengxuan on 2017/7/22.
 */

public class LoginPresenter extends BasePresenter<LoginActivity>{

    public static final String TOKEN_PARAM = "token-param";

    public void doLogin(String phone){
        getPKey(phone,mvpView);
    }

    public void goRegist(){
        Intent intent = new Intent();
        intent.setClass(mvpView,RegisterActivity.class);
        mvpView.startActivity(intent);
    }

    public void goHomePage(){

    }

    public void authorizeThirdParty(String name) {
        mvpView.showToast("正在跳转...");
        Platform thirdAuth = ShareSDK.getPlatform(name);
        thirdAuth.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.i("dx",hashMap.size()+"");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.i("dx",throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.i("dx",i+"");
            }
        });
        if (thirdAuth.isAuthValid()){
            thirdAuth.removeAccount(true);
        }
        thirdAuth.showUser(null);
    }

    public void onMsgSearchComplete(NetworkParam param) {
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.GetPKeyServiceMap){
            String pkey = param.originResponseBody;
            LoginParam loginParam = new LoginParam();
            loginParam.phone = "13521763794";
            pkey = pkey.replace("\r","").replace("\n","").replace("\t","");
            loginParam.password = RSAUtils.encrypt(pkey,System.currentTimeMillis() + mvpView.mPwdEt.getText().toString());
            loginParam.appVersion = "1.0.0";
            loginParam.phoneModel = Build.MODEL;
            NetworkParam networkParam = new NetworkParam(mvpView);
            networkParam.key = NetServiceMap.LoginServiceMap;
            networkParam.param = loginParam;
            networkParam.headers = new HashMap<String, String>();
            networkParam.headers.put(TOKEN_PARAM, CommonUtils.createRandom(false,16));
            RequestUtils.startRequest(networkParam);
        }else if(param.key == NetServiceMap.LoginServiceMap){
            LoginResult result = (LoginResult) param.baseResult;
            if(result == null){
                return;
            }
            if("200".equals(result.code)) {
                //TODO save userdata
                UCUtils.mid = result.data.meId;
                //mvpView.showToast();
                //goMainPage();
                getUserInfo();
                //HomeActivity.startHomeActivity(mvpView);
            }else {
                mvpView.showToast(result.msg);
            }
        }else if(param.key == NetServiceMap.GetUSER){
            if(param.baseResult!= null && param.baseResult instanceof GetUserInfoResult) {
                GetUserInfoResult result = (GetUserInfoResult) param.baseResult;
                UCUtils.getInstance().saveUserinfo(result.data);
                UCUtils.getInstance().loginQqIM(result.data.userId);
                goMainPage();
            }
        }
    }

    private void getUserInfo(){
        NetworkParam networkParam = new NetworkParam(mvpView);
        networkParam.key = NetServiceMap.GetUSER;
        networkParam.progressMessage = "登陆成功,正在获取用户信息";
        RequestUtils.startGetRequest(networkParam);
    }

    private void goMainPage(){
        Intent intent = new Intent();
        intent.setClass(mvpView, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mvpView.startActivity(intent);
    }

    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不     调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("share");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(mvpView.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(mvpView);
    }


}
