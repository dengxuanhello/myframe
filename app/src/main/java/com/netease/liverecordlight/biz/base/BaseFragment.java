package com.netease.liverecordlight.biz.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.netease.liverecordlight.net.NetworkListener;
import com.netease.liverecordlight.net.NetworkParam;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;

import java.util.List;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class BaseFragment extends Fragment implements NetworkListener, TIMMessageListener {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TIMManager.getInstance().addMessageListener(this);
    }

    @Override
    public void onNetStart(NetworkParam param) {

    }

    @Override
    public void onNetEnd(NetworkParam param) {

    }

    @Override
    public void onNetError(NetworkParam param) {

    }

    @Override
    public void onNetCancel(NetworkParam param) {

    }

    @Override
    public void onMsgSearchComplete(NetworkParam param) {

    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        return false;
    }
}
