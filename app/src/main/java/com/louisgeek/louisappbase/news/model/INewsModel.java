package com.louisgeek.louisappbase.news.model;

import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;

/**
 * Created by louisgeek on 2016/11/5.
 */

public interface INewsModel {
    void  loadNewsChannel(OnLoadNewsChannelListener onLoadNewsChannelListener);
    void  loadNewsList(NewsChannelBean.ChannelListBean channelListBean,int page, OnLoadNewsListListener onLoadNewsListListener);

    //
    void  loadLocalNewsArea(OnloadLocalNewsAreaListener onloadLocalNewsAreaListener);
    void  loadLocalNewsList(LocalNews_AreaBean.CityListBean cityListBean,int page,OnloadLocalNewsListListener onloadLocalNewsListListener);


    interface  OnLoadNewsChannelListener{
        void  onSuccess(ShowAPIBaseBean<NewsChannelBean> showAPIBaseBean);
        void  onError();
    }

    interface  OnLoadNewsListListener{
        void  onSuccess(ShowAPIBaseBean<NewsListBean> showAPIBaseBean);
        void  onError();
    }

    interface  OnloadLocalNewsAreaListener{
        void  onSuccess(ShowAPIBaseBean<LocalNews_AreaBean> showAPIBaseBean);
        void  onError();
    }

    interface  OnloadLocalNewsListListener{
        void  onSuccess(ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean);
        void  onError();
    }
}
