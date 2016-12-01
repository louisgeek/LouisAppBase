package com.louisgeek.louisappbase.imageshow.model;

import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public interface IImageShowModel {
    void loadImageShowList(ImageShowModelImpl.OnLoadImageShowListListener onLoadImageShowListListener);

    interface  OnLoadImageShowListListener{
        void  onSuccess(List<ImageShowBean> imageShowBeanList,int nowSeletedPos);
        void  onError();
    }
}
