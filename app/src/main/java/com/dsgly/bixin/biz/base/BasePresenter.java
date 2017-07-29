package com.dsgly.bixin.biz.base;

/**
 * Created by bjdengxuan1 on 2017/6/29.
 */

public abstract class BasePresenter<V extends IMvpView> implements Presenter<V>{
    protected V mvpView;
    @Override
    public void attachView(V view) {
        mvpView = view;
    }

    @Override
    public void detachView(V view) {
        mvpView = null;
    }
}
