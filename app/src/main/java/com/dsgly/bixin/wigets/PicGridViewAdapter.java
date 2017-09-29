package com.dsgly.bixin.wigets;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;
import com.dsgly.bixin.net.responseResult.GalleryResult;

import java.util.List;

/**
 * 每一个viewpager中的 StickPicListView 的adapter.
 * Created by bjdengxuan1 on 2017/7/12.
 */

public class PicGridViewAdapter extends RecyclerView.Adapter<PicGridViewAdapter.StickPicViewHolder> {

    private List<GalleryResult.GalleryInfo> dataList;
    private OnItemChooseListener itemChooseListener;
    private Context mContext;

    public PicGridViewAdapter(List<GalleryResult.GalleryInfo> dataList, Context context) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public StickPicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pic_grid_view_item, parent, false);
        return new StickPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StickPicViewHolder holder, final int position) {
        if(dataList.get(position)==null){
            return;
        }
//        holder.imageView.setTag(position);   Glide 不能设置tag  坑
        if("add".equals(dataList.get(position).pic)){//+号
            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            holder.imageView.setImageDrawable(mContext.getDrawable(R.drawable.button_import_video));
        }else {
            Glide.with(mContext).load(dataList.get(position).picThumb).into(holder.imageView);

            if (itemChooseListener != null && itemChooseListener.isSelf()) {
                holder.deleteBt.setVisibility(View.VISIBLE);
                holder.deleteBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemChooseListener != null) {
                            itemChooseListener.onDelete(position, dataList.get(position));
                        }
                    }
                });
            }
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemChooseListener != null){
                    itemChooseListener.onItemChoosed(position, dataList);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0:dataList.size();
    }

    class StickPicViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        View deleteBt;
        public StickPicViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.stick_item_image);
            deleteBt = itemView.findViewById(R.id.iv_pic_del);
        }
    }

    public interface OnItemChooseListener{
        void onItemChoosed(int position, List<GalleryResult.GalleryInfo> dataList);

        void onDelete(int position, GalleryResult.GalleryInfo picInfo);

        boolean isSelf();
    }

    public void setItemChooseListener(OnItemChooseListener itemChooseListener) {
        this.itemChooseListener = itemChooseListener;
    }

    public void setDataList(List<GalleryResult.GalleryInfo> dataList) {
        this.dataList = dataList;
    }
}
