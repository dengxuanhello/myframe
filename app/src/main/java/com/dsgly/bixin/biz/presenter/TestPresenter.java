package com.dsgly.bixin.biz.presenter;

import com.dsgly.bixin.biz.view.TestActivity;
import com.dsgly.bixin.biz.base.BasePresenter;

import cn.sharesdk.onekeyshare.ShareUtils;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public class TestPresenter extends BasePresenter<TestActivity> {

    public void doShare(){
        ShareUtils.showShare(mvpView);
    }


}
