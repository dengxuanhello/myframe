package com.netease.liverecordlight.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseFragment;
import com.netease.liverecordlight.biz.view.adapter.MainPageAdapter;
import com.netease.liverecordlight.net.NetworkParam;
import com.netease.liverecordlight.net.responseResult.BaseResult;

import java.util.List;

/**
 * 首页，包含有各种动态
 * Created by dengxuan on 2017/7/2.
 */

public class MainPageFrg extends BaseFragment {

    private RecyclerView mListview;
    private List<BaseResult> mMainPageDataList;
    private MainPageAdapter mMainPageAdapter;

    public static MainPageFrg newInstance(Bundle bundle){
        MainPageFrg frg = new MainPageFrg();
        if(bundle != null) {
            frg.setArguments(bundle);
        }
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mainPage = inflater.inflate(R.layout.main_page_frg_layout,container,false);
        mListview = (RecyclerView) mainPage.findViewById(R.id.main_page_list_view);
        return mainPage;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {
        super.onMsgSearchComplete(param);
        //mMainPageDataList = param.baseResult.data
        mMainPageAdapter = new MainPageAdapter(mMainPageDataList);
        mListview.setAdapter(mMainPageAdapter);

    }
}
