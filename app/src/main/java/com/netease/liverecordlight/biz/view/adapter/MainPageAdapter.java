package com.netease.liverecordlight.biz.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.net.responseResult.BaseResult;
import com.netease.liverecordlight.wigets.MainPageItemView;
import com.netease.liverecordlight.wigets.ScaledImageView;

import java.util.List;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseResult> mMainPageDataList;

    public MainPageAdapter(List<BaseResult> list){
        this.mMainPageDataList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_item_view, parent, false);
        MainPageViewItemHolder holder = new MainPageViewItemHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseResult result = mMainPageDataList.get(position);
        if(holder instanceof MainPageViewItemHolder){

        }
    }

    @Override
    public int getItemCount() {
        return mMainPageDataList ==null ? 0 : mMainPageDataList.size();
    }

    public static class MainPageViewItemHolder extends RecyclerView.ViewHolder{

        final ScaledImageView scaledImageView;
        final Button imgActionBth;
        final TextView authorName;
        final TextView authorConstellation;
        final TextView issuesContent;
        final TextView issuesContentTime;
        final TextView issuesLikeNum;
        final TextView issuesCommentNum;

        public MainPageViewItemHolder(View itemView) {
            super(itemView);
            scaledImageView = (ScaledImageView) itemView.findViewById(R.id.main_page_item_img);
            imgActionBth = (Button) itemView.findViewById(R.id.main_page_item_img_btn);
            authorName = (TextView) itemView.findViewById(R.id.issues_author_name);
            authorConstellation = (TextView) itemView.findViewById(R.id.issues_author_constellation);
            issuesContent = (TextView) itemView.findViewById(R.id.issues_content);
            issuesContentTime = (TextView) itemView.findViewById(R.id.issues_content_time);
            issuesLikeNum = (TextView) itemView.findViewById(R.id.issues_content_like_num);
            issuesCommentNum = (TextView) itemView.findViewById(R.id.issues_content_comment_num);
        }

    }
}
