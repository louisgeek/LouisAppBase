package com.louisgeek.louisappbase.imageshow;

import android.content.Context;
import android.content.Intent;

import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/14.
 */

public  class ImageShowDataHelper {
    public static  void setDataAndToImageShow(Context context, String imageUrl) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        //
        List<ImageShowBean> imageShowBeanList = new ArrayList<>();
        ImageShowBean imageShowBean =new ImageShowBean();
        imageShowBean.setImageUrl(imageUrl);
        imageShowBeanList.add(imageShowBean);
        InfoHolderSingleton.getInstance().putMapObj("imageShowBeanList", imageShowBeanList);
        InfoHolderSingleton.getInstance().putMapObj("nowSeletedPos", 0);
//
        context.startActivity(intent);
    }

    public static  void setDataAndToImageShow(Context context, ImageShowBean imageShowBean) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        //
        List<ImageShowBean> imageShowBeanList = new ArrayList<>();
        imageShowBeanList.add(imageShowBean);
        InfoHolderSingleton.getInstance().putMapObj("imageShowBeanList", imageShowBeanList);
        InfoHolderSingleton.getInstance().putMapObj("nowSeletedPos", 0);
//
        context.startActivity(intent);
    }
    public static void setDataAndToImageShow(Context context, List<ImageShowBean> imageShowBeanList,int nowSeletedPos) {
        Intent intent = new Intent(context, ImageShowActivity.class);
        //
        List<ImageShowBean> imageShowBeanArrayList = new ArrayList<>();
        imageShowBeanArrayList.addAll(imageShowBeanList);
        InfoHolderSingleton.getInstance().putMapObj("imageShowBeanList", imageShowBeanArrayList);
        InfoHolderSingleton.getInstance().putMapObj("nowSeletedPos", nowSeletedPos);
//
        context.startActivity(intent);
    }
}
