package com.louisgeek.louisappbase.musicplayer.presenter;

import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.musicplayer.contract.MusicPlayerContract;
import com.louisgeek.louisappbase.musicplayer.model.MusicPlayerModelImpl;

import java.util.List;

/**
* Created by louisgeek on 2016/11/21
*/

public class MusicPlayerPresenterImpl implements MusicPlayerContract.Presenter{

    private MusicPlayerContract.Model mModel=new MusicPlayerModelImpl();

    public MusicPlayerPresenterImpl(MusicPlayerContract.View view) {
        mView = view;
    }

    private MusicPlayerContract.View mView;
    @Override
    public void gainData() {
        mModel.loadData(new MusicPlayerContract.OnLoadDataCallBack() {

            @Override
            public void onSuccess(List<MusicBean> musicBeanList) {
                mView.backData(musicBeanList);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}