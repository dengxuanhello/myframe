package com.netease.liverecordlight.biz.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.netease.liverecordlight.net.NetworkListener;
import com.netease.liverecordlight.net.NetworkParam;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by bjdengxuan1 on 2017/6/23.
 */

public class BaseActivity extends FragmentActivity implements
        NetworkListener,ActivityInitInterface,ActivityJumpInterface,
        PresLayerComInterface,View.OnClickListener,IMvpView, TIMMessageListener {

    private PresLayerComHelper presLayerComHelper;
    public BasePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presLayerComHelper = new PresLayerComHelper(this);
        initViews();
        initListeners();
        initData();
        createPresenter();
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventMainThread(Object event) {

    }

    public void createPresenter(){

    }

    @Override
    public void onNetStart(NetworkParam param) {

    }

    @Override
    public void onNetEnd(NetworkParam param) {

    }

    @Override
    public void onNetError(NetworkParam param) {

    }

    @Override
    public void onNetCancel(NetworkParam param) {

    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void initData() {
        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void showToast(String msg) {
        presLayerComHelper.showToast(msg);
    }

    @Override
    public void showProgressDialog() {
        presLayerComHelper.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        presLayerComHelper.hideSoftKeyboard();
    }

    @Override
    public void showSoftKeyboard(View focusView) {
        presLayerComHelper.showSoftKeyboard(focusView);
    }

    @Override
    public void hideSoftKeyboard() {
        presLayerComHelper.hideSoftKeyboard();
    }

    @Override
    public void startActivity(Class<?> openClass, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void openActivityForResult(Class<?> openClass, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, openClass);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void goBackWithResult(Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) ;
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView(this);
        }
        super.onDestroy();
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        return false;
    }
}
