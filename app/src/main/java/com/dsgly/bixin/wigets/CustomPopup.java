package com.dsgly.bixin.wigets;

import android.view.View;
import android.widget.TextView;

/**
 * Created by madong on 2017/9/26.
 */

public class CustomPopup extends CommonPopupWindow {
    public CustomPopup(View rootView) {
        super(rootView);
    }

    public static class Builder {

        private View rootView;

        private int layoutId;

        private int resourceId;

        private String text;

        private OnItemClickListener mOnItemClickListener;

        public Builder(View rootView) {
            this.rootView = rootView;
        }

        public Builder setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder setResourceId(int resourceId) {
            this.resourceId = resourceId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
            return this;
        }

        public CommonPopupWindow create() {
            final CommonPopupWindow commonPopupWindow = new CommonPopupWindow(rootView);
            View view = View.inflate(rootView.getContext(), layoutId, null);
            TextView tv = (TextView) view.findViewById(resourceId);
            tv.setText(text);

            tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(0);
                        }
                        commonPopupWindow.dismiss();
                    }
                });

            commonPopupWindow.setView(view);
            return commonPopupWindow;
        }

    }

    public static interface OnItemClickListener {

        void onItemClick(int index);
    }
}
