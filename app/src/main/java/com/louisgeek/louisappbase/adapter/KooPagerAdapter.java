package com.louisgeek.louisappbase.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by louisgeek on 2016/11/7.
 */

public class KooPagerAdapter extends PagerAdapter{
    public KooPagerAdapter(List<ImageShowBean> imageShowBeanList) {
        mImageShowBeanList = imageShowBeanList;
    }

    private    List<ImageShowBean> mImageShowBeanList=new ArrayList<>();

    @Override
    public int getCount() {
        return mImageShowBeanList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext()).inflate(R.layout.layout_content_image_show,null,false);
        PhotoView photoView= (PhotoView) view.findViewById(R.id.id_photo_view);
        //
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                if (mPhotoViewAttacherListener!=null){
                    mPhotoViewAttacherListener.onViewTap(view,x,y);
                }
            }
        });
        GlideUtil.displayImage(photoView,mImageShowBeanList.get(position).getImageUrl());

        container.addView(view);
       // return super.instantiateItem(container, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
       // super.destroyItem(container, position, object);
    }

    public void refreshImageShowBeanList(List<ImageShowBean> imageShowBeanList){
        mImageShowBeanList.clear();
        mImageShowBeanList.addAll(imageShowBeanList);
        notifyDataSetChanged();
    }

    public  interface  PhotoViewAttacherListener{
        void onViewTap(View photoView, float x, float y);
    }

    public void setPhotoViewAttacherListener(PhotoViewAttacherListener photoViewAttacherListener) {
        mPhotoViewAttacherListener = photoViewAttacherListener;
    }

    PhotoViewAttacherListener mPhotoViewAttacherListener;

}
