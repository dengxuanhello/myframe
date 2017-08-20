package com.dsgly.bixin.biz.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseActivity;
import com.dsgly.bixin.biz.view.adapter.CommentListAdapter;
import com.dsgly.bixin.net.NetServiceMap;
import com.dsgly.bixin.net.NetworkParam;
import com.dsgly.bixin.net.RequestUtils;
import com.dsgly.bixin.net.responseResult.CommentsResult;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.wigets.FullyLinearLayoutManager;
import com.dsgly.bixin.wigets.ScaledImageView;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/9.
 */

public class CommentDetailActivity extends BaseActivity {

    public static String MomentDataKey = "MomentDataKey";


    public RecyclerView commentListView;
    public CommentListAdapter commentListAdapter;
    public MainPageDataResult.MomentData momentData;
    public List<CommentsResult.UserComment> data;
    //public CommentsResult commentsResult;

    public static void startCommentDetailActivity(Context context, MainPageDataResult.MomentData data){
        if(context != null) {
            Intent intent = new Intent();
            intent.setClass(context, CommentDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(MomentDataKey,data);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    @Override
    public void initViews() {
        super.initViews();
        setContentView(R.layout.comment_detail_activity);

        commentListView = (RecyclerView) findViewById(R.id.comment_list);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        commentListView.setLayoutManager(manager);
    }

    @Override
    public void initData() {
        super.initData();
        if(getIntent() == null){
            finish();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if(bundle == null){
            finish();
            return;
        }
        if(bundle.getSerializable(MomentDataKey) != null && bundle.getSerializable(MomentDataKey) instanceof MainPageDataResult.MomentData) {
            momentData = (MainPageDataResult.MomentData) bundle.getSerializable(MomentDataKey);
        }else {
            finish();
            return;
        }

        //TODO  加载这条动态的用户头像   Glide.with(this).load( ).into(scaledImageView);
        data = new ArrayList<CommentsResult.UserComment>();
        commentListAdapter = new CommentListAdapter(this,data,momentData);
        commentListView.setAdapter(commentListAdapter);
        requestForComments();
    }



    public void requestForComments(){
        if(momentData == null){
            return;
        }
        NetworkParam networkParam = new NetworkParam(this);
        networkParam.key = NetServiceMap.GetMomentComments;
        //RequestUtils.startGetRequestExt(networkParam,momentData.id);
        RequestUtils.startGetRequestExt(networkParam,"3");
    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        if(param == null){
            return;
        }
        if(param.key == NetServiceMap.GetMomentComments){
            if(param.baseResult != null && param.baseResult instanceof CommentsResult){
                CommentsResult commentsResult = (CommentsResult) param.baseResult;
                data = commentsResult.data;
                commentListAdapter.setData(data);
                commentListAdapter.notifyDataSetChanged();
            }
        }
    }
}
