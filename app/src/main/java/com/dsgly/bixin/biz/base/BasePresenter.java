package com.dsgly.bixin.biz.base;

import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkListener;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.GetPKeyParam;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public abstract class BasePresenter<V extends IMvpView> implements Presenter<V>{
    protected V mvpView;
    @Override
    public void attachView(V view) {
        mvpView = view;
    }

    @Override
    public void detachView(V view) {
        mvpView = null;
    }

    public void getPKey(String phone, NetworkListener listener){
        NetworkParam param = new NetworkParam(listener);

        param.key = NetServiceMap.GetPKeyServiceMap;
        GetPKeyParam getPKeyParam = new GetPKeyParam();
        getPKeyParam.phone = phone;
        param.param = getPKeyParam;
        RequestUtils.startGetRequest(param);
    }
}
