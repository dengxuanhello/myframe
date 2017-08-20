package com.dsgly.bixin.biz.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.view.VideoPlayActivity;
import com.dsgly.bixin.net.responseResult.CommentsResult;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;
import com.dsgly.bixin.wigets.ScaledImageView;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

/**
 * Created by dengxuan on 2017/8/12.
 */

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<CommentsResult.UserComment> data;
    public MainPageDataResult.MomentData momentData;
    public Context mContext;
    public CommentListAdapter(Context context,List<CommentsResult.UserComment> data,MainPageDataResult.MomentData momentData){
        this.mContext = context;
        this.momentData = momentData;
        this.data = data;
    }

    public void setData(List<CommentsResult.UserComment> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        RecyclerView.ViewHolder holder;
        if(viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_info_layout, parent, false);
            holder = new CommentListInfoHolder(itemView);
        }else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
            holder = new CommentListViewItemHolder(itemView);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof CommentListViewItemHolder){
            CommentsResult.UserComment comment = data.get(position-1);
            if(comment.userModel != null) {
                Glide.with(mContext).load(comment.userModel.headImgThumbUrl ).into(((CommentListViewItemHolder) holder).avatar);
                ((CommentListViewItemHolder) holder).username.setText(comment.userModel.nickName);
            }
            ((CommentListViewItemHolder) holder).commentTime.setText(comment.gmtCreated);
            ((CommentListViewItemHolder) holder).commentContent.setText(comment.content);
        }else if(holder instanceof CommentListInfoHolder){
            Glide.with(mContext).load(momentData.previewPic).into(((CommentListInfoHolder) holder).scaledImageView);
            ((CommentListInfoHolder) holder).scaledImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(momentData!=null && !TextUtils.isEmpty(momentData.videoPathOrigin)){
                        VideoPlayActivity.startVideoPlay(mContext , momentData.videoPathOrigin);
                    }
                }
            });
            ((CommentListInfoHolder) holder).authorName.setText(momentData.userId);
            if(!TextUtils.isEmpty(momentData.gender)&&!"0".equals(momentData.gender)) {
                ((CommentListInfoHolder) holder).authorName.setCompoundDrawables(null, null, getDrawableByType("1".equals(momentData.gender) ? R.drawable.icon_boy : R.drawable.icon_girl), null);
            }else {
                ((CommentListInfoHolder) holder).authorName.setCompoundDrawables(null, null, null, null);
            }
            ((CommentListInfoHolder) holder).authorConstellation.setText(momentData.authorConstellation);
            ((CommentListInfoHolder) holder).issuesContent.setText(momentData.content);
            ((CommentListInfoHolder) holder).issuesContentTime.setText(momentData.gmtCreated);
            ((CommentListInfoHolder) holder).issuesLikeNum.setText(momentData.starNum);
            ((CommentListInfoHolder) holder).issuesLikeNum.setCompoundDrawables(getDrawableByType("1".equals(momentData.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
            ((CommentListInfoHolder) holder).issuesLikeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CommentListInfoHolder) holder).issuesLikeNum.setCompoundDrawables(getDrawableByType("0".equals(momentData.hasStared)?R.drawable.button_like_pre:R.drawable.button_like),null,null,null);
                }
            });
            ((CommentListInfoHolder) holder).commnetNumTv.setText(String.format("%s条评论",momentData.commentNum));
        }
    }
    private Drawable getDrawableByType(int resid){
        Drawable drawable = mContext.getResources().getDrawable(resid);
        drawable.setBounds(0, 0, drawable.getMinimumWidth()+5, drawable.getMinimumHeight()+5);
        return drawable;
    }
    @Override
    public int getItemCount() {
        if(momentData != null){
            return data == null?1:data.size()+1;
        }
        return data == null?0:data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 0;
        }else {
            return 1;
        }
    }

    public static class CommentListViewItemHolder extends RecyclerView.ViewHolder{
        public CircleImageView avatar;
        public TextView username;
        public TextView commentTime;
        public TextView commentContent;
        public CommentListViewItemHolder(View itemView) {
            super(itemView);
            avatar = (CircleImageView) itemView.findViewById(R.id.user_avatar);
            username = (TextView) itemView.findViewById(R.id.username);
            commentTime = (TextView) itemView.findViewById(R.id.comment_time);
            commentContent = (TextView) itemView.findViewById(R.id.user_comment);
        }
    }

    public static class CommentListInfoHolder extends RecyclerView.ViewHolder{
        public ScaledImageView scaledImageView;
        public ImageView imgActionBth;
        public TextView authorName;
        public TextView authorConstellation;
        public TextView issuesContent;
        public TextView issuesContentTime;
        public TextView issuesLikeNum;
        public TextView commnetNumTv;
        private CircleImageView avatar;
        public CommentListInfoHolder(View itemView) {
            super(itemView);
            scaledImageView = (ScaledImageView) itemView.findViewById(R.id.main_page_item_img);
            imgActionBth = (ImageView) itemView.findViewById(R.id.main_page_item_img_btn);
            authorName = (TextView) itemView.findViewById(R.id.issues_author_name);
            authorConstellation = (TextView) itemView.findViewById(R.id.issues_author_constellation);
            issuesContent = (TextView) itemView.findViewById(R.id.issues_content);
            issuesContentTime = (TextView) itemView.findViewById(R.id.issues_content_time);
            issuesLikeNum = (TextView) itemView.findViewById(R.id.issues_content_like_num);
            commnetNumTv = (TextView) itemView.findViewById(R.id.comment_num_tv);
            avatar = (CircleImageView) itemView.findViewById(R.id.user_avatar);
        }
    }
}
