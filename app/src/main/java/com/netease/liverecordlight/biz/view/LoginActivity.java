package com.netease.liverecordlight.biz.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mob.tools.utils.UIHandler;
import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseActivity;
import com.netease.liverecordlight.biz.view.presenter.LoginPresenter;
import com.netease.liverecordlight.net.NetworkParam;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class LoginActivity extends BaseActivity {
    private EditText mAccountEt;
    private EditText mPwdEt;
    private Button mLoginBtn;
    private Button mRegistBtn;
    private TextView mForgetPwdTv;
    private ImageView mWXThirdLogin;
    private ImageView mWBThirdLogin;
    private ImageView mQQThirdLogin;

    private LoginPresenter presenter;

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.login_activity_layout);
        mAccountEt = (EditText) findViewById(R.id.user_account);
        mPwdEt = (EditText) findViewById(R.id.user_pwd);
        mLoginBtn = (Button) findViewById(R.id.user_login);
        mRegistBtn = (Button) findViewById(R.id.user_regist);
        mForgetPwdTv = (TextView) findViewById(R.id.forget_pwd_textview);
        mWXThirdLogin = (ImageView) findViewById(R.id.weixin_login_img);
        mWBThirdLogin = (ImageView) findViewById(R.id.weibo_login_img);
        mQQThirdLogin = (ImageView) findViewById(R.id.qq_login_img);
        mLoginBtn.setOnClickListener(this);
        mRegistBtn.setOnClickListener(this);
        mForgetPwdTv.setOnClickListener(this);
        mWXThirdLogin.setOnClickListener(this);
        mWBThirdLogin.setOnClickListener(this);
        mQQThirdLogin.setOnClickListener(this);
    }

    @Override
    public void createPresenter() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }



    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        presenter.onMsgSearchComplete(param);
    }

    private void showShare() {
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
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mLoginBtn)){
            presenter.doLogin();
        }else if(v.equals(mRegistBtn)){
            goRegist();
        }else if(v.equals(mForgetPwdTv)){
            VideoPlayActivity.startVideoPlay(this,VideoPlayActivity.TEST_RUL);
        }else if(v.equals(mWXThirdLogin)){
            authorizeWx();
        }else if(v.equals(mWBThirdLogin)){

        }else if(v.equals(mQQThirdLogin)){

        }
    }

    private void goRegist(){
        Intent intent = new Intent();
        intent.setClass(this,RegisterActivity.class);
        startActivity(intent);
    }

    private void authorizeWx() {
        Platform wechat= ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {

            }

            @Override
            public void onCancel(Platform platform, int i) {

            }
        });
        wechat.authorize();
    }
}
