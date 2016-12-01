package com.louisgeek.louisappbase.webviews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.custom.OkCancelDialogFragment;
import com.louisgeek.louisappbase.custom.OkCancelPromptDialogFragment;
import com.louisgeek.louisappbase.custom.OkDialogFragment;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.socks.library.KLog;


public class WebViewActivity extends BaseAppCompatAty {
    WebView mWebview;
    private String mHomeUrl = "file:///android_asset/test.html";
    private  String mUrl="http://qq.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_webview);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_webview;
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

         mWebview= (WebView) findViewById(R.id.id_webview);


         initWebView();
    }

    private void initWebView() {
        WebSettings webSetting = mWebview.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);


        /**
         * loadUrl
         */
        mUrl=getIntent().getData()==null?"":getIntent().getData().toString();
        if (mUrl!=null&&!mUrl.equals("")) {
            //
            mWebview.loadUrl(mUrl);
        }else {
            mWebview.loadUrl(mHomeUrl);
        }


        /**
         * javascriptInterfaceHelper 别名要和js里面的一致
         */
        mWebview.addJavascriptInterface(new JavascriptInterfaceHelper(WebViewActivity.this),"javascriptInterfaceHelper");


        mWebview.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
               WebView.HitTestResult hitTestResult= mWebview.getHitTestResult();
                final String imagePath = hitTestResult.getExtra();
                switch (hitTestResult.getType()) {
                    //获取点击的标签是否为图片
                    case WebView.HitTestResult.IMAGE_TYPE:
                        // Toast.makeText(getApplicationContext(), "IMAGE_TYPE"+ path, Toast.LENGTH_LONG).show();
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        // Toast.makeText(getApplicationContext(), "SRC_IMAGE_ANCHOR_TYPE"+ path, Toast.LENGTH_LONG).show();
                        //
                        showDialog(imagePath);
                        break;
                }

                KLog.d("hitTestResult:" + hitTestResult.getType());
                KLog.d("getView onLongClick:" + v);
                return false;
            }
        });

        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
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
                //####mWebview.loadUrl(javascriptInterfaceHelper_openImageShow_NeedAddJsStr);
            }
        });



        mWebview.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                OkDialogFragment okDialogFragment=OkDialogFragment.newInstance("",message);
                okDialogFragment.show(getSupportFragmentManager(),"onJsAlert");
                //return super.onJsAlert(view, url, message, result);
                result.confirm();
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                OkCancelDialogFragment okCancelDialogFragment=OkCancelDialogFragment.newInstance("",message);
                okCancelDialogFragment.show(getSupportFragmentManager(),"onJsConfirm");
                okCancelDialogFragment.setOnBtnClickListener(new OkCancelDialogFragment.OnBtnClickListener() {
                    @Override
                    public void onOkBtnClick(DialogInterface dialogInterface) {
                        result.confirm();
                    }

                    @Override
                    public void onCancelBtnClick(DialogInterface dialogInterface) {
                        result.cancel();
                    }
                });
              //  return super.onJsConfirm(view, url, message, result);
                return true;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                OkCancelPromptDialogFragment okCancelPromptDialogFragment=OkCancelPromptDialogFragment.newInstance("",message);
                okCancelPromptDialogFragment.show(getSupportFragmentManager(),"onJsPrompt");
                okCancelPromptDialogFragment.setOnBtnClickListener(new OkCancelPromptDialogFragment.OnBtnClickListener() {
                    @Override
                    public void onOkBtnClick(DialogInterface dialogInterface,String editTextValue) {
                        result.confirm(editTextValue);
                    }

                    @Override
                    public void onCancelBtnClick(DialogInterface dialogInterface) {
                        result.cancel();
                    }
                });
                // return super.onJsPrompt(view, url, message, defaultValue, result);
                return true;
            }
        });

        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

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
                ImageShowDataHelper.setDataAndToImageShow(WebViewActivity.this,path);
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

    @Override
    public void onBackPressed() {
        if (mWebview.canGoBack()){
            mWebview.goBack();
        }else{
            mWebview.destroy();
            super.onBackPressed();
        }
    }
}
