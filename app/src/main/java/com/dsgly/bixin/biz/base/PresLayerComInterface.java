package com.dsgly.bixin.biz.base;

import android.view.View;

/**
 * 逻辑处理层公有功能
 * Created by bjdengxuan1 on 2017/6/29.
 */

public interface PresLayerComInterface {
    /**
     * 弹出消息
     *
     * @param msg
     */
    public void showToast(String msg);

    /**
     * 网络请求加载框
     */
    public void showProgressDialog();

    /**
     * 隐藏网络请求加载框
     */
    public void hideProgressDialog();

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    public void showSoftKeyboard(View focusView);

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard();
}
