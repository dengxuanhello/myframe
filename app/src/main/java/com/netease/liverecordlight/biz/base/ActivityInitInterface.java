package com.netease.liverecordlight.biz.base;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public interface ActivityInitInterface {
    /**
     * 初始化布局组件
     */
    public void initViews();

    /**
     * 增加按钮点击事件
     */
    void initListeners();

    /**
     * 初始化数据
     */
    public void initData();

}
