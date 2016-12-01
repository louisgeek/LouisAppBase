package com.louisgeek.louisappbase.lrucache;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.louisgeek.httplib.OkHttpClientSingleton;
import com.louisgeek.httplib.callback.SimpleStringOkHttpCallback;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

/**
 * Created by louisgeek on 2016/11/9.
 */

public class LruCacheSimpleTool {
  public  static void  loadImage(Context context, String imageUrl, final ImageView imageView){
      LruCacheBitmapUitl.initFirst(context);
      //
      final Bitmap bitmap=LruCacheBitmapUitl.getBitmapCacheLevels_Enter(imageUrl);
      if (bitmap!=null){
          imageView.setImageBitmap(bitmap);
      }else{
          LruCacheBitmapUitl.getBitmapFromUrlAndSaveCacheToDisk(imageUrl, new LruCacheBitmapUitl.OnLoadImageCallBack() {
              @Override
              public void onSuccess(Bitmap bitmap) {
                  imageView.setImageBitmap(bitmap);
              }

              @Override
              public void onError() {

              }
          });
      }
  }
    public  static void  loadString(Context context, final String keyRaw, final OnRetrunStringCallBack onRetrunStringCallBack) {
        LruCacheStringUitl.initFirst(context);
        //
        final String value = LruCacheStringUitl.getStingCacheLevels(keyRaw);
        if (value != null) {
            KLog.d("value:" + value);
            ThreadUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRetrunStringCallBack.onReturnString(value);
                }
            });
        } else {
            OkHttpClientSingleton.getInstance().doPostAsync(keyRaw, new SimpleStringOkHttpCallback() {
                @Override
                public void OnSuccess(final String result, int statusCode) {
                    LruCacheStringUitl.saveCacheToDisk(keyRaw, result);
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRetrunStringCallBack.onReturnString(result);
                        }
                    });
                }
            });
        }
    }

    public interface  OnRetrunStringCallBack{
       void onReturnString(String backStr);
    }
}
