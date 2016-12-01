package com.louisgeek.louisappbase.imagepicker.presenter;
import android.content.Context;

import com.louisgeek.louisappbase.imagepicker.contract.LocalImageContract;
import com.louisgeek.louisappbase.imagepicker.model.LocalImageModelImpl;

import java.util.List;
import java.util.Map;

/**
* Created by louisgeek on 2016/11/17
*/

public class LocalImagePresenterImpl implements LocalImageContract.Presenter{

    private  LocalImageContract.Model mModel=new LocalImageModelImpl();

    public LocalImagePresenterImpl(LocalImageContract.View view) {
        mView = view;
    }

    private  LocalImageContract.View mView;

    @Override
    public void gainDirAndFileListViewData(Context context) {
        mView.showProgressLoading();
        mModel.loadDirAndFileListViewData(context, new LocalImageContract.OnLoadDataListener() {
            @Override
            public void onSuccess(Map<String, List<String>> groupMap) {
                mView.hideProgressLoading();

                mView.backDirAndFileData(groupMap);
            }

            @Override
            public void onError(String error) {
                mView.hideProgressLoading();
                mView.showError(error);
            }
        });

    }

}