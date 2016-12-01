package com.louisgeek.louisappbase.musicplayer.model;
import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.musicplayer.MusicPlayerHelper;
import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.musicplayer.contract.MusicPlayerContract;

import java.util.List;

/**
* Created by louisgeek on 2016/11/21
*/

public class MusicPlayerModelImpl implements MusicPlayerContract.Model{

    @Override
    public void loadData(final MusicPlayerContract.OnLoadDataCallBack onLoadDataCallBack) {
        MusicPlayerHelper.scanLocalMusicList(KooApplication.getAppContext(), new MusicPlayerHelper.ScanLocalMusicCallBack() {
            @Override
            public void onSuccess(List<MusicBean> mMusicBeanList) {
                onLoadDataCallBack.onSuccess(mMusicBeanList);
            }

            @Override
            public void onError(String message) {
                onLoadDataCallBack.onError(message);
            }
        });
    }
}