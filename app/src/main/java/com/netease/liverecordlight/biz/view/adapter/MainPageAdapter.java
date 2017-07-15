package com.netease.liverecordlight.biz.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.net.responseResult.MainPageDataResult;
import com.netease.liverecordlight.wigets.ScaledImageView;

import java.util.List;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainPageDataResult> mMainPageDataList;
    private Context mContext;

    public MainPageAdapter(List<MainPageDataResult> list,Context context){
        this.mMainPageDataList = list;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_page_item_view, parent, false);
        MainPageViewItemHolder holder = new MainPageViewItemHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MainPageDataResult result = mMainPageDataList.get(position);
        if(holder instanceof MainPageViewItemHolder){
            ((MainPageViewItemHolder) holder).scaledImageView.setBackgroundResource(R.drawable.test);
            ((MainPageViewItemHolder) holder).authorName.setText(result.getAuthorName());
            if(!TextUtils.isEmpty(result.getSex())&&!"0".equals(result.getSex())) {
                ((MainPageViewItemHolder) holder).authorName.setCompoundDrawables(null, null, getDrawableByType("1".equals(result.getSex()) ? R.drawable.icon_boy : R.drawable.icon_girl), null);
            }else {
                ((MainPageViewItemHolder) holder).authorName.setCompoundDrawables(null, null, null, null);
            }
            ((MainPageViewItemHolder) holder).authorConstellation.setText(result.getAuthorConstellation());
            ((MainPageViewItemHolder) holder).issuesCommentNum.setText(result.getIssuesCommentNum());
            ((MainPageViewItemHolder) holder).issuesContent.setText(result.getIssuesContent());
            ((MainPageViewItemHolder) holder).issuesContentTime.setText(result.getIssuesContentTime());
            ((MainPageViewItemHolder) holder).issuesLikeNum.setText(result.getIssuesLikeNum());
            ((MainPageViewItemHolder) holder).issuesLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainPageViewItemHolder) holder).issuesLikeNum.setCompoundDrawables(getDrawableByType(R.drawable.button_like_pre),null,null,null);
                }
            });
        }
    }

    private Drawable getDrawableByType(int resid){
        Drawable drawable =mContext.getResources().getDrawable(resid);
        drawable.setBounds(0, 0, drawable.getMinimumWidth()+5, drawable.getMinimumHeight()+5);
        return drawable;
    }

    @Override
    public int getItemCount() {
        return mMainPageDataList == null ? 0 : mMainPageDataList.size();
    }

    public static class MainPageViewItemHolder extends RecyclerView.ViewHolder{

        final ScaledImageView scaledImageView;
        final ImageView imgActionBth;
        final TextView authorName;
        final TextView authorConstellation;
        final TextView issuesContent;
        final TextView issuesContentTime;
        final TextView issuesLikeNum;
        final TextView issuesCommentNum;

        public MainPageViewItemHolder(View itemView) {
            super(itemView);
            scaledImageView = (ScaledImageView) itemView.findViewById(R.id.main_page_item_img);
            imgActionBth = (ImageView) itemView.findViewById(R.id.main_page_item_img_btn);
            authorName = (TextView) itemView.findViewById(R.id.issues_author_name);
            authorConstellation = (TextView) itemView.findViewById(R.id.issues_author_constellation);
            issuesContent = (TextView) itemView.findViewById(R.id.issues_content);
            issuesContentTime = (TextView) itemView.findViewById(R.id.issues_content_time);
            issuesLikeNum = (TextView) itemView.findViewById(R.id.issues_content_like_num);
            issuesCommentNum = (TextView) itemView.findViewById(R.id.issues_content_comment_num);
        }
    }
}
