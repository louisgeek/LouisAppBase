package com.louisgeek.louisappbase.lrucache;

import com.socks.library.KLog;

/**
 * Created by louisgeek on 2016/11/10.
 */

public class LruCacheNormalLogicTool {

    public static void findCacheLogic(String withoutTimeKey, final OnCacheLogicCallback onCacheLogicCallback) {
        String keyRawLazy = null;
        String keyRawLazy_online = getTenMinuteAppendKeyRawLazy(withoutTimeKey);

        /**
         *二级缓存
         */
        keyRawLazy = LruCacheStringUitl.getCacheFromDiskAndSaveCacheToMemory(withoutTimeKey);
        if (keyRawLazy == null) {//缓存里也没有
            KLog.d("缓存里也没有该数据");
            keyRawLazy = keyRawLazy_online;
        }
        /**
         * 核心方法
         */
        onCacheLogicCallback.onCacheLogic(keyRawLazy);
    }

    private static String getTenMinuteAppendKeyRawLazy(String withoutTimeKey) {
        /**
         * 因为api接口数据10分钟更新一次    可以进行10分钟缓存
         */
        final long tenMinute_milliseconds = 10 * 60 * 1000 * 1L;//乘以1L  长整形 不然会当成int类型
        long nowMilliseconds = System.currentTimeMillis();
        final String keyRawLazy = withoutTimeKey + "&kootime=" + nowMilliseconds / tenMinute_milliseconds;
        //KLog.d("keyRawLazy:" + keyRawLazy);
        return keyRawLazy;
    }

    public static void saveCacheMaybeIn_Http_back_OnSuccess(String keyRawLazy, String saveResult, String withoutTimeKey) {
        if (saveResult != null && !saveResult.equals("")) {
            /**
             * 缓存saveResult
             */
            LruCacheStringUitl.saveCacheToDisk(keyRawLazy, saveResult);
            /**
             * 同时缓存存放缓存的key的原 raw
             */
            LruCacheStringUitl.saveCacheToDisk(withoutTimeKey, keyRawLazy);
        }
    }

    public interface OnCacheLogicCallback {
        void onCacheLogic(String keyRawLazy);
    }
}
