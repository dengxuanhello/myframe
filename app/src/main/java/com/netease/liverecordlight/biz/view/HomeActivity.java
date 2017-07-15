package com.netease.liverecordlight.biz.view;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseActivity;
import com.netease.liverecordlight.biz.view.frg.MainPageFrg;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private final int TAB_HOME = 1;
    private final int TAB_CHAT = 2;
    private final int TAB_SETTING = 3;
    private MainPageFrg mMainPageFrg;
    private RadioButton mHomePageBtn;
    private RadioButton mChatPageBtn;
    private RadioButton mSettingsPageBtn;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.home_activity);
        mHomePageBtn = (RadioButton) findViewById(R.id.home_frg_page);
        mChatPageBtn = (RadioButton) findViewById(R.id.chat_page);
        mSettingsPageBtn = (RadioButton) findViewById(R.id.settings_page);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(this);
        setTabSelection(TAB_HOME);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId){
            case R.id.home_frg_page:
                setTabSelection(TAB_HOME);
                break;
            case R.id.chat_page:
                setTabSelection(TAB_CHAT);
                break;
            case R.id.settings_page:
                setTabSelection(TAB_SETTING);
                break;
        }
    }

    private void setTabSelection(int type){
        Fragment fragment = MainPageFrg.newInstance(null);
        switch (type){
            case TAB_HOME:
                fragment = MainPageFrg.newInstance(null);
                break;
            case TAB_CHAT:

                break;
            case TAB_SETTING:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commitAllowingStateLoss();

    }



}
