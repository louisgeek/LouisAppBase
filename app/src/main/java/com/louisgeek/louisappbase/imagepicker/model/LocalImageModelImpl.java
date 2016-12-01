package com.louisgeek.louisappbase.imagepicker.model;
import android.content.Context;

import com.louisgeek.louisappbase.imagepicker.ImagePickerHelper;
import com.louisgeek.louisappbase.imagepicker.contract.LocalImageContract;
import com.socks.library.KLog;

import java.util.List;
import java.util.Map;

/**
* Created by louisgeek on 2016/11/17
*/

public class LocalImageModelImpl implements LocalImageContract.Model{


    @Override
    public void loadDirAndFileListViewData(Context context,final LocalImageContract.OnLoadDataListener onLoadDataListener) {
        ImagePickerHelper.scanLocalImage(context, new ImagePickerHelper.ScanLocalImagesCallBack() {
            @Override
            public void onSuccess(Map<String, List<String>> groupMap) {
                onLoadDataListener.onSuccess(groupMap);
            }

            @Override
            public void onError(String message) {
                KLog.d("onError message " + message);
                onLoadDataListener.onError(message);
            }
        });
    }
}