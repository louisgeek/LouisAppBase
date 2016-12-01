package com.louisgeek.louisappbase.lrucache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.louisgeek.louisappbase.util.AppTool;
import com.louisgeek.louisappbase.util.MD5Util;
import com.louisgeek.louisappbase.util.SdTool;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by louisgeek on 2016/11/8.
 *
 * * 1 DiskLruCache的本地缓存文件路径为:
 *   /sdcard/Android/data/xxxxx/cache/yyyyy
 *   当应用被卸载时该文件亦会被删除
 * 2 新闻类APP一般的做法.从新闻列表(ListView)中点击某条新闻跳转到详细画面.
 *   在详细画面一般有图片和文字.此时可以将图片和文字用同一个key存到不同的缓存文件夹下.
 *   在断网时浏览该新闻时候依据该key分别取出对应的图片和文字即可.
 * 3 在示例中用到了DiskLruCache的存和取的相关API.在此对其余重要API简要整理:
 *   3.1 remove(String key) 删除某个已缓存的对象.
 *   3.2 size() 得到缓存路径下缓存的总大小.这个很常见.许多APP都提示当前有多少缓存.
 *   3.3 flush() 将内存中的操作记录同步到日志文件(也就是journal文件)中
 *       一般在Activity的onPause()中调用该方法.
 *   3.4 close() 关闭DiskLruCache.该方法与open方法对应.
 *       一般在Activity的onDestory()中调用该方法.
 *   3.5 delete() 将缓存数据全部删除.
 */

public class LruCacheBitmapUitl {
    private static final String TAG = "LruCacheBitmapUitl";


    /**
     * DISKLRUCACHE_VALUE_COUNT 表示同一个key可以对应多少个缓存文件，一般情况下我们都是传1，这样key和缓存文件一一对应，
     * 查找和移除都会比较方便。
     * <p>
     * DISKLRUCACHE_MAX_SIZE 表示最大可以缓存多少字节的数据。
     */
    private final static int DISKLRUCACHE_VALUE_COUNT = 1;
    private final static long DISKLRUCACHE_MAX_SIZE = 300*1024*1024;
    private final static String DISKLRUCACHE_BITMAP_UNIQUE_NAME = "kooBitmapData";


    /**
     *
     */
    private static LruCache<String, Bitmap> mLruCache;
    private static DiskLruCache mDiskLruCache;

    public static void  initFirst(Context context){
        if (mLruCache==null)
        {
            initLruCache();
        }
        if (mDiskLruCache==null||mDiskLruCache.isClosed()) {
            initDiskLruCache(context);
        }
    }

    private static void initDiskLruCache(Context context) {
        try {
            File diskCacheDir = new File(SdTool.getDiskCacheDirPath(context) + File.separator + DISKLRUCACHE_BITMAP_UNIQUE_NAME);
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs();
            }
            if (diskCacheDir.getUsableSpace() > DISKLRUCACHE_MAX_SIZE) {
                //剩余空间大于我们指定的磁盘缓存大小
                mDiskLruCache = DiskLruCache.open(diskCacheDir, AppTool.getVersionCode(context),
                        DISKLRUCACHE_VALUE_COUNT, DISKLRUCACHE_MAX_SIZE);
            }else{
                Log.e(TAG, "initDiskLruCache:磁盘大小不足");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initLruCache() {
        //获取当前进程的可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 8;//可用的八分之一
        KLog.d("maxSize:"+maxSize);
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//得到value对象以字节为单位的大小
            }
        };
    }

    /**
     * 常规入口
     * @param urlStr
     * @return null  mean has no cache
     */
    public static Bitmap getBitmapCacheLevels_Enter(String urlStr){
            Bitmap bitmap=getCacheFromMemory(urlStr);
            if (bitmap!=null){
                KLog.d("【缓存】：内存："+urlStr);
                return bitmap;
            }else {
                bitmap=getCacheFromDisk(urlStr);
                if (bitmap!=null){
                    KLog.d("【缓存】：硬盘："+urlStr);
                    return bitmap;
                }
            }
        return  bitmap;
    }

