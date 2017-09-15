package com.dsgly.bixin.biz.view;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.LoginPresenter;
import com.dsgly.bixin.net.NetworkParam;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class LoginActivity extends BaseActivity {
    public EditText mAccountEt;
    public EditText mPwdEt;
    public Button mLoginBtn;
    private Button mRegistBtn;
    private TextView mForgetPwdTv;
    private ImageView mWXThirdLogin;
//    private ImageView mWBThirdLogin;
//    private ImageView mQQThirdLogin;

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
//        mWBThirdLogin = (ImageView) findViewById(R.id.weibo_login_img);
//        mQQThirdLogin = (ImageView) findViewById(R.id.qq_login_img);
        mLoginBtn.setOnClickListener(this);
        mRegistBtn.setOnClickListener(this);
        mForgetPwdTv.setOnClickListener(this);
        mWXThirdLogin.setOnClickListener(this);
//        mWBThirdLogin.setOnClickListener(this);
//        mQQThirdLogin.setOnClickListener(this);
        mAccountEt.setText("18611598314");
        mPwdEt.setText("123456");
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

    @Override
    public void onClick(View v) {
        if(v.equals(mLoginBtn)){
            if(!TextUtils.isEmpty(mAccountEt.getText())
                    &&!TextUtils.isEmpty(mPwdEt.getText())) {
                presenter.doLogin(mAccountEt.getText().toString());
            }else {
                showToast("输入用户名和密码");
            }
        }else if(v.equals(mRegistBtn)){
            presenter.goRegist();
        }else if(v.equals(mForgetPwdTv)){
            presenter.showShare();
            //VideoPlayActivity.startVideoPlay(this,VideoPlayActivity.TEST_RUL);
        }else if(v.equals(mWXThirdLogin)){
            presenter.authorizeThirdParty(Wechat.NAME);
//        }else if(v.equals(mWBThirdLogin)){
            //VideoRecorderActivity.startVideoRecordActivity(this);
//            presenter.authorizeThirdParty(SinaWeibo.NAME);
//        }else if(v.equals(mQQThirdLogin)){
//            presenter.authorizeThirdParty(QQ.NAME);
        }
    }
}
