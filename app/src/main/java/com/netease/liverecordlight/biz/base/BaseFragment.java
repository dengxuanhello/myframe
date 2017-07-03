package com.netease.liverecordlight.biz.base;

import android.support.v4.app.Fragment;

import com.netease.liverecordlight.net.NetworkListener;
import com.netease.liverecordlight.net.NetworkParam;

/**
 * Created by dengxuan on 2017/7/2.
 */

public class BaseFragment extends Fragment implements NetworkListener {

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
}
