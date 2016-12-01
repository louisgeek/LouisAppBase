package com.louisgeek.louisappbase.jokes.model;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.louisgeek.httplib.OkHttpClientSingleton;
import com.louisgeek.httplib.callback.SimpleGsonOkHttpCallback;
import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.data.UrlApis;
import com.louisgeek.louisappbase.jokes.MainJokeFragment;
import com.louisgeek.louisappbase.jokes.bean.JokeImageAndTextListBean;
import com.louisgeek.louisappbase.lrucache.LruCacheNormalLogicTool;
import com.louisgeek.louisappbase.lrucache.LruCacheStringUitl;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louisgeek on 2016/11/6.
 */

public class JokeModelImpl implements IJokeModel {
    @Override
    public void loadJokeData(String type, int page, final OnLoadJokeDataListener onLoadJokeDataListener) {
        String apiUrl = "";
        switch (Integer.valueOf(type)) {
            case MainJokeFragment.GIF_IMAGE:
                apiUrl = UrlApis.jokeGifImageListUrl;
                break;
            case MainJokeFragment.NORMAL_IMAGE:
                apiUrl = UrlApis.jokeImageListUrl;
                break;
            case MainJokeFragment.TEXT:
                apiUrl = UrlApis.jokeTextListUrl;
                break;
        }
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("showapi_appid", UrlApis.showapi_appid);
        paramMap.put("showapi_sign", UrlApis.showapi_sign_simple);

        //
        paramMap.put("page", String.valueOf(page));


        final String withoutTimeKey = apiUrl + "?page=" + page;

        final String finalApiUrl = apiUrl;
        LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
            @Override
            public void onCacheLogic(final String keyRawLazy) {

                //
                final String result = LruCacheStringUitl.getStingCacheLevels(keyRawLazy);
                if (result != null) {
                    KLog.json("result:" + result);
                    TypeToken<ShowAPIBaseBean<JokeImageAndTextListBean>> typeToken = new TypeToken<ShowAPIBaseBean<JokeImageAndTextListBean>>() {
                    };
                    final ShowAPIBaseBean<JokeImageAndTextListBean> showAPIBaseBean = ShowAPIBaseBean.fromJson(result, typeToken);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(KooApplication.getAppContext(), "笑话数据来自缓存", Toast.LENGTH_SHORT).show();
                            onLoadJokeDataListener.onSuccess(showAPIBaseBean);
                        }
                    });
                } else {

                    OkHttpClientSingleton.getInstance().doPostAsync(finalApiUrl, paramMap, new SimpleGsonOkHttpCallback<ShowAPIBaseBean<JokeImageAndTextListBean>>() {
                        @Override
                        public ShowAPIBaseBean<JokeImageAndTextListBean> OnSuccess(String result, int statusCode) {
                            LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy, result, withoutTimeKey);

                            TypeToken<ShowAPIBaseBean<JokeImageAndTextListBean>> typeToken = new TypeToken<ShowAPIBaseBean<JokeImageAndTextListBean>>() {
                            };
                            return ShowAPIBaseBean.fromJson(result, typeToken);
                        }

                        @Override
                        public void OnSuccessNotifyUI(ShowAPIBaseBean<JokeImageAndTextListBean> showAPIBaseBean) {
                            super.OnSuccessNotifyUI(showAPIBaseBean);
                            Toast.makeText(KooApplication.getAppContext(), "笑话数据来自网络", Toast.LENGTH_SHORT).show();
                            onLoadJokeDataListener.onSuccess(showAPIBaseBean);
                        }
                        @Override
                        public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                            super.OnErrorNotifyUI(errorMsg, statusCode);
                            KLog.e(statusCode+errorMsg);
                            //Toast.makeText(KooApplication.getAppContext(), "笑话数据获取异常"+errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                //
            }
        });
    }
}
