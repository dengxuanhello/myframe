package com.netease.liverecordlight.biz.view.presenter;

import com.netease.liverecordlight.biz.base.BasePresenter;
import com.netease.liverecordlight.biz.view.LoginActivity;
import com.netease.liverecordlight.net.NetworkParam;
import com.netease.liverecordlight.net.RequestUtils;
import com.netease.liverecordlight.net.requestParam.GetPKeyParam;

/**
 * Created by dengxuan on 2017/7/22.
 */

public class LoginPresenter extends BasePresenter<LoginActivity>{

    private void getPKey(String phone){
        GetPKeyParam param = new GetPKeyParam();
        param.phone = phone;
        NetworkParam networkParam = new NetworkParam(this.mvpView);
        networkParam.hostPath = "http://http://139.199.20.201//publicKey";
        RequestUtils.startPostRequest(networkParam);
    }

    public void doLogin(){

    }

    public void onMsgSearchComplete(NetworkParam param) {

    }

}
