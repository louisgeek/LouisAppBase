package com.louisgeek.louisappbase.webviews;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.custom.OkDialogFragment;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.socks.library.KLog;
import com.tencent.smtt.export.external.interfaces.HttpAuthHandler;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


public class X5WebViewActivity extends BaseAppCompatAty {

    private String mHomeUrl = "file:///android_asset/test.html";
    private String mUrl = "http://qq.com";
    private WebView mX5webview;
    private boolean isX5Inited;
    ProgressDialog mProgressDialog;
    /**
     * 输入法设置
     * 避免输入法界面弹出后遮挡输入光标的问题
     * <p>
     * 方法一：在AndroidManifest.xml中设置
     * android:windowSoftInputMode="stateHidden|adjustResize"
     * 方法二：在代码中动态设置：
     * getWindow().setSoftInputMode(WindowManager.LayoutParams.
     * <p>
     * SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.
     * <p>
     * SOFT_INPUT_STATE_HIDDEN);
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //##setContentView(R.layout.activity_x5_webview);
        //
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //
        getWindow().setFormat(PixelFormat.TRANSLUCENT);//（这个对宿主没什么影响，建议声明）


    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_x5_webview;
    }

    @Override
    protected int setupToolbarResId() {
        return R.id.id_toolbar;
    }

    @Override
    protected int setupCollapsingToolbarLayoutResId() {
        return 0;
    }

    @Override
    protected int setupDrawerLayoutResId() {
        return 0;
    }

    @Override
    protected int setupNavigationViewResId() {
        return 0;
    }

    @Override
    protected int setupMenuResId() {
        return 0;
    }

    @Override
    protected TabHostShowData setupTabHostData() {
        return null;
    }

    @Override
    protected void initView() {
        //start
        //## mHandler.post(mRunnable);

        //一些初始化
        initProgressBar();

        initX5Webview();
    }

    private void initProgressBar() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("loading");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /**
     * 循环执行
     */
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if (!isX5Inited) {
                KLog.d("QbSdk post");
                isX5Inited = KooApplication.getInstance().isX5InitedSuccess();
            } else {
                //完成后
                mHandler.removeCallbacks(mRunnable);
                KLog.d("QbSdk removeCallbacks");

            }

            //再次调用此Runnable对象，以实现每xxx 一次的定时器操作
            mHandler.postDelayed(this, 1000);
            //mHandler.post(this);
        }
    };

    @Override
    protected boolean needEventBus() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
    }

    @Override
    protected boolean setupToolbarTitleCenter() {
        return true;
    }

    @Override
    protected void onNavigationViewItemSelected(MenuItem menuItem) {

    }

    @Override
    protected boolean onOptionsItemSelectedOutter(MenuItem menuItem, boolean superBackValue) {
        return superBackValue;
    }

    private void initX5Webview() {

        //WebView x5webview=new WebView(this);
        mX5webview = (WebView) findViewById(R.id.id_x5webview);


        if (mX5webview.getX5WebViewExtension() == null) {
            Toast.makeText(mContext, "X5 内核未真正加载！", Toast.LENGTH_SHORT).show();
        }
        /**
         *WebSettings
         */
        WebSettings webSetting = mX5webview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0)
                .getPath());
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        //webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // webSetting.setPreFectch(true);

        /**
         * loadUrl
         */
        mUrl = getIntent().getData() == null ? "" : getIntent().getData().toString();
        if (mUrl != null && !mUrl.equals("")) {
            //
            mX5webview.loadUrl(mUrl);
        } else {
            mX5webview.loadUrl(mHomeUrl);
        }

        /**
         * javascriptInterfaceHelper 别名要和js里面的一致
         */
        mX5webview.addJavascriptInterface(new JavascriptInterfaceHelper(X5WebViewActivity.this),"javascriptInterfaceHelper");


        //
        mX5webview.setWebViewClient(new WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                //return super.shouldOverrideUrlLoading(webView, url);
                webView.loadUrl(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                mProgressDialog.show();
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                super.onPageFinished(webView, url);
                mProgressDialog.dismiss();
                /**
                 * 有链接也会add
                 */
                    String javascriptInterfaceHelper_openImageShow_NeedAddJsStr=
                            "javascript:(function(){"
                            + "  var objs = document.getElementsByTagName(\"img\"); "
                            + "  for(var i=0;i<objs.length;i++){"
                            + "     objs[i].onclick=function(){"
                            + "          window.javascriptInterfaceHelper.jsWillCallJavaFun_openImageShow(this.src);"
                            + "     }"
                            + "  }"
                            + "})()";
               // KLog.d(javascriptInterfaceHelper_openImageShow_NeedAddJsStr);
                //###mX5webview.loadUrl(javascriptInterfaceHelper_openImageShow_NeedAddJsStr);
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView webView, HttpAuthHandler httpAuthHandler, String s, String s1) {
                super.onReceivedHttpAuthRequest(webView, httpAuthHandler, s, s1);
                boolean flag = httpAuthHandler.useHttpAuthUsernamePassword();
                Log.i("xxx", "useHttpAuthUsernamePassword is" + flag);
                //  Log.i("xxx", "HttpAuth host is" + host);
            }

            @Override
            public void onDetectedBlankScreen(String s, int i) {
                super.onDetectedBlankScreen(s, i);
            }
        });


        mX5webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView webView, String title) {
                super.onReceivedTitle(webView, title);
                kooSetToolbarTitle(title);
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
            }

            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult jsResult) {
                //return super.onJsAlert(webView, url, message, jsResult);
                OkDialogFragment okDialogFragment = OkDialogFragment.newInstance("", message);
                okDialogFragment.show(getSupportFragmentManager(), "onJsAlert");
                jsResult.confirm();
                return true;

            }
        });


        mX5webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        });


        /**
         *
         */
        mX5webview.getView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               WebView.HitTestResult hitTestResult = mX5webview.getHitTestResult();
                final String imagePath = hitTestResult.getExtra();
                switch (hitTestResult.getType()) {
                    //获取点击的标签是否为图片
                    case WebView.HitTestResult.IMAGE_TYPE:
                        // Toast.makeText(getApplicationContext(), "IMAGE_TYPE"+ path, Toast.LENGTH_LONG).show();
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        // Toast.makeText(getApplicationContext(), "SRC_IMAGE_ANCHOR_TYPE"+ path, Toast.LENGTH_LONG).show();

                        showDialog(imagePath);
                        break;
                }

                KLog.d("hitTestResult:" + hitTestResult.getType());
                KLog.d("getView onLongClick:" + v);
                return false;
            }
        });
    }

    private void showDialog(final String path){
        AlertDialog.Builder builder=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("查看图片");
        builder.setPositiveButton("查看", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                ImageShowDataHelper.setDataAndToImageShow(X5WebViewActivity.this,path);
                //
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onBackPressed() {
        //
        if (mX5webview.canGoBack()) {
            mX5webview.goBack();// 返回前一个页面
        } else {
            mX5webview.destroy();
            super.onBackPressed();
        }
    }
}
