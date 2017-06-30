package com.netease.liverecordlight.biz.presenter;

import com.dx.thirdparty.ShareUtils;
import com.netease.liverecordlight.biz.view.TestActivity;
import com.netease.liverecordlight.biz.base.BasePresenter;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public class TestPresenter extends BasePresenter<TestActivity> {

    public void doShare(){
        ShareUtils.showShare(mvpView);
    }


}
