package com.louisgeek.louisappbase.jokes.model;

import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.jokes.bean.JokeImageAndTextListBean;

/**
 * Created by louisgeek on 2016/11/6.
 */

public interface IJokeModel {
   void loadJokeData(String type,int page,OnLoadJokeDataListener onLoadJokeDataListener);

    interface  OnLoadJokeDataListener{
        void  onSuccess(ShowAPIBaseBean<JokeImageAndTextListBean> jokeImageAndTextListBeanShowAPIBaseBean);
        void  onError();
    }
}
