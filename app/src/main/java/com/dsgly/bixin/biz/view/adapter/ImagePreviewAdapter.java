package com.dsgly.bixin.biz.view.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dsgly.bixin.R;

import java.util.List;

/**
 * Created by madong on 2016/12/5.
 */

public class ImagePreviewAdapter extends PagerAdapter {

    private boolean mDataSetChanged;
    private final List<String> mList;
    private View.OnClickListener mOnClickListener;
    private View.OnLongClickListener mOnLongClickListener;

    public ImagePreviewAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getView(position, null, container);
        container.addView(view);
        return view;
    }

    private View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_preview, parent, false);
        }

        ImageView iv = (ImageView) convertView.findViewById(R.id.photo_view);

        if (position >= 0 && position < getCount()) {
            String item = mList.get(position);
            Glide.with(parent.getContext()).load(item).into(iv);
        }

        iv.setOnClickListener(mOnClickListener);
        iv.setOnLongClickListener(mOnLongClickListener);
        return convertView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public final int getItemPosition(Object object) {
        if (mDataSetChanged) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        mDataSetChanged = true;
        super.notifyDataSetChanged();
        mDataSetChanged = false;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
        this.mOnLongClickListener = onLongClickListener;
    }

}
