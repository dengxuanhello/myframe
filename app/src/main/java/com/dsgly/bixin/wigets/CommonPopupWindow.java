package com.dsgly.bixin.wigets;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.dsgly.bixin.R;

/**
 * Created by madong on 2017/9/3.
 */

public class CommonPopupWindow {

    public static final int TYPE_PERSONAL_PAGE = 1;

    protected PopupWindow popupWindow;

    protected View mRootView;

    protected View mView;

    public CommonPopupWindow(View rootView) {
        this.mRootView = rootView;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public void show() {
        if (mView == null) {
            return;
        }

        if (popupWindow == null) {
            popupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            popupWindow.update();
            popupWindow.setAnimationStyle(R.style.PopupBottomShow);
        }
        popupWindow.showAtLocation(mRootView, Gravity.BOTTOM, 0, 0);
    }

}
