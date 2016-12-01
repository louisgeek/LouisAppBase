package com.louisgeek.louisappbase.imageshow.model;

import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class ImageShowModelImpl implements  IImageShowModel{
    @Override
    public void loadImageShowList(OnLoadImageShowListListener onLoadImageShowListListener) {
        List<ImageShowBean> imageShowBeanList= (List<ImageShowBean>) InfoHolderSingleton.getInstance().getMapObj("imageShowBeanList");
        int nowSeletedPos=0;
        if (InfoHolderSingleton.getInstance().getMapObj("nowSeletedPos")!=null){
            nowSeletedPos= (int) InfoHolderSingleton.getInstance().getMapObj("nowSeletedPos");
        }

        if (imageShowBeanList!=null&&imageShowBeanList.size()>0){
            onLoadImageShowListListener.onSuccess(imageShowBeanList,nowSeletedPos);
        }else {
            onLoadImageShowListListener.onError();
        }
    }

}
