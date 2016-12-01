package com.louisgeek.louisappbase.news.view;

import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;

/**
 * Created by louisgeek on 2016/11/5.
 */

public interface INewsView {
    void  setNewChannelData(ShowAPIBaseBean<NewsChannelBean> showAPIBaseBean);
    void  setLocalNewsArea(ShowAPIBaseBean<LocalNews_AreaBean> showAPIBaseBean);

}