    public static Bitmap getCacheFromMemory(String keyRaw){
        Bitmap bitmap=mLruCache.get(MD5Util.encode(keyRaw));
      return bitmap;
    }
    public static void saveCacheToMemory(String keyRaw, Bitmap bitmap){
        if (bitmap==null){
            KLog.e("saveCacheToMemory bitmap is null");
            return;
        }
        if (getCacheFromMemory(keyRaw) == null) {
            //不存在时添加
            mLruCache.put(MD5Util.encode(keyRaw), bitmap);
        }
    }
    public static Bitmap getCacheFromDisk(String keyRaw){
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Util.encode(keyRaw));
            if (snapshot!=null) {
                InputStream inputStream = snapshot.getInputStream(0);//
                bitmap = BitmapFactory.decodeStream(inputStream);
                //
                inputStream.close();
            }
        } catch (Exception e) {
        }
        return bitmap;
    }
    public static void saveCacheToDisk(String keyRaw, Bitmap bitmap){
        if (bitmap==null){
            KLog.e("saveCacheToDisk bitmap is null");
            return;
        }

        if (getCacheFromDisk(keyRaw) != null) {
            //不存在时添加
            //获取DiskLruCache的Editor
            DiskLruCache.Editor editor = null;
            boolean isSuccess=false;
            try {
                editor = mDiskLruCache.edit(MD5Util.encode(keyRaw));

                if (editor!=null) {
                    //从Editor中获取OutputStream 用于存放缓存
                    OutputStream outputStream=editor.newOutputStream(0);
                    /**
                     *要写bitmap的字节的内容
                     */
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();
                    //out
                    outputStream.write(bytes);
                    isSuccess=true;

                    outputStream.close();
                    byteArrayOutputStream.close();
                }

                //
            } catch (IOException e) {
                e.printStackTrace();
            }
            //
            if (isSuccess) {
                try {
                    editor.commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    editor.abort();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           // KLog.d("saveCacheToDisk:"+isSuccess);
            //###   flushCache();
        }
    }

    public static Bitmap getCacheFromDiskAndSaveToMemoryCache(String keyRaw){
        Bitmap bitmap =getCacheFromDisk(keyRaw);
        /**
        * 加入内存
        */
        if (bitmap!=null){
        saveCacheToMemory(keyRaw, bitmap);
        }
        return bitmap;
    }
    public static void getBitmapFromUrlAndSaveCacheToDisk(final String urlStr, final OnLoadImageCallBack onLoadImageCallBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //
                DiskLruCache.Editor editor;
                try {
                    KLog.d("getBitmapFromUrlAndSaveCacheToDisk:mDiskLruCache.isClosed()"+mDiskLruCache.isClosed());
                    editor = mDiskLruCache.edit(MD5Util.encode(urlStr));
                    if (editor!=null) {
                        //从Editor中获取即将存放文件的 OutputStream的对象
                        OutputStream outputStream = editor.newOutputStream(0);
                        //outputStream.write(bytes);
                        /**
                         *
                         */
                        if (getBitmapFromUrlAndWrite(urlStr, outputStream)) {
                            editor.commit();
                            ThreadUtil.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    onLoadImageCallBack.onSuccess(getCacheFromDiskAndSaveToMemoryCache(urlStr));
                                }
                            });
                        } else {
                            editor.abort();
                            onLoadImageCallBack.onError();
                        }
                        //###   flushCache();
                        //
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }
    private static boolean getBitmapFromUrlAndWrite(String urlStr,OutputStream needSaveOutputStream4DiskLruCache){
        boolean isSuccess=false;
        HttpURLConnection connection = null;
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            connection.setReadTimeout(5 * 1000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            //打开连接
            connection.connect();
            //获取内容长度
            int contentLength = connection.getContentLength();
            /**
             *
             */
            bufferedInputStream = new BufferedInputStream(connection.getInputStream(), 1024);

            /**
             *
             */
            bufferedOutputStream = new BufferedOutputStream(needSaveOutputStream4DiskLruCache, 1024);

            int temp;
            while ((temp = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(temp);
            }
            /**
             * 存储DiskLruCache到成功
             */

            isSuccess=true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                if (bufferedOutputStream != null) {
                    bufferedOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return  isSuccess;
    }

    @Deprecated //有问题  不保存bitmap  待修改
    public static void getBitmapFromUrlAndSaveCacheToDiskOld(final String urlStr){

        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 * thread begin
                 */
                DiskLruCache.Editor editor = null;
                boolean isSuccess = false;
                HttpURLConnection connection = null;
                BufferedInputStream bufferedInputStream = null;
                BufferedOutputStream bufferedOutputStream = null;
                try {
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5 * 1000);
                    connection.setReadTimeout(5 * 1000);
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Charset", "UTF-8");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    //打开连接
                    connection.connect();
                    //获取内容长度
                    int contentLength = connection.getContentLength();
                    /**
                     *
                     */
                    bufferedInputStream = new BufferedInputStream(connection.getInputStream(), 1024);

                    /**
                     * DiskLruCache
                     */
                    editor = mDiskLruCache.edit(MD5Util.encode(urlStr));
                    if (editor != null) {
                        //从Editor中获取即将存放文件的 OutputStream的对象
                        OutputStream outputStream = editor.newOutputStream(0);
                        /**
                         *用bufferedOutputStream 写
                         */
                        bufferedOutputStream = new BufferedOutputStream(outputStream, 1024);

                        //
                        int temp;
                        while ((temp = bufferedInputStream.read()) != -1) {
                            bufferedOutputStream.write(temp);
                        }
                        //成功
                        isSuccess = true;
                        //
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    try {
                        if (bufferedInputStream != null) {
                            bufferedInputStream.close();
                        }
                        if (bufferedOutputStream != null) {
                            bufferedOutputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                /**
                 *
                 */
                try {
                    if (isSuccess) {
                        Log.d(TAG, "bitmap  run: isSuccess");
                        editor.commit();
                    } else {
                        Log.d(TAG, "bitmap  run: abort");
                        editor.abort();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                /**
                 * thread end
                 */
            }
            }).start();
    }

    public static void removeAllDiskCache() {
        /**
         * 这个方法用于将所有的缓存数据全部删除
         * 其实只需要调用一下DiskLruCache的delete()方法就可以实现了。
         * 会删除包括日志文件在内的所有文件
         */
        try {
            if (mDiskLruCache!=null){
            mDiskLruCache.delete();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static  long getDiskCacheSize() {
        return mDiskLruCache.size();
    }
    public  static void closeDiskCacheMaybeOnDestroy() {
        /**
         * 这个方法用于将DiskLruCache关闭掉，是和open()方法对应的一个方法。
         * 关闭掉了之后就不能再调用DiskLruCache中任何操作缓存数据的方法，
         * 通常只应该在Activity的onDestroy()方法中去调用close()方法。
         */
        try {
            if (mDiskLruCache!=null){
            mDiskLruCache.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  static void flushDiskCacheMaybeOnPause() {
        /**
         * 用于将内存中的操作记录同步到日志文件（也就是journal文件）当中。
         * 通常在Activity的onPause()方法中去调用一次flush()方法就可以了
         */
        try {
            if (mDiskLruCache!=null&&!mDiskLruCache.isClosed()){
            mDiskLruCache.flush();}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  interface  OnLoadImageCallBack{
        void onSuccess(Bitmap bitmap);
        void onError();
    }
    //public OnLoadImageCallBack onLoadImageCallBack;
}
