package com.louisgeek.louisappbase.news.presenter;

import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.model.INewsModel;
import com.louisgeek.louisappbase.news.model.NewsModelImpl;
import com.louisgeek.louisappbase.news.view.INewsView;

/**
 * Created by louisgeek on 2016/11/5.
 */

public class NewsChannelPresenterImpl implements  INewsChannelPresenter{

    private INewsModel mNewsModel=new NewsModelImpl();

    public NewsChannelPresenterImpl(INewsView newsView) {
        mNewsView = newsView;
    }

    private INewsView mNewsView;


    @Override
    public void gainNewsChannelData() {
        mNewsModel.loadNewsChannel(new INewsModel.OnLoadNewsChannelListener() {
            @Override
            public void onSuccess(ShowAPIBaseBean showAPIBaseBean) {
                mNewsView.setNewChannelData(showAPIBaseBean);
            }

            @Override
            public void onError() {

            }
        });
    }

}
