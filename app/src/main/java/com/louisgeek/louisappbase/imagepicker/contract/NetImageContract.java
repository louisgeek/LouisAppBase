package com.louisgeek.louisappbase.imagepicker.contract;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/18.
 */

public class NetImageContract {

    public interface View {
        void backNetData(List<String> imageUrls);
        void  showError(String error);
    }

    public interface Presenter {
        void gainNetData();
    }

    public interface Model {
        void loadNetData(OnLoadDataListener onLoadDataListener);
    }

    public interface  OnLoadDataListener{
        void  onSuccess(List<String> imageUrls);
        void  onError(String error);
    }

}