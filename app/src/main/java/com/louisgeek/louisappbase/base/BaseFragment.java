package com.louisgeek.louisappbase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by louisgeek on 2016/10/31.
 */

public abstract class BaseFragment extends Fragment {

    //
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    protected String mParam1;
    protected String mParam2;


    protected View mRootView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(setupLayoutId(), container, false);
        initView(mRootView);
        initData();
        return mRootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

   protected abstract int setupLayoutId();
   protected abstract void initView(View rootView);
   protected abstract void initData();
   protected <V extends View> V findById(int id) {
        return (V) mRootView.findViewById(id);
   }
}
