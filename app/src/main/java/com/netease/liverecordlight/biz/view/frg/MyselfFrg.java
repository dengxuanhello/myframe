package com.netease.liverecordlight.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseFragment;

/**
 * Created by dengxuan on 2017/7/16.
 */

public class MyselfFrg extends BaseFragment {


    public static MyselfFrg newInstance(Bundle bundle){
        MyselfFrg frg = new MyselfFrg();
        if(bundle != null) {
            frg.setArguments(bundle);
        }
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.main_page_frg_layout,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
