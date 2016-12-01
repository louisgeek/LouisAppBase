package com.louisgeek.louisappbase.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/4.
 * 构建 TabHostShowData
 */

public class TabHostShowData {
    public  List<Class<?>> mFragmentClassesList=new ArrayList<>();
    public  List<Integer> mImageResIDList=new ArrayList<>();
    public  List<Integer> mTabLabelResIDList=new ArrayList<>();

    /*public static String mTabTag[] = { "tab1", "tab2","tab3","tab4"};*/
    public  void addFragmentClass(Class<?> clazz,int imageResId,int tabLabelResId){
        mFragmentClassesList.add(clazz);
        mImageResIDList.add(imageResId);
        mTabLabelResIDList.add(tabLabelResId);
    }

    public List<Class<?>> getFragmentClassesList() {
        return mFragmentClassesList;
    }

    public List<Integer> getImageResIDList() {
        return mImageResIDList;
    }

    public List<Integer> getTabLabelResIDList() {
        return mTabLabelResIDList;
    }
}
