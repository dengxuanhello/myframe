package com.dsgly.bixin.wigets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.dsgly.bixin.R;

/**
 * 首页每一个动态item
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageItemView extends LinearLayout {

    public MainPageItemView(Context context) {
        super(context);
        initView();
    }

    public MainPageItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.main_page_item_view, this, true);
    }
}
