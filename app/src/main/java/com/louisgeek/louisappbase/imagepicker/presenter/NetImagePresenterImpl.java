package com.louisgeek.louisappbase.imagepicker.presenter;
import com.louisgeek.louisappbase.imagepicker.contract.NetImageContract;
import com.louisgeek.louisappbase.imagepicker.model.NetImageModelImpl;

import java.util.List;

/**
* Created by louisgeek on 2016/11/18
*/

public class NetImagePresenterImpl implements NetImageContract.Presenter{

    private  NetImageContract.Model mModel=new NetImageModelImpl();

    public NetImagePresenterImpl(NetImageContract.View view) {
        mView = view;
    }

    private  NetImageContract.View mView;

    @Override
    public void gainNetData() {

        mModel.loadNetData(new NetImageContract.OnLoadDataListener() {
            @Override
            public void onSuccess(List<String> imageUrls) {
                mView.backNetData(imageUrls);
            }

            @Override
            public void onError(String error) {
                mView.showError(error);
            }
        });

    }
}