package com.louisgeek.louisappbase.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by louisgeek on 2016/6/17.
 */
public class SizeTool {
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param v
     * @return
     */
    public static int getMeasuredWidthMy(View v) {
        int w_h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w_h, w_h);
        int width = v.getMeasuredWidth();
        System.out.println("measure width=" + width);
        return width;
    }

    /**
     * @param v
     * @return
     */
    public static int getMeasuredHeightMy(View v) {
        int w_h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w_h, w_h);
        int height = v.getMeasuredHeight();
        System.out.println("measure height=" + height);
        return height;
    }
    public static void getViewRealWidth(final View view, final BackWidthAndHeightCallBack backWidthAndHeightCallBack){
        view.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        int width=view.getWidth(); // 获取宽度
                        int height=view.getHeight(); // 获取高度

                        /**
                         *
                         */
                        backWidthAndHeightCallBack.backWidthAndHeight(width,height);
                        return true;
                    }
                });
    }
    public static void getViewRealWidth2(final View view, final BackWidthAndHeightCallBack backWidthAndHeightCallBack){
        view.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                           view.getViewTreeObserver()
                                   .removeOnGlobalLayoutListener(this);
                       }
                       else {
                           view.getViewTreeObserver()
                                   .removeGlobalOnLayoutListener(this);
                       }
                        int width=view.getWidth(); // 获取宽度
                        int height=view.getHeight(); // 获取高度

                        /**
                         *
                         */
                        backWidthAndHeightCallBack.backWidthAndHeight(width,height);
                    }
                });
    }

    public static void getViewRealWidth3(final View view, final BackWidthAndHeightCallBack backWidthAndHeightCallBack){
        view.addOnLayoutChangeListener(
                new View.OnLayoutChangeListener() {

                    @Override
                    public void onLayoutChange(View v, int l, int t, int r, int b,
                                               int oldL, int oldT, int oldR, int oldB) {
                        view.removeOnLayoutChangeListener(this);
                        int width=view.getWidth(); // 获取宽度
                        int height=view.getHeight(); // 获取高度

                        /**
                         *
                         */
                        backWidthAndHeightCallBack.backWidthAndHeight(width,height);
                    }
                });
    }

    public static void getViewRealWidth4(final View view, final BackWidthAndHeightCallBack backWidthAndHeightCallBack){
        view.post(new Runnable() {

            @Override
            public void run() {
                int width=view.getWidth(); // 获取宽度
                int height=view.getHeight(); // 获取高度

                /**
                 *
                 */
                backWidthAndHeightCallBack.backWidthAndHeight(width,height);
            }
        });
    }


    public  interface  BackWidthAndHeightCallBack{
       void backWidthAndHeight(int width,int height);
    }
}
