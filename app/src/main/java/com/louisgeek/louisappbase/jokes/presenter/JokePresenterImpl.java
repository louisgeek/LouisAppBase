package com.louisgeek.louisappbase.jokes.presenter;

import com.louisgeek.louisappbase.jokes.bean.JokeImageAndTextListBean;
import com.louisgeek.louisappbase.jokes.model.IJokeModel;
import com.louisgeek.louisappbase.jokes.model.JokeModelImpl;
import com.louisgeek.louisappbase.jokes.view.IJokeView;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;

/**
 * Created by louisgeek on 2016/11/6.
 */

public class JokePresenterImpl implements IJokePresenter{
    private IJokeModel mJokeModel=new JokeModelImpl();

    public JokePresenterImpl(IJokeView jokeView) {
        mJokeView = jokeView;
    }

    private IJokeView mJokeView;

    @Override
    public void gainJokeData(String type, int page) {
        mJokeModel.loadJokeData(type,page, new IJokeModel.OnLoadJokeDataListener() {
            @Override
            public void onSuccess(ShowAPIBaseBean<JokeImageAndTextListBean> jokeImageAndTextListBeanShowAPIBaseBean) {
                mJokeView.setJokeListData(jokeImageAndTextListBeanShowAPIBaseBean);
            }

            @Override
            public void onError() {

            }
        });
    }
}
