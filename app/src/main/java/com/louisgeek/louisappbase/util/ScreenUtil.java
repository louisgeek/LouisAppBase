package com.louisgeek.louisappbase.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.socks.library.KLog;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class ScreenUtil {


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;

    }

    public static int getScreenWidth2(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    public static int getScreenHeight2(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * dpi为160时，density为1
     * density = (dpi*1.0)/ 160;
     *
     * @param context
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        KLog.d("displayMetrics.density:" + displayMetrics.density);
        KLog.d("displayMetrics.densityDpi:" + displayMetrics.densityDpi);
        KLog.d("displayMetrics.scaledDensity:" + displayMetrics.scaledDensity);
        KLog.d("displayMetrics.xdpi:" + displayMetrics.xdpi);
        KLog.d("displayMetrics.ydpi:" + displayMetrics.ydpi);
        return displayMetrics.density;
    }

    public static float getScreenDensity2(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.density;
    }

}
