package com.louisgeek.louisappbase.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.socks.library.KLog;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 网络工具类
 *
 * @Author louisgeek
 * 2016-1-15 9:11:03
 */
public class NetWorkTool {

    private static final String TAG = "NetWorkTool";

    public static String getNetworkType(Context context) {
        String strNetworkType = "";

        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.d(TAG, "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                Log.d(TAG, "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }
        Log.d(TAG, "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    //只判断网络连接是否可用
    public static boolean isNetWorkConnected(Context context) {
        boolean isConnection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                isConnection = true;
            }
        }
        return isConnection;
    }

    //只判断网络连接是否可用
    public static boolean isNetWorkConnected_Wifi(Context context) {
        boolean isConnection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    isConnection = true;
                }
            }
        }
        return isConnection;
    }

    //只判断网络连接是否可用
    public static boolean isNetWorkConnected_Moblie(Context context) {
        boolean isConnection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    isConnection = true;
                }
            }
        }
        return isConnection;
    }

    /**
     * 判断是否真正有外网连接
     * 设置超时时间短  获取的结果不一定准确
     * 设置超时时间长  如果没网就阻塞了
     * 常用于提示“网络好像有点问题。。。”
     *
     * 推荐小于请求网络工具的超时时间
     *
     */
    private static final boolean isCanRealAccessInternet(final Context context) {
        if (!isNetWorkConnected(context)) {
            return false;
        }
        try {
            /**
             * 超时时间  10秒钟
             */
            if (executePingCommand(10*1000)==0){
                KLog.d("successsss");
                return true;
            }else {
                return false;
            }
        } catch (IOException e) {
            //e.printStackTrace();
            KLog.w("IOException");
            return false;
        } catch (InterruptedException e) {
            //e.printStackTrace();
            KLog.w("InterruptedException");
            return false;
        } catch (TimeoutException e) {
           // e.printStackTrace();
            KLog.w("TimeoutException");
            return false;
        }
    }
    /**
     * 判断是否真正有外网连接
     * 设置超时时间短  获取的结果不一定准确
     * 设置超时时间长  如果没网就阻塞了
     * 常用于提示“网络好像有点问题。。。”
     *
     * 推荐小于请求网络工具的超时时间
     */
    private static final boolean isCanRealAccessInternetTwo(final Context context) {
        if (!isNetWorkConnected(context)) {
            return false;
        }
        /**
         * 超时时间   10秒钟
         */
            if (executePingCommandTwo(10*1000)){
                return true;
            }else {
                return false;
            }
    }

    /**
     * 执行ping cmd
     *
     * @param timeout 自定义检测超时时间
     * @return 返回 0 表示成功
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public static int executePingCommand(long timeout) throws IOException, InterruptedException, TimeoutException {
        Runtime runtime = Runtime.getRuntime();
        String url_ip = "www.qq.com";
        Process process = runtime.exec("ping -c 2 -w 50 " + url_ip);
        PingWorkerThread worker = new PingWorkerThread(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null) {
                return worker.exit;
            } else {
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } finally {
            process.destroy();
        }
    }

    private static class PingWorkerThread extends Thread {
        private final Process process;
        private Integer exit;

        public PingWorkerThread(Process process) {
            this.process = process;
        }

        @Override
        public void run() {
            try {
                exit = process.waitFor();
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    /**
     * 执行ping cmd 和第一种差不多
     * @param timeout
     * @return
     */
    public static boolean executePingCommandTwo(int timeout) {
        boolean isSuccess = false;
        Process process = null;
        try {
            String url_ip = "www.qq.com";
            process = Runtime.getRuntime().exec("ping -c 2 -w 50 " + url_ip);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ProcessWithTimeout processWithTimeout = new ProcessWithTimeout(process);
        int exitCode = processWithTimeout.waitForProcess(timeout);

        if (exitCode == Integer.MIN_VALUE) {
            // Timeout
            KLog.w("Timeout");
            isSuccess = false;
        } else {
            // No timeout !
            KLog.w("success !");
            isSuccess = true;
        }
        return isSuccess;
    }

    private static class ProcessWithTimeout extends Thread {
        private Process m_process;
        private int m_exitCode = Integer.MIN_VALUE;

        public ProcessWithTimeout(Process p_process) {
            m_process = p_process;
        }

        public int waitForProcess(int p_timeoutMilliseconds) {
            this.start();

            try {
                this.join(p_timeoutMilliseconds);
            } catch (InterruptedException e) {
                this.interrupt();
            }

            return m_exitCode;
        }

        @Override
        public void run() {
            try {
                m_exitCode = m_process.waitFor();
            } catch (InterruptedException e) {
                // do nothing
                Log.e(TAG, e.getMessage());
            } catch (Exception ex) {
                // Unexpected exception
                Log.e(TAG, ex.getMessage());
            }
        }
    }
    public static void checkInternetState(final Context context, final OnInternetCallBack onInternetCallBack){
        KLog.d("checkInternetState start");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final boolean isCan=isCanRealAccessInternet(context);
                KLog.d("checkInternetState end:isCan"+isCan);
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onInternetCallBack.onInternetBack(isCan);
                    }
                });
            }
        }).start();
    }

    public  interface OnInternetCallBack{
        void onInternetBack(boolean isAccess);
    }

}
