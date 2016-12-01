package com.louisgeek.louisappbase.jokes.model;

import com.louisgeek.louisappbase.base.JuHeBaseBean;
import com.louisgeek.louisappbase.jokes.bean.Juhe_JokeImageAndTextListBean;

/**
 * Created by louisgeek on 2016/11/6.
 */

public interface IJuheJokeModel {

   void loadJuheJokeData(String type,String time, int sort, int page, OnLoadJuheJokeDataListener onLoadJuheJokeDataListener);

    interface  OnLoadJuheJokeDataListener{
        void  onSuccess(JuHeBaseBean<Juhe_JokeImageAndTextListBean> juhe_jokeImageAndTextListBeanJuHeBaseBean);
        void  onError();
    }
}
