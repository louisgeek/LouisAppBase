package com.louisgeek.louisappbase.lrucache;

import android.content.Context;
import android.util.Log;
import android.util.LruCache;

import com.jakewharton.disklrucache.DiskLruCache;
import com.louisgeek.louisappbase.util.AppTool;
import com.louisgeek.louisappbase.util.MD5Util;
import com.louisgeek.louisappbase.util.SdTool;
import com.socks.library.KLog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * Created by louisgeek on 2016/11/8.
 * <p>
 * * 1 DiskLruCache的本地缓存文件路径为:
 * /sdcard/Android/data/xxxxx/cache/yyyyy
 * 当应用被卸载时该文件亦会被删除
 * 2 新闻类APP一般的做法.从新闻列表(ListView)中点击某条新闻跳转到详细画面.
 * 在详细画面一般有图片和文字.此时可以将图片和文字用同一个key存到不同的缓存文件夹下.
 * 在断网时浏览该新闻时候依据该key分别取出对应的图片和文字即可.
 * 3 在示例中用到了DiskLruCache的存和取的相关API.在此对其余重要API简要整理:
 * 3.1 remove(String key) 删除某个已缓存的对象.
 * 3.2 size() 得到缓存路径下缓存的总大小.这个很常见.许多APP都提示当前有多少缓存.
 * 3.3 flush() 将内存中的操作记录同步到日志文件(也就是journal文件)中
 * 一般在Activity的onPause()中调用该方法.
 * 3.4 close() 关闭DiskLruCache.该方法与open方法对应.
 * 一般在Activity的onDestory()中调用该方法.
 * 3.5 delete() 将缓存数据全部删除.
 */

public class LruCacheStringUitl {
    private static final String TAG = "LruCacheStringUitl";


    /**
     * DISKLRUCACHE_VALUE_COUNT 表示同一个key可以对应多少个缓存文件，一般情况下我们都是传1，这样key和缓存文件一一对应，
     * 查找和移除都会比较方便。
     * <p>
     * DISKLRUCACHE_MAX_SIZE 表示最大可以缓存多少字节的数据。
     */
    private final static int DISKLRUCACHE_VALUE_COUNT = 1;
    private final static long DISKLRUCACHE_MAX_SIZE = 200 * 1024 * 1024;
    private final static String DISKLRUCACHE_STRING_UNIQUE_NAME = "kooStringData";

    /**
     *
     */
    private static LruCache<String, String> mLruCache;
    private static DiskLruCache mDiskLruCache;

    public static void initFirst(Context context) {
        if (mLruCache == null) {
            initLruCache();
        }
        if (mDiskLruCache == null||mDiskLruCache.isClosed()) {
            initDiskLruCache(context);
        }
    }

    private static void initDiskLruCache(Context context) {
        try {
            File diskCacheDir = new File(SdTool.getDiskCacheDirPath(context) + File.separator + DISKLRUCACHE_STRING_UNIQUE_NAME);
            if (!diskCacheDir.exists()) {
                diskCacheDir.mkdirs();
            }
            if (diskCacheDir.getUsableSpace() > DISKLRUCACHE_MAX_SIZE) {
                //剩余空间大于我们指定的磁盘缓存大小
                mDiskLruCache = DiskLruCache.open(diskCacheDir, AppTool.getVersionCode(context),
                        DISKLRUCACHE_VALUE_COUNT, DISKLRUCACHE_MAX_SIZE);
            } else {
                Log.e(TAG, "initDiskLruCache:磁盘大小不足");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initLruCache() {
        //获取当前进程的可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int maxSize = maxMemory / 10;//十分之一
        mLruCache = new LruCache<String, String>(maxSize) {
        };
    }

    /**
     * 常规入口
     * @param keyRaw
     * @return   null mean  has no cache
     */
    public static String getStingCacheLevels(String keyRaw){
        String tempStr=getCacheFromMemory(keyRaw);
        if (tempStr!=null){
            KLog.d("【缓存】：内存："+keyRaw);
            return  tempStr;
        }else {
            tempStr=getCacheFromDiskAndSaveCacheToMemory(keyRaw);
            if (tempStr!=null){
                KLog.d("【缓存】：硬盘："+keyRaw);
                return tempStr;
            }
        }
        return tempStr;
    }
    public static String getCacheFromMemory(String keyRaw) {
        return mLruCache.get(MD5Util.encode(keyRaw));
    }

    private static void saveCacheToMemory(String keyRaw, String value) {
        if (getCacheFromMemory(MD5Util.encode(keyRaw)) == null) {
            //不存在时添加
            mLruCache.put(MD5Util.encode(keyRaw), value);
        }
    }

    public static String getCacheFromDiskAndSaveCacheToMemory(String keyRaw) {
        String tempStr=null;
        try {
            DiskLruCache.Snapshot snapshot = mDiskLruCache.get(MD5Util.encode(keyRaw));
            if (snapshot!=null) {
                InputStream inputStream = snapshot.getInputStream(0);//

                /**
                 *way one
                 */
          /*  StringBuffer stringBuffer = new StringBuffer();
            byte[] buff = new byte[1024];
            int len;
            while ((len = inputStream.read(buff)) != -1) {
                stringBuffer.append(new String(buff, 0, len, "utf-8"));
            }
            tempStr = stringBuffer.toString();*/
/**
 *way two
 */
                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                tempStr = stringBuilder.toString();
                if (tempStr != null) {
                    tempStr = tempStr.trim();
                }
                //
                inputStream.close();
                /**
                 * 加入内存
                 */
                saveCacheToMemory(keyRaw, tempStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempStr;
    }

    public static void saveCacheToDisk(final String keyRaw, final String needSaveString) {
        boolean isSuccess = false;
        DiskLruCache.Editor editor = null;
        try {
            editor = mDiskLruCache.edit(MD5Util.encode(keyRaw));
            if (editor != null) {
                //从Editor中获取即将存放文件的 OutputStream的对象
                OutputStream outputStream = editor.newOutputStream(0);
                //outputStream.write(bytes);

                InputStream  inputStream = new ByteArrayInputStream(needSaveString.getBytes("UTF-8"));
                /**
                 *
                 */
                int length;
                byte[] bytes = new byte[1024];
                while ((length = inputStream.read(bytes)) != -1) {
                    //Log.d(TAG, "saveCacheToDisk: length:"+length);
                    outputStream.write(bytes,0,length);
                }
                isSuccess = true;


                //###   flushCache();
                //
                inputStream.close();
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         *
         */
        try {
            if (isSuccess) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static long getDiskCacheSize() {
        return mDiskLruCache.size();
    }

    public static void closeDiskCacheMaybeOnDestroy() {
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

    public static void flushDiskCacheMaybeOnPause() {
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
}
