package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.CompleteProfilePresenter;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfileActivity extends BaseActivity {

    private CompleteProfilePresenter presenter;

    public TextView mDateTextView;

    public static void startCompleteProfileActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, CompleteProfileActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void createPresenter() {
        presenter = new CompleteProfilePresenter();
        presenter.attachView(this);
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.complete_profile_activity);
        mDateTextView = (TextView) findViewById(R.id.date_info_tv);
        mDateTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDateTextView)){
            presenter.showDataPicker();
        }
    }
}
