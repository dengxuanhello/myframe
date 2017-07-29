package com.dsgly.bixin.wigets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsgly.bixin.R;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class UserRegItemView extends LinearLayout {

    private TextView topTitleView;
    private EditText userInputView;
    private TextView userActionView;

    public UserRegItemView(Context context) {
        super(context);
        initView();
    }

    public UserRegItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.user_reg_item_view, this, true);
        topTitleView = (TextView) findViewById(R.id.top_item_name);
        userInputView = (EditText) findViewById(R.id.user_input_area);
        userActionView = (TextView) findViewById(R.id.user_action_area);
    }

    private void setTopTitleText(String text){
        topTitleView.setText(text);
    }
    private void setUserInputHint(String hint){
        topTitleView.setHint(hint);
    }
    private void setUserAction(String text){
        topTitleView.setText(text);
    }
}
