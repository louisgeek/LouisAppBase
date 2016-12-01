package com.louisgeek.louisappbase.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.louisgeek.louisappbase.adapter.KooFragmentPagerAdapter;
import com.louisgeek.louisappbase.lrucache.LruCacheStringUitl;

/**
 * Created by louisgeek on 2016/10/31.
 */

public abstract class BaseFragment extends Fragment {
    /**
     * LogTag
     */
    protected static String mLogTag;
    //
    protected static final String ARG_PARAM1 = "param1";
    protected static final String ARG_PARAM2 = "param2";

    protected String mParam1;
    protected String mParam2;


    protected View mRootView;
    ViewPager mViewPager;
    TabLayout mTabLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(setupLayoutId(), container, false);

        mLogTag = this.getClass().getSimpleName();
        Log.d(mLogTag,"onCreateView");


        if (setupViewPagerResId()!=0){
            initViewPager();
            //TabLayout 依托 ViewPager
            if (setupTabLayoutResId()!=0){
                initTabLayout();
            }
        }


        /**
         *   LruCacheStringUitl.initFirst(this);
         */
        LruCacheStringUitl.initFirst(getContext());


        /**
         * last
         */
        initView(mRootView);

        return mRootView;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }


   protected abstract int setupLayoutId();
   protected abstract int setupViewPagerResId();
   protected abstract int setupTabLayoutResId();
   protected abstract void initView(View rootView);


    private void initViewPager() {

        mViewPager = findById(setupViewPagerResId());

        KooFragmentPagerAdapter fragmentPagerAdapter=setupKooFragmentPagerAdapter();

        if (fragmentPagerAdapter!=null) {
          /*  KooFragmentPagerAdapter pagerAdapter = new KooFragmentPagerAdapter(getChildFragmentManager());
            pagerAdapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_TOP + "", ""), getString(R.string.news_title_top));
          */
            mViewPager.setAdapter(fragmentPagerAdapter);
        }

    }

    private void initTabLayout() {
        mTabLayout=findById(setupTabLayoutResId());
        mTabLayout.setVisibility(View.VISIBLE);
        if (mViewPager!=null){
            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Deprecated
    public void setTabTitles(String[] strings){
        if (strings==null||strings.length<=0){
            return;
        }
        for (int i = 0; i < strings.length; i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(strings[i]));
        }
    }

   protected abstract KooFragmentPagerAdapter setupKooFragmentPagerAdapter();



   protected <V extends View> V findById(int id) {
        return (V) mRootView.findViewById(id);
   }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(mLogTag,"onDestroyView");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(mLogTag,"onPause");
        LruCacheStringUitl.flushDiskCacheMaybeOnPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LruCacheStringUitl.closeDiskCacheMaybeOnDestroy();
        Log.d(mLogTag,"onDestroy");
    }

}
