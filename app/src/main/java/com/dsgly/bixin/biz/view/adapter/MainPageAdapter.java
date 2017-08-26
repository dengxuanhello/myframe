package com.dsgly.bixin.biz.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.Freezable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsgly.bixin.R;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.wigets.ScaledImageView;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainPageDataResult.MomentData> mMainPageDataList;
    private Context mContext;

    public MainPageAdapter(List<MainPageDataResult.MomentData> list,Context context){
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
        final MainPageDataResult.MomentData result = mMainPageDataList.get(position);
        if(holder instanceof MainPageViewItemHolder){
            ((MainPageViewItemHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onViewClickedListener!=null){
                        onViewClickedListener.onWholeItemClicked(result);
                    }
                }
            });
            //((MainPageViewItemHolder) holder).scaledImageView.setBackgroundResource(R.drawable.test);
            Glide.with(mContext).load(result.previewPic).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).into(((MainPageViewItemHolder) holder).scaledImageView);
            Log.i("dxp",result.previewPic);
            ((MainPageViewItemHolder) holder).scaledImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onViewClickedListener!=null){
                        onViewClickedListener.onImageViewClicked(result);
                    }
                }
            });
            //((MainPageViewItemHolder) holder).authorName.setText(result.authorName);
            ((MainPageViewItemHolder) holder).authorName.setText(result.userId);
            if(!TextUtils.isEmpty(result.gender)&&!"0".equals(result.gender)) {
                ((MainPageViewItemHolder) holder).authorName.setCompoundDrawables(null, null, getDrawableByType("1".equals(result.gender) ? R.drawable.icon_boy : R.drawable.icon_girl), null);
            }else {
                ((MainPageViewItemHolder) holder).authorName.setCompoundDrawables(null, null, null, null);
            }
            ((MainPageViewItemHolder) holder).authorConstellation.setText(result.authorConstellation);
            ((MainPageViewItemHolder) holder).issuesCommentNum.setText(result.commentNum);
            ((MainPageViewItemHolder) holder).issuesCommentNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onViewClickedListener!=null){
                        onViewClickedListener.onCommentClicked(result);
                    }
                }
            });
            ((MainPageViewItemHolder) holder).issuesContent.setText(result.content);
            ((MainPageViewItemHolder) holder).issuesContentTime.setText(result.gmtCreated);
            ((MainPageViewItemHolder) holder).issuesLikeNum.setText(result.starNum);
            ((MainPageViewItemHolder) holder).issuesLikeNum.setCompoundDrawables(getDrawableByType("1".equals(result.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
            ((MainPageViewItemHolder) holder).issuesLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onViewClickedListener!=null){
                        onViewClickedListener.onStarClicked(result);
                    }
                    //((MainPageViewItemHolder) holder).issuesLikeNum.setCompoundDrawables(getDrawableByType("0".equals(result.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
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
        final View rootView;

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
            rootView = itemView;
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

    public void setMainPageDataList(List<MainPageDataResult.MomentData> mMainPageDataList) {
        this.mMainPageDataList = mMainPageDataList;
    }

    public interface ViewClickedListener{
        void onImageViewClicked(MainPageDataResult.MomentData result);
        void onNameClicked(MainPageDataResult.MomentData result);
        void onStarClicked(MainPageDataResult.MomentData result);
        void onCommentClicked(MainPageDataResult.MomentData result);
        void onWholeItemClicked(MainPageDataResult.MomentData result);
    }


    private ViewClickedListener onViewClickedListener;

    public void setOnViewClickedListener(ViewClickedListener listener){
        this.onViewClickedListener = listener;
    }

}
