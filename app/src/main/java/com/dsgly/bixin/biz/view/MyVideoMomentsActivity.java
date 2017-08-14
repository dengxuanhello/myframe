package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.adapter.MainPageAdapter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.net.responseResult.UserInfo;
import com.dsgly.bixin.utils.UCUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/10.
 */

public class MyVideoMomentsActivity extends BaseActivity implements MainPageAdapter.ViewClickedListener {
    private RecyclerView videosView;
    private List<MainPageDataResult.MomentData> mMainPageDataList;
    private MainPageAdapter mMainPageAdapter;
    private UserInfo localUserInfo;

    public static void startMyVideoMomentsActivity(Context context){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, MyVideoMomentsActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.my_video_moment_activity);
        videosView = (RecyclerView) findViewById(R.id.videos);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //manager.setStackFromEnd(true);
        videosView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        super.initData();
        localUserInfo = UCUtils.getInstance().getUserInfo();
        mMainPageDataList = new ArrayList<MainPageDataResult.MomentData>();
        mMainPageAdapter = new MainPageAdapter(mMainPageDataList,this);
        mMainPageAdapter.setOnViewClickedListener(this);
        videosView.setAdapter(mMainPageAdapter);
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
                mMainPageAdapter.setMainPageDataList(mMainPageDataList);
                mMainPageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onImageViewClicked(MainPageDataResult.MomentData result) {
        if(result!=null && !TextUtils.isEmpty(result.videoPathOrigin)){
            VideoPlayActivity.startVideoPlay(this,result.videoPathOrigin);
        }
    }

    @Override
    public void onNameClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onStarClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onCommentClicked(MainPageDataResult.MomentData result) {

    }

    @Override
    public void onWholeItemClicked(MainPageDataResult.MomentData result) {

    }
}
