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

    public void onMsgSearchComplete(NetworkParam param) {
        if(param!=null && param.key == NetServiceMap.GetPKeyServiceMap){

        }
    }

}
