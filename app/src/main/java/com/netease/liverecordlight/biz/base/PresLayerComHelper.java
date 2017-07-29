package com.netease.liverecordlight.biz.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public class PresLayerComHelper implements PresLayerComInterface {
    private Context context;

    public PresLayerComHelper(Context context) {
        this.context = context;
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void showSoftKeyboard(View focusView) {

    }

    @Override
    public void hideSoftKeyboard() {
        if(context instanceof Activity) {
            try {
                InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(((Activity)context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } catch (Exception e) {
            }
        }
    }
}
