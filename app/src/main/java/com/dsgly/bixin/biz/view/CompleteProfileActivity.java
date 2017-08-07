package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.presenter.CompleteProfilePresenter;

/**
 * Created by dengxuan on 2017/8/5.
 */

public class CompleteProfileActivity extends BaseActivity {
    public static int REQUEST_ALBUM_CODE = 0x1000;
    private CompleteProfilePresenter presenter;

    public TextView mDateTextView;
    public TextView mHeightTextView;
    public LinearLayout mChooseAvatarLl;
    public Button mNextBtn;

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
        mHeightTextView = (TextView) findViewById(R.id.height_choose_tv);
        mHeightTextView.setOnClickListener(this);
        mChooseAvatarLl = (LinearLayout) findViewById(R.id.choose_avatar);
        mChooseAvatarLl.setOnClickListener(this);
        mNextBtn = (Button) findViewById(R.id.complete_next);
        mNextBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mDateTextView)){
            presenter.showDatePicker();
        }else if(v.equals(mHeightTextView)){
            presenter.showHeightPicker();
        }else if(v.equals(mChooseAvatarLl)){
            presenter.choosePic();
        }else if(v.equals(mNextBtn)){
            CompleteProfileUploadVideoActivity.startCompleteProfileUploadVideoActivity(this);
        }
    }
}
