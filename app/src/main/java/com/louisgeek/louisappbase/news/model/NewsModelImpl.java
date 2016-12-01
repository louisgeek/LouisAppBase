package com.louisgeek.louisappbase.news.model;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.louisgeek.httplib.OkHttpClientSingleton;
import com.louisgeek.httplib.callback.SimpleGsonOkHttpCallback;
import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.data.UrlApis;
import com.louisgeek.louisappbase.lrucache.LruCacheNormalLogicTool;
import com.louisgeek.louisappbase.lrucache.LruCacheStringUitl;
import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

import static com.louisgeek.louisappbase.base.ShowAPIBaseBean.fromJson;

/**
 * Created by louisgeek on 2016/11/5.
 */

public class NewsModelImpl implements INewsModel {


    @Override
    public void loadNewsChannel(final OnLoadNewsChannelListener onLoadNewsChannelListener) {
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("showapi_appid", UrlApis.showapi_appid);
        paramMap.put("showapi_sign", UrlApis.showapi_sign_simple);

        final String withoutTimeKey = UrlApis.newsChannelUrl;

      LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
        @Override
        public void onCacheLogic(final String keyRawLazy) {

            //
            final String result = LruCacheStringUitl.getStingCacheLevels(keyRawLazy);
            if (result != null) {
                KLog.json("result:" + result);
                TypeToken<ShowAPIBaseBean<NewsChannelBean>> typeToken = new TypeToken<ShowAPIBaseBean<NewsChannelBean>>() {
                };
                final ShowAPIBaseBean<NewsChannelBean> newsChannelBeanShowAPIBaseBean = ShowAPIBaseBean.fromJson(result, typeToken);
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(KooApplication.getAppContext(), "频道数据来自缓存", Toast.LENGTH_SHORT).show();
                        onLoadNewsChannelListener.onSuccess(newsChannelBeanShowAPIBaseBean);
                    }
                });
            } else {
                //
                OkHttpClientSingleton.getInstance().doPostAsync(UrlApis.newsChannelUrl, paramMap, new SimpleGsonOkHttpCallback<ShowAPIBaseBean<NewsChannelBean>>() {
                    @Override
                    public ShowAPIBaseBean<NewsChannelBean> OnSuccess(String result, int statusCode) {
                        KLog.d("newsChannelUrl:" + result);
                        LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy,result,withoutTimeKey);

                        //
                        TypeToken<ShowAPIBaseBean<NewsChannelBean>> typeToken = new TypeToken<ShowAPIBaseBean<NewsChannelBean>>() {
                        };
                        return ShowAPIBaseBean.fromJson(result, typeToken);
                    }

                    @Override
                    public void OnSuccessNotifyUI(ShowAPIBaseBean<NewsChannelBean> newsChannelBeanShowAPIBaseBean) {
                        super.OnSuccessNotifyUI(newsChannelBeanShowAPIBaseBean);
                        Toast.makeText(KooApplication.getAppContext(), "频道数据来自网络", Toast.LENGTH_SHORT).show();
                        onLoadNewsChannelListener.onSuccess(newsChannelBeanShowAPIBaseBean);
                    }

                    @Override
                    public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                        super.OnErrorNotifyUI(errorMsg, statusCode);
                        KLog.e(statusCode+errorMsg);
                        //Toast.makeText(KooApplication.getAppContext(), "频道数据获取异常"+errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            //
        }
    });

    }

    @Override
    public void loadNewsList(NewsChannelBean.ChannelListBean channelListBean, int page, final OnLoadNewsListListener onLoadNewsListListener) {
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("showapi_appid", UrlApis.showapi_appid);
        paramMap.put("showapi_sign", UrlApis.showapi_sign_simple);

        //KLog.d("channelListBean:" + channelListBean);

        paramMap.put("channelId", channelListBean.getChannelId());
        paramMap.put("page", String.valueOf(page));
        paramMap.put("needHtml", "1");
        paramMap.put("needContent", "1");
        paramMap.put("needAllList", "0");//有问题 先不要


        final String withoutTimeKey = UrlApis.newsListUrl + "?channelId=" + channelListBean.getChannelId() + "&page=" + page;
        LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
            @Override
            public void onCacheLogic(final String keyRawLazy) {
    ////
                final String result = LruCacheStringUitl.getStingCacheLevels(keyRawLazy);
                if (result != null && !result.equals("")) {
                    KLog.json("result:" + result);
                    TypeToken<ShowAPIBaseBean<NewsListBean>> typeToken = new TypeToken<ShowAPIBaseBean<NewsListBean>>() {
                    };
                    final ShowAPIBaseBean<NewsListBean> showAPIBaseBean = fromJson(result, typeToken);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(KooApplication.getAppContext(), "新闻数据来自缓存", Toast.LENGTH_SHORT).show();
                            onLoadNewsListListener.onSuccess(showAPIBaseBean);
                        }
                    });
                } else {
                    //

                    OkHttpClientSingleton.getInstance().doPostAsync(UrlApis.newsListUrl, paramMap, new SimpleGsonOkHttpCallback<ShowAPIBaseBean<NewsListBean>>() {
                        @Override
                        public ShowAPIBaseBean<NewsListBean> OnSuccess(String result, int statusCode) {

                            LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy,result,withoutTimeKey);
                            // KLog.json(result);
                            TypeToken<ShowAPIBaseBean<NewsListBean>> typeToken = new TypeToken<ShowAPIBaseBean<NewsListBean>>() {
                            };
                            return ShowAPIBaseBean.fromJson(result, typeToken);
                        }

                        @Override
                        public void OnSuccessNotifyUI(ShowAPIBaseBean<NewsListBean> showAPIBaseBean) {
                            super.OnSuccessNotifyUI(showAPIBaseBean);
                            Toast.makeText(KooApplication.getAppContext(), "新闻数据来自网络", Toast.LENGTH_SHORT).show();
                            onLoadNewsListListener.onSuccess(showAPIBaseBean);
                        }
                        @Override
                        public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                            super.OnErrorNotifyUI(errorMsg, statusCode);
                            KLog.e(statusCode+errorMsg);
                           // Toast.makeText(KooApplication.getAppContext(), "新闻数据获取异常"+errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                /////
            }
        });

    }

    @Override
    public void loadLocalNewsArea(final OnloadLocalNewsAreaListener onloadLocalNewsAreaListener) {
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("showapi_appid", UrlApis.showapi_appid);
        paramMap.put("showapi_sign", UrlApis.showapi_sign_simple);

        final String withoutTimeKey = UrlApis.localNewsAreaUrl;

        LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
            @Override
            public void onCacheLogic(final String keyRawLazy) {

                final  String result=LruCacheStringUitl.getStingCacheLevels(keyRawLazy);

                if (result != null && !result.equals("")) {
                    KLog.json("result:" + result);
                    TypeToken<ShowAPIBaseBean<LocalNews_AreaBean>> typeToken = new TypeToken<ShowAPIBaseBean<LocalNews_AreaBean>>() {
                    };
                    final ShowAPIBaseBean<LocalNews_AreaBean> localNews_areaBeanShowAPIBaseBean= ShowAPIBaseBean.fromJson(result, typeToken);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(KooApplication.getAppContext(), "地区数据来自缓存", Toast.LENGTH_SHORT).show();
                            onloadLocalNewsAreaListener.onSuccess(localNews_areaBeanShowAPIBaseBean);
                        }
                    });
                }else{
                    OkHttpClientSingleton.getInstance().doPostAsync(UrlApis.localNewsAreaUrl, paramMap, new SimpleGsonOkHttpCallback<ShowAPIBaseBean<LocalNews_AreaBean>>() {
                        @Override
                        public ShowAPIBaseBean<LocalNews_AreaBean> OnSuccess(String result, int statusCode) {
                            //
                            LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy,result,withoutTimeKey);
                            //
                            TypeToken<ShowAPIBaseBean<LocalNews_AreaBean>> typeToken = new TypeToken<ShowAPIBaseBean<LocalNews_AreaBean>>() {
                            };
                            return ShowAPIBaseBean.fromJson(result, typeToken);
                        }

                        @Override
                        public void OnSuccessNotifyUI(ShowAPIBaseBean<LocalNews_AreaBean> localNews_areaBeanShowAPIBaseBean) {
                            super.OnSuccessNotifyUI(localNews_areaBeanShowAPIBaseBean);
                            Toast.makeText(KooApplication.getAppContext(), "地区数据来自网络", Toast.LENGTH_SHORT).show();
                            onloadLocalNewsAreaListener.onSuccess(localNews_areaBeanShowAPIBaseBean);
                        }
                        @Override
                        public void OnErrorNotifyUI(String errorMsg, int statusCode) {
                            super.OnErrorNotifyUI(errorMsg, statusCode);
                            KLog.e(statusCode+errorMsg);
                            //Toast.makeText(KooApplication.getAppContext(), "地区数据获取异常"+errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });



    }

    @Override
    public void loadLocalNewsList(LocalNews_AreaBean.CityListBean cityListBean, int page, final OnloadLocalNewsListListener onloadLocalNewsListListener) {
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("showapi_appid", UrlApis.showapi_appid);
        paramMap.put("showapi_sign", UrlApis.showapi_sign_simple);


        paramMap.put("areaId", cityListBean.getAreaId());
        paramMap.put("page", String.valueOf(page));


        final String withoutTimeKey = UrlApis.localNewsListUrl+"?areaId="+cityListBean.getAreaId()+"&page="+page;

        LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
            @Override
            public void onCacheLogic(final String keyRawLazy) {

                final  String result=LruCacheStringUitl.getStingCacheLevels(keyRawLazy);

                if (result != null && !result.equals("")) {
                    KLog.json("result:" + result);
                    TypeToken<ShowAPIBaseBean<LocalNews_NewsListBean>> typeToken = new TypeToken<ShowAPIBaseBean<LocalNews_NewsListBean>>() {
                    };
                    final ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean= ShowAPIBaseBean.fromJson(result, typeToken);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onloadLocalNewsListListener.onSuccess(showAPIBaseBean);
                        }
                    });
                }else{
                    OkHttpClientSingleton.getInstance().doPostAsync(UrlApis.localNewsListUrl, paramMap, new SimpleGsonOkHttpCallback<ShowAPIBaseBean<LocalNews_NewsListBean>>() {
                        @Override
                        public ShowAPIBaseBean<LocalNews_NewsListBean> OnSuccess(String result, int statusCode) {
                            //
                            LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy,result,withoutTimeKey);
                            //
                            TypeToken<ShowAPIBaseBean<LocalNews_NewsListBean>> typeToken = new TypeToken<ShowAPIBaseBean<LocalNews_NewsListBean>>() {
                            };
                            return ShowAPIBaseBean.fromJson(result, typeToken);
                        }

                        @Override
                        public void OnSuccessNotifyUI(ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean) {
                            super.OnSuccessNotifyUI(showAPIBaseBean);
                            onloadLocalNewsListListener.onSuccess(showAPIBaseBean);
                        }
                    });

                }

            }
        });


    }




}
