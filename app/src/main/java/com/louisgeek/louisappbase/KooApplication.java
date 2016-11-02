package com.louisgeek.louisappbase;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Created by louisgeek on 2016/11/1.
 */

public class KooApplication extends Application{
    private  static boolean mDebug;
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * from gradle config
         */
        mDebug=BuildConfig.LOG_DEBUG;

        //全局tag Koo
        KLog.init(mDebug, "Koo");
    }
    public static boolean isDebug() {
        return mDebug;
    }
}
