package com.dsgly.bixin.wigets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.dsgly.bixin.net.responseResult.GalleryResult;

import java.util.List;

/**
 * Created by bjdengxuan1 on 2017/7/12.
 */

public class PicGridView extends RecyclerView {

    public static final int ROWS = 2; //2行
    public static final int COLUMNS = 3; //3列

    private List<GalleryResult.GalleryInfo> mDataList;
    private PicGridViewAdapter mAdapter;

    public PicGridView(Context context) {
        super(context);
        initDefaultStyle();
    }

    public PicGridView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDefaultStyle();
    }

    private void initDefaultStyle(){
        setLayoutManager(new WrapHeightLayoutManager(getContext(), COLUMNS));
        //addItemDecoration(new DividerItemDecoration(getContext() , DividerItemDecoration.VERTICAL));
    }

    public void fillData(List<GalleryResult.GalleryInfo> dataList){
        if(dataList == null || dataList.size() == 0){
            return;
        }
        mDataList = dataList;//adjustData(dataList);
        mAdapter = new PicGridViewAdapter(mDataList,getContext());
        setAdapter(mAdapter);
    }

    private List<GalleryResult.GalleryInfo> adjustData(List<GalleryResult.GalleryInfo> mDataList){
        if(mDataList == null || mDataList.size() <= COLUMNS*ROWS){
            return mDataList;
        }
        return mDataList.subList(0,COLUMNS*ROWS);
    }

    public PicGridViewAdapter getAdapter() {
        return mAdapter;
    }
}
