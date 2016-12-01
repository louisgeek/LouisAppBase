package com.louisgeek.louisappbase;

import android.app.Application;
import android.content.Context;

import com.socks.library.KLog;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Created by louisgeek on 2016/11/1.
 */

public class KooApplication extends Application{
    private  static boolean mDebug;
    private static KooApplication instance;
    private static Context mAppContext;
    private  boolean  isX5InitedSuccess;

    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * from gradle config
         */
        mDebug=BuildConfig.LOG_DEBUG;

        //全局tag Koo
        KLog.init(mDebug, "Koo");

        instance=this;
        mAppContext=getApplicationContext();

        /**
         *App 首次就可以加载 x5 内核
         App 在启动后（例如在 Application 的 onCreate 中）立刻调用 QbSdk 的预加载接口 initX5Environment ，可参考接入示例，
         第一个参数传入 context，第二个参数传入 callback，不需要 callback 的可以传入 null，initX5Environment
         内部会创建一个线程向后台查询当前可用内核版本号，这个函数内是异步执行所以不会阻塞 App 主线程，这个函数
         内是轻量级执行所以对 App 启动性能没有影响，当 App 后续创建 webview 时就可以首次加载 x5 内核了
         */
        QbSdk.initX5Environment(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                KLog.d("QbSdk app onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean b) {
                KLog.d("QbSdk app onViewInitFinished "+b);
                isX5InitedSuccess=b;
            }
        });
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                KLog.d("QbSdk onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                KLog.d("QbSdk onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                KLog.d("QbSdk onDownloadProgress:"+i);
            }
        });

       // KLog.d("KooApplication onCreate");
    }

    public static boolean isDebug() {
        return mDebug;
    }


    public static KooApplication getInstance() {
        return instance;
    }
    public static Context getAppContext() {
        return mAppContext;
    }


    public boolean isX5InitedSuccess() {
        return isX5InitedSuccess;
    }

    /**
     * 当终止应用程序对象时调用，不保证一定被调用，当程序是被内核终止以便为其他应用程序释放资源，那么将不会提醒，
     * 并且不调用应用程序的对象的onTerminate方法而直接终止进程
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        // 程序终止的时候执行
        KLog.d("KooApplication onTerminate");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候执行
        KLog.d("KooApplication onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 程序在内存清理的时候执行
       // KLog.d("KooApplication onTrimMemory");
    }


}
