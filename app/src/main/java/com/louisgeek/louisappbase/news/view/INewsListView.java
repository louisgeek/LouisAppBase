package com.louisgeek.louisappbase.news.view;

import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;

/**
 * Created by louisgeek on 2016/11/5.
 */

public interface INewsListView {
    void  setNewListData(ShowAPIBaseBean<NewsListBean> showAPIBaseBean);
    void  setLocalNewListData(ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean);
}
