package com.louisgeek.louisappbase.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.louisgeek.louisappbase.R;

public class GlideUtil {

    public static void displayImage(ImageView imageView, String url, int width, int height) {
        Glide.with(imageView.getContext()).load(url).placeholder(R.mipmap.ic_image_no)
                .error(R.mipmap.ic_image_no).crossFade().override(width,height).into(imageView);
    }

    public static void displayImage( ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).placeholder(R.mipmap.ic_image_no)
                .error(R.mipmap.ic_image_no).crossFade().into(imageView);
    }
    public static void displayImageRes(ImageView imageView, int resId) {
        Glide.with(imageView.getContext()).load(resId).placeholder(R.mipmap.ic_image_no)
                .error(R.mipmap.ic_image_no).crossFade().into(imageView);
    }
}
