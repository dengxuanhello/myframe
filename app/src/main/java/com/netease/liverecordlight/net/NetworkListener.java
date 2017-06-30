package com.netease.liverecordlight.net;

/**
 * Created by bjdengxuan1 on 2017/6/19.
 */

public interface NetworkListener {
    void onNetStart(NetworkParam param);
    void onNetEnd(NetworkParam param);
    void onNetError(NetworkParam param);
    void onNetCancel(NetworkParam param);
    void onMsgSearchComplete(NetworkParam param);
}
