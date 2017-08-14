package com.dsgly.bixin.biz.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.net.responseResult.CommentsResult;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

/**
 * Created by dengxuan on 2017/8/12.
 */

public class CommentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List<CommentsResult.UserComment> data;
    public Context mContext;
    public CommentListAdapter(Context context,List<CommentsResult.UserComment> data){
        this.mContext = context;
        this.data = data;
    }

    public void setData(List<CommentsResult.UserComment> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        CommentListViewItemHolder holder = new CommentListViewItemHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(data == null){
            return;
        }
        CommentsResult.UserComment comment = data.get(position);
        if(holder instanceof CommentListViewItemHolder){
            if(comment.userModel != null) {
                Glide.with(mContext).load(comment.userModel.headImgThumbUrl ).into(((CommentListViewItemHolder) holder).avatar);
                ((CommentListViewItemHolder) holder).username.setText(comment.userModel.nickName);
            }
            ((CommentListViewItemHolder) holder).commentTime.setText(comment.gmtCreated);
            ((CommentListViewItemHolder) holder).commentContent.setText(comment.content);
        }
    }

    @Override
    public int getItemCount() {
        return data == null?0:data.size();
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
}
