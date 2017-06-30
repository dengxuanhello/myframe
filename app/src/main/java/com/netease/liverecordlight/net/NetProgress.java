package com.netease.liverecordlight.net;

/**
 * Created by bjdengxuan1 on 2017/6/22.
 */

public interface NetProgress {
    void onProgress(float progress);
    void onComplete();
}
