package com.louisgeek.louisappbase.imagepicker.contract;

import android.content.Context;

import java.util.List;
import java.util.Map;

/**
 * Created by louisgeek on 2016/11/17.
 */

public class LocalImageContract {


    public interface View {
        void  backDirAndFileData(Map<String, List<String>> groupMap);
        void  showProgressLoading();
        void  hideProgressLoading();
        void  showError(String error);
    }

    public interface Presenter {
        void  gainDirAndFileListViewData(Context context);
    }

    public interface Model {
        void loadDirAndFileListViewData(Context context, OnLoadDataListener onLoadDataListener);
    }

    public interface  OnLoadDataListener{
        void  onSuccess(Map<String, List<String>> groupMap);
        void  onError(String error);
    }
}