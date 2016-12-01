package com.louisgeek.louisappbase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;



public class KooFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList=new ArrayList<>();
    private List<String>  mStringList=new ArrayList<>();
    FragmentManager mFragmentManager;
    public KooFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager=fm;
    }

    public void addFragment(Fragment fragment,String pageTitle){
        mFragmentList.add(fragment);
        mStringList.add(pageTitle);
        notifyDataSetChanged();
    }
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return super.getPageTitle(position);
        return mStringList.get(position);
    }
    /**
     * 解决  Fragment 滑动时保存状态的问题
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment)super.instantiateItem(container,position);
        mFragmentManager.beginTransaction().show(fragment).commit();
        return fragment;
    }

    /**
     * 解决   Fragment 滑动时保存状态的问题
     * @param container
     * @param position
     * @param object
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
// !!!               super.destroyItem(container, position, object);
        Fragment fragment = mFragmentList.get(position);
        mFragmentManager.beginTransaction().hide(fragment).commit();
    }
}
