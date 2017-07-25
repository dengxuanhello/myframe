package com.netease.liverecordlight.net;


import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.netease.liverecordlight.net.requestParam.BaseParam;
import com.netease.liverecordlight.net.responseResult.BaseResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class NetworkParam{


    public boolean block;
    public boolean cancelAble;
    public BaseParam param;
    public BaseResult baseResult;
   // public Handler handler;
    public String progressMessage;
    public Callback callback;
    public NetworkListener networkListener;
    public String originResponseBody;
    public NetServiceMap key;

    private NetworkParam(){}

    public NetworkParam(final NetworkListener listener){
        this.networkListener = listener;
        this.callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(networkListener!=null){
                    networkListener.onNetError(NetworkParam.this);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response != null && response.body() != null){
                    originResponseBody = response.body().string();
                    if(!TextUtils.isEmpty(originResponseBody) && key != null) {
                        Log.i("network",originResponseBody);
                        try {
                            baseResult = new Gson().fromJson(originResponseBody, key.getResultClz());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (networkListener != null) {
                            networkListener.onMsgSearchComplete(NetworkParam.this);
                        }
                    }
                }
            }
        };
    }
}
