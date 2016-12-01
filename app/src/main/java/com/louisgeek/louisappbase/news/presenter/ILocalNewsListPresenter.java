package com.louisgeek.louisappbase.news.presenter;

import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;

/**
 * Created by louisgeek on 2016/11/5.
 */

public interface ILocalNewsListPresenter {
    void gainLocalNewsListData(LocalNews_AreaBean.CityListBean cityListBean, int page);
}
