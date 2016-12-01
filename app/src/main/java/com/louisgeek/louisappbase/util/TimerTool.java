package com.louisgeek.louisappbase.util;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by louisgeek on 2016/11/10.
 */

public class TimerTool {
    private static Timer timer = new Timer();
    //=======
    private static TimerTask timerTask= new TimerTask() {
        public void run() {
              //boolean xxx=  ThreadUtil.isMainThread();
           // KLog.d("isMainThread:"+xxx);

        }
    };
    public static void startTimer() {
        //启动定时器  延迟0    1分钟执行一次
        timer.schedule(timerTask, 0, 1*60*1000);
    }
    public static void endTimer() {
        //停止定时器
        timer.cancel();
    }
    //=======
}
