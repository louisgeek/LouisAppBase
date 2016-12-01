package com.louisgeek.louisappbase.news.presenter;

import com.louisgeek.louisappbase.news.bean.NewsChannelBean;

/**
 * Created by louisgeek on 2016/11/5.
 */

public interface INewsListPresenter {
    void gainNewsListData(NewsChannelBean.ChannelListBean channelListBean,int page);
}
