package com.netease.liverecordlight.biz.view.presenter;

import com.netease.liverecordlight.biz.base.BasePresenter;
import com.netease.liverecordlight.biz.view.LoginActivity;
import com.netease.liverecordlight.net.NetServiceMap;
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
        networkParam.key = NetServiceMap.LoginServiceMap;
        RequestUtils.startRequest(networkParam);
    }

    public void doLogin(){

    }

    public void onMsgSearchComplete(NetworkParam param) {

    }

}
