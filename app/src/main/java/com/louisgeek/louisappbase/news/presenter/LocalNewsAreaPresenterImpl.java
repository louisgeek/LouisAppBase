package com.louisgeek.louisappbase.news.presenter;

import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.model.INewsModel;
import com.louisgeek.louisappbase.news.model.NewsModelImpl;
import com.louisgeek.louisappbase.news.view.INewsView;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class LocalNewsAreaPresenterImpl implements  ILocalNewsAreaPresenter{

    public LocalNewsAreaPresenterImpl(INewsView newsView) {
        mNewsView = newsView;
    }

    private INewsView mNewsView;
    private INewsModel mNewsModel=new NewsModelImpl();
    @Override
    public void gainLocalNewsAreaData() {
        mNewsModel.loadLocalNewsArea(new INewsModel.OnloadLocalNewsAreaListener() {
            @Override
            public void onSuccess(ShowAPIBaseBean<LocalNews_AreaBean> showAPIBaseBean) {
                mNewsView.setLocalNewsArea(showAPIBaseBean);
            }

            @Override
            public void onError() {

            }
        });
    }
}
