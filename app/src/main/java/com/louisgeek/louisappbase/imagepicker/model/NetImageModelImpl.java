package com.louisgeek.louisappbase.imagepicker.model;
import com.louisgeek.louisappbase.imagepicker.Images;
import com.louisgeek.louisappbase.imagepicker.contract.NetImageContract;

import java.util.ArrayList;
import java.util.List;

/**
* Created by louisgeek on 2016/11/18
*/

public class NetImageModelImpl implements NetImageContract.Model{

    @Override
    public void loadNetData(NetImageContract.OnLoadDataListener onLoadDataListener) {
        List<String> imageUrls=new ArrayList<>();
        if (Images.imageThumbUrls!=null&&Images.imageThumbUrls.length>0) {
            for (int i = 0; i < Images.imageThumbUrls.length; i++) {
                imageUrls.add(Images.imageThumbUrls[i]);
            }
            onLoadDataListener.onSuccess(imageUrls);
        }else{
            onLoadDataListener.onError("数据为空");
        }

    }
}