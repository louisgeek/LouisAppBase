package com.louisgeek.louisappbase.jokes.view;

import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.jokes.bean.JokeImageAndTextListBean;

/**
 * Created by louisgeek on 2016/11/6.
 */

public interface IJokeView {
    void  setJokeListData(ShowAPIBaseBean<JokeImageAndTextListBean> jokeImageAndTextListBeanShowAPIBaseBean);

}
