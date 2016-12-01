package com.louisgeek.louisappbase.news.presenter;

import android.os.Handler;

import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.model.INewsModel;
import com.louisgeek.louisappbase.news.model.NewsModelImpl;
import com.louisgeek.louisappbase.news.view.INewsListView;

/**
 * Created by louisgeek on 2016/11/5.
 */

public class NewsListPresenterImpl implements  INewsListPresenter{

    public NewsListPresenterImpl(INewsListView newsListView) {
        mNewsListView = newsListView;
    }

    private INewsListView mNewsListView;


    private INewsModel mNewsModel=new NewsModelImpl();



    @Override
    public void gainNewsListData(NewsChannelBean.ChannelListBean channelListBean,int page) {
        mNewsModel.loadNewsList(channelListBean, page,new INewsModel.OnLoadNewsListListener() {
            @Override
            public void onSuccess(final ShowAPIBaseBean<NewsListBean> showAPIBaseBean) {
                //延迟x秒消失
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mNewsListView.setNewListData(showAPIBaseBean);
                    }
                },1*1000);
            }

            @Override
            public void onError() {

            }
        });
    }
}