package com.netease.liverecordlight.biz.view.presenter;

import android.content.Intent;
import android.util.Log;
import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BasePresenter;
import com.netease.liverecordlight.biz.view.LoginActivity;
import com.netease.liverecordlight.biz.view.RegisterActivity;
import com.netease.liverecordlight.net.NetServiceMap;
import com.netease.liverecordlight.net.NetworkParam;
import com.netease.liverecordlight.net.RequestUtils;
import com.netease.liverecordlight.net.requestParam.GetPKeyParam;
import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by dengxuan on 2017/7/22.
 */

public class LoginPresenter extends BasePresenter<LoginActivity>{

    private void getPKey(String phone){
        NetworkParam param = new NetworkParam(mvpView);
        param.key = NetServiceMap.GetPKeyServiceMap;
        GetPKeyParam getPKeyParam = new GetPKeyParam();
        getPKeyParam.phone = phone;
        param.param = getPKeyParam;
        RequestUtils.startGetRequest(param);
    }

    public void doLogin(String phone){
        getPKey(phone);
    }

    public void goRegist(){
        Intent intent = new Intent();
        intent.setClass(mvpView,RegisterActivity.class);
        mvpView.startActivity(intent);
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
        thirdAuth.showUser(null);
    }

    public void onMsgSearchComplete(NetworkParam param) {
        if(param!=null && param.key == NetServiceMap.GetPKeyServiceMap){

        }
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
