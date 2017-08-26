package com.dsgly.bixin.biz.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsgly.bixin.R;
import com.dsgly.bixin.net.responseResult.MainPageDataResult;

import java.util.List;

/**
 * Created by dengxuan on 2017/8/20.
 */

public class MyVideoMomentAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MainPageDataResult.MomentData> mMainPageDataList;
    private Context mContext;
    private ItemClickedListener clickedListener;

    public MyVideoMomentAdapter(Context context){
        this.mContext = context;
    }

    public void setMainPageDataList(List<MainPageDataResult.MomentData> mMainPageDataList) {
        this.mMainPageDataList = mMainPageDataList;
    }

    public void setClickedListener(ItemClickedListener clickedListener) {
        this.clickedListener = clickedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_video_moment_item, parent, false);
        MyMomentListItemHolder holder = new MyMomentListItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final MainPageDataResult.MomentData data = mMainPageDataList.get(position);
        if(holder instanceof MyMomentListItemHolder){
            Glide.with(mContext).load(data.previewPic).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ic_launcher).into(((MyMomentListItemHolder) holder).videoCover);
            ((MyMomentListItemHolder) holder).momentText.setText(data.content);
            ((MyMomentListItemHolder) holder).momentTime.setText(data.gmtCreated);
            ((MyMomentListItemHolder) holder).commentNum.setText(data.commentNum);
            ((MyMomentListItemHolder) holder).likeNum.setText(data.starNum);
            ((MyMomentListItemHolder) holder).rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickedListener != null){
                        clickedListener.onItemClicked(data);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMainPageDataList == null ? 0 : mMainPageDataList.size();
    }

    public static class MyMomentListItemHolder extends RecyclerView.ViewHolder {
        final View rootView;
        final ImageView videoCover;
        final TextView momentText;
        final TextView momentTime;
        final TextView likeNum;
        final TextView commentNum;
        public MyMomentListItemHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            videoCover = (ImageView) itemView.findViewById(R.id.video_cover);
            momentText = (TextView) itemView.findViewById(R.id.moment_text);
            momentTime = (TextView) itemView.findViewById(R.id.moment_time);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            commentNum = (TextView) itemView.findViewById(R.id.comment_num);
        }
    }

    public interface ItemClickedListener{
        void onItemClicked(MainPageDataResult.MomentData data);
    }
}
