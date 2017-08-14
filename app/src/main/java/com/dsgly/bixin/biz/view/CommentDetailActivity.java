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
import com.dsgly.bixin.wigets.ScaledImageView;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dengxuan on 2017/8/9.
 */

public class CommentDetailActivity extends BaseActivity {

    public static String MomentDataKey = "MomentDataKey";

    public ScaledImageView scaledImageView;
    public ImageView imgActionBth;
    public TextView authorName;
    public TextView authorConstellation;
    public TextView issuesContent;
    public TextView issuesContentTime;
    public TextView issuesLikeNum;
    public RecyclerView commentListView;
    public CommentListAdapter commentListAdapter;
    public MainPageDataResult.MomentData momentData;

    public TextView commnetNumTv;
    private CircleImageView avatar;
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
        scaledImageView = (ScaledImageView) findViewById(R.id.main_page_item_img);
        imgActionBth = (ImageView) findViewById(R.id.main_page_item_img_btn);
        authorName = (TextView) findViewById(R.id.issues_author_name);
        authorConstellation = (TextView) findViewById(R.id.issues_author_constellation);
        issuesContent = (TextView) findViewById(R.id.issues_content);
        issuesContentTime = (TextView) findViewById(R.id.issues_content_time);
        issuesLikeNum = (TextView) findViewById(R.id.issues_content_like_num);
        commnetNumTv = (TextView) findViewById(R.id.comment_num_tv);
        avatar = (CircleImageView) findViewById(R.id.user_avatar);
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
        Glide.with(this).load(momentData.previewPic).into(scaledImageView);
        scaledImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(momentData!=null && !TextUtils.isEmpty(momentData.videoPathOrigin)){
                    VideoPlayActivity.startVideoPlay(CommentDetailActivity.this , momentData.videoPathOrigin);
                }
            }
        });
        authorName.setText(momentData.userId);
        if(!TextUtils.isEmpty(momentData.gender)&&!"0".equals(momentData.gender)) {
            authorName.setCompoundDrawables(null, null, getDrawableByType("1".equals(momentData.gender) ? R.drawable.icon_boy : R.drawable.icon_girl), null);
        }else {
            authorName.setCompoundDrawables(null, null, null, null);
        }
        authorConstellation.setText(momentData.authorConstellation);
        issuesContent.setText(momentData.content);
        issuesContentTime.setText(momentData.gmtCreated);
        issuesLikeNum.setText(momentData.starNum);
        issuesLikeNum.setCompoundDrawables(getDrawableByType("1".equals(momentData.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
        issuesLikeNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                issuesLikeNum.setCompoundDrawables(getDrawableByType("0".equals(momentData.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
            }
        });
        commnetNumTv.setText(String.format("%s条评论",momentData.commentNum));
        //TODO  加载这条动态的用户头像   Glide.with(this).load( ).into(scaledImageView);
        data = new ArrayList<CommentsResult.UserComment>();
        commentListAdapter = new CommentListAdapter(this,data);
        commentListView.setAdapter(commentListAdapter);
        requestForComments();
    }

    private Drawable getDrawableByType(int resid){
        Drawable drawable = getResources().getDrawable(resid);
        drawable.setBounds(0, 0, drawable.getMinimumWidth()+5, drawable.getMinimumHeight()+5);
        return drawable;
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
