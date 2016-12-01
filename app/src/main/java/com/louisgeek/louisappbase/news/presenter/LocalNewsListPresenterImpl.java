package com.louisgeek.louisappbase.news.presenter;

import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.model.INewsModel;
import com.louisgeek.louisappbase.news.model.NewsModelImpl;
import com.louisgeek.louisappbase.news.view.INewsListView;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class LocalNewsListPresenterImpl implements  ILocalNewsListPresenter{

    public LocalNewsListPresenterImpl(INewsListView newsListView) {
        mNewsListView = newsListView;
    }

    private INewsListView mNewsListView;
    private INewsModel mNewsModel=new NewsModelImpl();


    @Override
    public void gainLocalNewsListData(LocalNews_AreaBean.CityListBean cityListBean, int page) {
        mNewsModel.loadLocalNewsList(cityListBean,page,new INewsModel.OnloadLocalNewsListListener() {
            @Override
            public void onSuccess(ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean) {
                mNewsListView.setLocalNewListData(showAPIBaseBean);
            }
            @Override
            public void onError() {

            }
        });
    }
}
