package com.dsgly.bixin.net;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.dsgly.bixin.net.requestParam.BaseParam;
import com.dsgly.bixin.net.responseResult.BaseResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public class NetworkParam{

    private static final int NET_DATA_BACK = 0x1000;
    public static final int NET_SHOW_PROGRESS = 0x1001;
    public static final int NET_CLOSE_PROGRESS = 0x1002;
    public static final int NET_ERROR = 0x1003;

    public boolean block = true;
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

    Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case NET_DATA_BACK:
                    String netData = (String) msg.obj;
                    if(!TextUtils.isEmpty(netData) && key != null) {
                        Log.i("network",originResponseBody);
                        try {
                            baseResult = new Gson().fromJson(netData, key.getResultClz());
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                        if (networkListener != null) {
                            networkListener.onMsgSearchComplete(NetworkParam.this);
                        }
                    }
                    break;
                case NET_SHOW_PROGRESS:
                    if (networkListener != null) {
                        networkListener.onNetStart(NetworkParam.this);
                    }
                    break;
                case NET_CLOSE_PROGRESS:
                    if (networkListener != null) {
                        networkListener.onNetEnd(NetworkParam.this);
                    }
                    break;
                case NET_ERROR:
                    if (networkListener != null) {
                        networkListener.onNetError(NetworkParam.this);
                    }
                    break;
            }

        }
    };

    public NetworkParam(final NetworkListener listener){
        this.networkListener = listener;
        this.progressMessage = "正在请求网络...";
        this.callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(block){
                    handler.sendEmptyMessage(NET_CLOSE_PROGRESS);
                }
                if(networkListener != null){
                    handler.sendEmptyMessage(NET_ERROR);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(block){
                    handler.sendEmptyMessage(NetworkParam.NET_CLOSE_PROGRESS);
                }
                if(response != null && response.body() != null){
                    originResponseBody = response.body().string();
                    Message message = new Message();
                    message.what = NET_DATA_BACK;
                    message.obj = originResponseBody;
                    handler.sendMessage(message);
                }
            }
        };
    }
}
