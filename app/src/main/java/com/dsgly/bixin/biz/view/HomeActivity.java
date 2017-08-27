package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dsgly.bixin.QQIM.ui.ConversationFragment;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.base.BaseFragment;
import com.dsgly.bixin.biz.view.frg.MainPageFrg;
import com.dsgly.bixin.biz.view.frg.MyselfFrg;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.responseResult.GetUserInfoResult;
import com.dsgly.bixin.utils.UCUtils;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class HomeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    public static final int REQ_FOR_COMPLETE_INFO = 0x111;
    private final int TAB_HOME = 1;
    private final int TAB_CHAT = 2;
    private final int TAB_SETTING = 3;
    private BaseFragment mCurrentFrg;
    private RadioButton mHomePageBtn;
    private RadioButton mChatPageBtn;
    private RadioButton mSettingsPageBtn;
    private RadioGroup radioGroup;

    public static void startHomeActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, HomeActivity.class);
            context.startActivity(intent);
        }
    }

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
        switch (type){
            case TAB_HOME:
                mCurrentFrg = MainPageFrg.newInstance(null);
                break;
            case TAB_CHAT:
                mCurrentFrg =  ConversationFragment.newInstance(null);
                break;
            case TAB_SETTING:
                mCurrentFrg = MyselfFrg.newInstance(null);
                break;
            default:
                mCurrentFrg = MainPageFrg.newInstance(null);
                break;
        }
        if(mCurrentFrg != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mCurrentFrg).commitAllowingStateLoss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_FOR_COMPLETE_INFO){
            if(resultCode == RESULT_OK){
                getUserInfo();
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param.key == NetServiceMap.GetUSER){
            if(param.baseResult!= null && param.baseResult instanceof GetUserInfoResult) {
                GetUserInfoResult result = (GetUserInfoResult) param.baseResult;
                UCUtils.getInstance().saveUserinfo(result.data);
                UCUtils.getInstance().loginQqIM(result.data.userId);
                setTabSelection(TAB_SETTING);
            }
        }
    }
}
