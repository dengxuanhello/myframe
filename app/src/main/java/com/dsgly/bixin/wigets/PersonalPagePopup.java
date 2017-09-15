package com.dsgly.bixin.wigets;

import android.view.View;

import com.dsgly.bixin.R;

/**
 * 个人主页弹出菜单，转发、屏蔽、举报
 * Created by madong on 2017/9/3.
 */

public class PersonalPagePopup extends CommonPopupWindow implements View.OnClickListener {

    private OnPopupClickListener mOnPopupClickListener;

    public PersonalPagePopup(View rootView) {
        super(rootView);
    }

    public void setOnPopupClickListener(OnPopupClickListener onPopupClickListener) {
        this.mOnPopupClickListener = onPopupClickListener;
    }

    @Override
    public void show() {
        if (popupWindow == null) {
            View view = View.inflate(mRootView.getContext(), R.layout.layout_popup_personal_page, null);
            view.findViewById(R.id.bt_popup_forward).setOnClickListener(this);
            view.findViewById(R.id.bt_popup_pingbi).setOnClickListener(this);
            view.findViewById(R.id.bt_popup_report).setOnClickListener(this);
            view.findViewById(R.id.bt_popup_cancel).setOnClickListener(this);
            setView(view);
        }

        super.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_popup_forward:
                if (mOnPopupClickListener != null) {
                    mOnPopupClickListener.onForwardClick();
                }
                break;
            case R.id.bt_popup_pingbi:
                if (mOnPopupClickListener != null) {
                    mOnPopupClickListener.onPingbiClick();
                }
                break;
            case R.id.bt_popup_report:
                if (mOnPopupClickListener != null) {
                    mOnPopupClickListener.onReportClick();
                }
                break;
            case R.id.bt_popup_cancel:
                break;
        }
    }

    public static interface OnPopupClickListener {

        void onForwardClick();

        void onPingbiClick();

        void onReportClick();
    }
}
