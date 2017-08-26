package com.dsgly.bixin.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseFragment;
import com.dsgly.bixin.biz.view.CommentDetailActivity;
import com.dsgly.bixin.biz.view.CompleteProfileUploadVideoActivity;
import com.dsgly.bixin.biz.view.VideoRecorderActivity;
import com.dsgly.bixin.biz.view.adapter.MainPageAdapter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.requestParam.MainPageDataParam;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.utils.ArrayUtils;
import com.dsgly.bixin.utils.UCUtils;
import com.netease.svsdk.constants.RequestCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页，包含有各种动态
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageFrg extends BaseFragment implements MainPageAdapter.ViewClickedListener, View.OnClickListener {

    private RecyclerView mListview;
    private List<MainPageDataResult.MomentData> mMainPageDataList;
    private MainPageAdapter mMainPageAdapter;
    private ImageView mPublishVideoView;

    public static MainPageFrg newInstance(Bundle bundle){
        MainPageFrg frg = new MainPageFrg();
        if(bundle != null) {
            frg.setArguments(bundle);
        }
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainPage = inflater.inflate(R.layout.main_page_frg_layout,container,false);
        mListview = (RecyclerView) mainPage.findViewById(R.id.main_page_list_view);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(false);
        mListview.setLayoutManager(manager);
        mPublishVideoView = (ImageView) mainPage.findViewById(R.id.publish_video_iv);
        mPublishVideoView.setOnClickListener(this);
        return mainPage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainPageDataList = new ArrayList<MainPageDataResult.MomentData>();
        mMainPageAdapter = new MainPageAdapter(mMainPageDataList,getContext());
        mMainPageAdapter.setOnViewClickedListener(this);
        mListview.setAdapter(mMainPageAdapter);
        mMainPageAdapter.notifyDataSetChanged();

        requestMainPageData();
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.MonentList){
            MainPageDataResult result = (MainPageDataResult) param.baseResult;
            if(result != null && !ArrayUtils.isEmpty(result.data)){
                mMainPageDataList = result.data;
                mMainPageAdapter.setMainPageDataList(mMainPageDataList);
                mMainPageAdapter.notifyDataSetChanged();
            }
        }
    }

    private void requestMainPageData(){
        MainPageDataParam mainPageDataParam = new MainPageDataParam();
        mainPageDataParam.cursor = "1";
        mainPageDataParam.pageSize = "20";
        mainPageDataParam.meId = UCUtils.meId;
        NetworkParam networkParam = new NetworkParam(this);
        //networkParam.param = mainPageDataParam;
        networkParam.key = NetServiceMap.MonentList;

        RequestUtils.startGetRequest(networkParam);
    }


    @Override
    public void onImageViewClicked(MainPageDataResult.MomentData result) {
        /*if(result!=null && !TextUtils.isEmpty(result.videoPathOrigin)){
            VideoPlayActivity.startVideoPlay(getActivity(),result.videoPathOrigin);
        }*/
        if(result != null){
            CommentDetailActivity.startCommentDetailActivity(getActivity(),result);
        }
    }

    @Override
    public void onNameClicked(MainPageDataResult.MomentData result) {
        if(result != null){
            CommentDetailActivity.startCommentDetailActivity(getActivity(),result);
        }
    }

    @Override
    public void onStarClicked(MainPageDataResult.MomentData result) {
        if(result != null){
            //CommentDetailActivity.startCommentDetailActivity(getActivity(),result);
            starMoment(result.id);
        }
    }

    @Override
    public void onCommentClicked(MainPageDataResult.MomentData result) {
        if(result != null){
            CommentDetailActivity.startCommentDetailActivity(getActivity(),result);
        }
    }

    @Override
    public void onWholeItemClicked(MainPageDataResult.MomentData result) {
        if(result != null){
            CommentDetailActivity.startCommentDetailActivity(getActivity(),result);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.equals(mPublishVideoView)){
            VideoRecorderActivity.startVideoRecordActivity(getActivity(), 777);
            //CompleteProfileUploadVideoActivity.startCompleteProfileUploadVideoActivity(getActivity());
        }
    }
}
