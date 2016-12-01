package com.louisgeek.louisappbase.jokes.presenter;

import com.louisgeek.louisappbase.base.JuHeBaseBean;
import com.louisgeek.louisappbase.jokes.bean.Juhe_JokeImageAndTextListBean;
import com.louisgeek.louisappbase.jokes.model.IJuheJokeModel;
import com.louisgeek.louisappbase.jokes.model.JuheJokeModelImpl;
import com.louisgeek.louisappbase.jokes.view.IJuheJokeView;

/**
 * Created by louisgeek on 2016/11/6.
 */

public class JuheJokePresenterImpl implements IJuheJokePresenter{
    private IJuheJokeModel mJuheJokeModel=new JuheJokeModelImpl();

    public JuheJokePresenterImpl(IJuheJokeView juheJokeView) {
        mJuheJokeView = juheJokeView;
    }

    private IJuheJokeView mJuheJokeView;

    @Override
    public void gainJuheJokeData(String type,String time,int sort, int page) {
        mJuheJokeModel.loadJuheJokeData(type,time,sort,page, new IJuheJokeModel.OnLoadJuheJokeDataListener() {
            @Override
            public void onSuccess(JuHeBaseBean<Juhe_JokeImageAndTextListBean> juhe_jokeImageAndTextListBeanJuHeBaseBean) {
                mJuheJokeView.setJuheJokeListData(juhe_jokeImageAndTextListBeanJuHeBaseBean);
            }

            @Override
            public void onError() {

            }
        });
    }
}
