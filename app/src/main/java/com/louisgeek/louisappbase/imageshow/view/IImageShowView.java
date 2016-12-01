package com.louisgeek.louisappbase.imageshow.view;

import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public interface IImageShowView {
    void  setImageShowList(List<ImageShowBean> imageShowBeanList,int nowSeletedPos);
    void  showDataError();
}
