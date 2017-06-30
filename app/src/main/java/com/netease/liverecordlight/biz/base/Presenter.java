package com.netease.liverecordlight.biz.base;

/**
 * 业务处理层
 * Created by bjdengxuan1 on 2017/6/29.
 */

public interface Presenter<V> {
    void attachView(V view);
    void detachView(V view);
}
