package com.dsgly.bixin.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsgly.bixin.R;
import com.dsgly.bixin.biz.base.BaseFragment;

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

        return inflater.inflate(R.layout.myself_frg_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
