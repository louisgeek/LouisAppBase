package com.louisgeek.louisappbase.jokes.model;

import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.louisgeek.httplib.OkHttpClientSingleton;
import com.louisgeek.httplib.callback.SimpleGsonOkHttpCallback;
import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.base.JuHeBaseBean;
import com.louisgeek.louisappbase.data.UrlApis;
import com.louisgeek.louisappbase.jokes.bean.Juhe_JokeImageAndTextListBean;
import com.louisgeek.louisappbase.lrucache.LruCacheNormalLogicTool;
import com.louisgeek.louisappbase.lrucache.LruCacheStringUitl;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by louisgeek on 2016/11/6.
 */

public class JuheJokeModelImpl implements IJuheJokeModel {

    public static  final int SORT_DESC=1;
    public static  final int SORT_ASC=2;
    @Override
    public void loadJuheJokeData(String type,String time,int sort, int page, final OnLoadJuheJokeDataListener onLoadJuheJokeDataListener) {
        String sortStr="desc";
        switch (sort){
            case SORT_DESC:
                sortStr="desc";
                break;
            case SORT_ASC:
                sortStr="asc";
                break;

        }
        /**
         * 必须 是  GET  请求
         * 	sort	string	必填	类型，desc:指定时间之前发布的，asc:指定时间之后发布的
         page	int	否必填	当前页数,默认1
         pagesize	int	否必填	每次返回条数,默认1,最大20
         time	string	必填	时间戳（10位），如：1418816972
         key	string	必填	您申请的key
         */

        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("sort", sortStr);//指定时间之前发布的
        //paramMap.put("sort", "asc");//指定时间之后发布的
        paramMap.put("time", String.valueOf(System.currentTimeMillis()/1000));
        paramMap.put("key", UrlApis.JUHE_JOKE_APPKEY);
        //
        paramMap.put("page", String.valueOf(page));
        paramMap.put("pagesize", "10");//每页条数


        final String withoutTimeKey = UrlApis.juhe_ImageJokeListUrl + "?sort="+sortStr+"&page=" + page;

        final String finalApiUrl = UrlApis.juhe_ImageJokeListUrl;
        LruCacheNormalLogicTool.findCacheLogic(withoutTimeKey, new LruCacheNormalLogicTool.OnCacheLogicCallback() {
            @Override
            public void onCacheLogic(final String keyRawLazy) {

                //
                final String result = LruCacheStringUitl.getStingCacheLevels(keyRawLazy);
                if (result != null) {
                    KLog.json("result:" + result);
                    TypeToken<JuHeBaseBean<Juhe_JokeImageAndTextListBean>> typeToken = new TypeToken<JuHeBaseBean<Juhe_JokeImageAndTextListBean>>() {
                    };
                    final JuHeBaseBean<Juhe_JokeImageAndTextListBean> juHeBaseBean = JuHeBaseBean.fromJson(result, typeToken);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(KooApplication.getAppContext(), "笑话数据来自缓存", Toast.LENGTH_SHORT).show();
                            onLoadJuheJokeDataListener.onSuccess(juHeBaseBean);
                        }
                    });
                } else {

                    OkHttpClientSingleton.getInstance().doGetAsync(finalApiUrl, paramMap, new SimpleGsonOkHttpCallback<JuHeBaseBean<Juhe_JokeImageAndTextListBean>>() {
                        @Override
                        public JuHeBaseBean<Juhe_JokeImageAndTextListBean> OnSuccess(String result, int statusCode) {
                            LruCacheNormalLogicTool.saveCacheMaybeIn_Http_back_OnSuccess(keyRawLazy, result, withoutTimeKey);

                            TypeToken<JuHeBaseBean<Juhe_JokeImageAndTextListBean>> typeToken = new TypeToken<JuHeBaseBean<Juhe_JokeImageAndTextListBean>>() {
                            };
                            return JuHeBaseBean.fromJson(result, typeToken);
                        }

                        @Override
                        public void OnSuccessNotifyUI(JuHeBaseBean<Juhe_JokeImageAndTextListBean> juHeBaseBean) {
                            super.OnSuccessNotifyUI(juHeBaseBean);
                            Toast.makeText(KooApplication.getAppContext(), "笑话数据来自网络", Toast.LENGTH_SHORT).show();
                            onLoadJuheJokeDataListener.onSuccess(juHeBaseBean);
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
