package com.louisgeek.louisappbase.musicplayer.contract;

import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/21.
 */

public class MusicPlayerContract{
    
public interface View{
    void  backData(List<MusicBean> musicBeanList);
}

public interface Presenter{
    void  gainData();
}

public interface Model{
    void  loadData(OnLoadDataCallBack onLoadDataCallBack);
}

    public  interface  OnLoadDataCallBack{
        void  onSuccess(List<MusicBean> musicBeanList);
        void  onError(String error);
    }

}