package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.adapter.MyVideoMomentAdapter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.UCUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/20.
 */

public class MyVideoMomentActivity extends BaseActivity implements MyVideoMomentAdapter.ItemClickedListener {

    private UserInfo localUserInfo;
    private RecyclerView mMomentList;
    private List<MainPageDataResult.MomentData> mMainPageDataList;
    private MyVideoMomentAdapter mVideoMomentAdapter;

    public static void startMyVideoMomentActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, MyVideoMomentActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.video_moment_activity_layout);
        mMomentList = (RecyclerView) findViewById(R.id.moment_list);
    }

    @Override
    public void initData() {
        super.initData();
        localUserInfo = UCUtils.getInstance().getUserInfo();
        mMainPageDataList = new ArrayList<MainPageDataResult.MomentData>();
        mVideoMomentAdapter = new MyVideoMomentAdapter(this);
        mVideoMomentAdapter.setClickedListener(this);
        mMomentList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mMomentList.setAdapter(mVideoMomentAdapter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSelfMoment();
    }

    private void getSelfMoment(){
        if(localUserInfo == null){
            return;
        }
        NetworkParam param = new NetworkParam(this);
        param.key = NetServiceMap.GetUserMoments;
        RequestUtils.startGetRequestExt(param,localUserInfo.userId);
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.GetUserMoments){
            if(param.baseResult instanceof MainPageDataResult) {
                MainPageDataResult result = (MainPageDataResult) param.baseResult;
                mMainPageDataList = result.data;
                mVideoMomentAdapter.setMainPageDataList(mMainPageDataList);
                mVideoMomentAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClicked(MainPageDataResult.MomentData data) {
        if(data != null){
            CommentDetailActivity.startCommentDetailActivity(this,data);
        }
    }
}
