package com.netease.liverecordlight.biz.view.frg;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.netease.liverecordlight.R;
import com.netease.liverecordlight.biz.base.BaseFragment;

/**
 * Created by bjdengxuan1 on 2017/7/17.
 */

public class ChatListFrg extends BaseFragment {

    private RecyclerView mChatListView;

    public static ChatListFrg newInstance(Bundle bundle){
        ChatListFrg frg = new ChatListFrg();
        if(bundle != null) {
            frg.setArguments(bundle);
        }
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_list_frg_layout,container,false);
        mChatListView = (RecyclerView) v.findViewById(R.id.chat_list_view);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
