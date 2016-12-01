package com.louisgeek.louisappbase.imageshow.presenter;

import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.imageshow.model.ImageShowModelImpl;
import com.louisgeek.louisappbase.imageshow.view.IImageShowView;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class ImageShowPresenterImpl implements  IImageShowPresenter{

    ImageShowModelImpl mImageShowModel=new ImageShowModelImpl();
    IImageShowView mIImageShowView;

    public ImageShowPresenterImpl(IImageShowView IImageShowView) {
        mIImageShowView = IImageShowView;
    }

    @Override
    public void gainImageShowList() {

        mImageShowModel.loadImageShowList(new ImageShowModelImpl.OnLoadImageShowListListener() {
            @Override
            public void onSuccess(List<ImageShowBean> imageShowBeanList,int nowSeletedPos) {
                mIImageShowView.setImageShowList(imageShowBeanList,nowSeletedPos);
            }

            @Override
            public void onError() {
                mIImageShowView.showDataError();
            }
        });
    }
}
