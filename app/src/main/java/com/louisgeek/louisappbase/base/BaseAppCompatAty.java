package com.louisgeek.louisappbase.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.custom.FragmentTabHostUnDestroyFragmentSupport;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by louisgeek on 2016/10/31.
 */

public abstract class BaseAppCompatAty extends AppCompatActivity {
    /**
     * LogTag
     */
    protected static String mLogTag;

    /**
     * Context
     */
    protected Context mContext;

    /**
     * Screen information
     */
    protected int mScreenWidth = 0;
    protected int mScreenHeight = 0;
    protected float mScreenDensity = 0.0f;


    protected Toolbar mToolbar;

    protected DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigationView;
    private MenuItem mNavigationViewLastSelectedMenuItem;

    private TextView mToolbar_title_view;

    private CollapsingToolbarLayout mCollapsingToolbarLayout;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setupLayoutId());
        if (needSwipeBack()) {
            /**
             * 需要设置style //背景透明，不设滑动关闭时背景就是黑的。
             * <item name="android:windowIsTranslucent">true</item>
             */
            /**
             *  侧滑返回
             */
            SwipeBackHelper.onCreate(this);
        }
        /**
         *基本信息
         */
        mContext = this;
        mLogTag = this.getClass().getSimpleName();
/**
 *屏幕信息
 */
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        mScreenDensity = displayMetrics.density;
        mScreenWidth = displayMetrics.widthPixels;
        mScreenHeight = displayMetrics.heightPixels;
        KLog.d("mScreenDensity:"+mScreenDensity);
        KLog.d("mScreenWidth:"+mScreenWidth);
        KLog.d("mScreenHeight:"+mScreenHeight);


        if (setupToolbarResId() != 0) {
            initToolbar();
            //DrawerLayout 依托 Toolbar
            if (setupDrawerLayoutResId() != 0) {
                initDrawerLayout();
            }
        }


        if (setupTabHostData()!=null){
            setupTabHost();
        }




        /**
         *last
         */
        initView();
    }


    private void initToolbar() {
        mToolbar = findById(setupToolbarResId());
        if (mToolbar == null) {
            return;
        }
        mToolbar_title_view = findById(mToolbar, R.id.id_toolbar_title);

        if (setupCollapsingToolbarLayoutResId()!=0) {
            mCollapsingToolbarLayout = findById(setupCollapsingToolbarLayoutResId());
        }
        /**
         * setToolbarTitle
         */
        kooSetToolbarTitle(mToolbar.getTitle() != null ? mToolbar.getTitle().toString() : "");

        mToolbar.setVisibility(View.VISIBLE);
        //替换ActionBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }


    }


    /**
     * after initToolbar
     */
    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(setupDrawerLayoutResId());
        if (mDrawerLayout == null) {
            return;
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.base_drawer_open, R.string.base_drawer_close);
        /**同步toolbar与drawerlayout的状态，也就是home的图标会改变,
         该方法会自动和actionBar关联, 将开关的图片显示在了action上，
         如果不设置，也可以有抽屉的效果，不过是默认的图标*/
        mDrawerToggle.syncState();

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        if (setupNavigationViewResId() != 0) {
            mNavigationView = (NavigationView) findViewById(setupNavigationViewResId());
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    /**存放上一次MenuItem */
                    if (mNavigationViewLastSelectedMenuItem != null) {
                        mNavigationViewLastSelectedMenuItem.setChecked(false);
                    }
                    item.setChecked(true);
                    mNavigationViewLastSelectedMenuItem = item;
                    mDrawerLayout.closeDrawers();

                    //***
                    onNavigationViewItemSelected(item);

                    return false;
                }
            });
        }
    }



    private void setupTabHost() {
        //boolean hasCenterBtn=true;
        TabHostShowData tabHostShowData=setupTabHostData();
        //
        if (tabHostShowData==null||tabHostShowData.getFragmentClassesList()==null||tabHostShowData.getFragmentClassesList().size()<=0){
            return;
        }
        FragmentTabHostUnDestroyFragmentSupport mFragmentTabHostUnDestroyFragmentSupport= (FragmentTabHostUnDestroyFragmentSupport) findViewById(android.R.id.tabhost);
        for (int i = 0; i < tabHostShowData.getFragmentClassesList().size(); i++) {
            TabHost.TabSpec tabSpec=mFragmentTabHostUnDestroyFragmentSupport.newTabSpec(getString(tabHostShowData.getImageResIDList().get(i)));
            View indicatorView=buildIndicatorView(getString(tabHostShowData.getTabLabelResIDList().get(i)),
                    tabHostShowData.getImageResIDList().get(i));
            //
          /*   ImageView   id_iv_main_center= (ImageView) findViewById(R.id.id_iv_main_center);
           if (hasCenterBtn){
              id_iv_main_center.setVisibility(View.VISIBLE);
            //R.string.tab_label_add hide
            if (i==2){
                *//**占位*//*
                indicatorView.setVisibility(View.INVISIBLE);
            }
            }else{
                id_iv_main_center.setVisibility(View.GONE);
            }*/
            tabSpec.setIndicator(indicatorView);
            /*另一种FragmentTabHost布局方式见：https://code.csdn.net/snippets/1958337 */
            //mFragmentTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
            mFragmentTabHostUnDestroyFragmentSupport.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
            //Bundle 会传给底层的Fragment的实例创建  可以传递参数 b;传递公共的userid,version,sid   可以为null
            Bundle bundle=new Bundle();
            bundle.putString("name",i+"XX");
            mFragmentTabHostUnDestroyFragmentSupport.addTab(tabSpec,tabHostShowData.getFragmentClassesList().get(i),bundle);
        }
        mFragmentTabHostUnDestroyFragmentSupport.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mFragmentTabHostUnDestroyFragmentSupport.getTabWidget().setBackgroundColor(ContextCompat.getColor(this,android.R.color.white));

        //mFragmentTabHost.getTabWidget().getChildAt()
        /**
         * 当叶卡超过1个的时候才显示底部标记
         */
        if (tabHostShowData.getFragmentClassesList().size()>1){
            mFragmentTabHostUnDestroyFragmentSupport.getTabWidget().setVisibility(View.VISIBLE);
        }else{
            mFragmentTabHostUnDestroyFragmentSupport.getTabWidget().setVisibility(View.GONE);
        }
        mFragmentTabHostUnDestroyFragmentSupport.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                KLog.d("onTabChanged:"+tabId);

            }
        });
    }
    private View buildIndicatorView(String title,int imgResID) {
        View view= LayoutInflater.from(this).inflate(R.layout.base_fragment_tab_host_indicator_normal,null,false);
        ImageView imageView= (ImageView) view.findViewById(R.id.id_iv_indicator);
        TextView textView= (TextView) view.findViewById(R.id.id_tv_indicator);
        imageView.setImageResource(imgResID);
        textView.setText(title);
        return view;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (needEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (needSwipeBack()) {
            SwipeBackHelper.onPostCreate(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (needEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (needSwipeBack()) {
            SwipeBackHelper.onDestroy(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**主界面左上角的icon*/
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        /**
         * 把父类返回值 保存  提供onOptionsItemSelectedOutter使用
         */
        boolean superBackValue=super.onOptionsItemSelected(item);
        /**
         * 实现时候 记得   return superBackValue;
         */
        return  onOptionsItemSelectedOutter(item,superBackValue);
        //return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (setupMenuResId() != 0) {
            getMenuInflater().inflate(setupMenuResId(), menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 返回布局文件Layout Id
     */
    protected abstract int setupLayoutId();

    protected abstract int setupToolbarResId();
    /**
     * 为了兼容 设置了CollapsingToolbarLayout后出现的   Toolbar的title问题
     */
    protected abstract int setupCollapsingToolbarLayoutResId();

    protected abstract int setupDrawerLayoutResId();

    protected abstract int setupNavigationViewResId();

    protected abstract int setupMenuResId();


    protected abstract TabHostShowData setupTabHostData();


    protected abstract void initView();

    protected abstract boolean needEventBus();

    protected abstract boolean needSwipeBack();

    protected abstract boolean setupToolbarTitleCenter();

//    protected abstract KooFragmentPagerAdapter setupKooFragmentPagerAdapter();

    protected abstract void onNavigationViewItemSelected(MenuItem menuItem);

    protected abstract boolean onOptionsItemSelectedOutter(MenuItem menuItem,boolean superBackValue);

    /**
     * Find View
     */
    protected <V extends View> V findById(int resID) {
        return (V) findViewById(resID);
    }

    protected <V extends View> V findById(View view, int id) {
        return (V) view.findViewById(id);
    }


    /**
     *  Snackbar将遍历整个view tree来寻找一个合适的view，可能是
     *  一个CoordinatorLayout ,也可能是window decor’s content view
     */
    /**
     * maybe  set  with CoordinatorLayout
     */
    protected void showSnack(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnack(String msg) {
        showSnack(getRootView(), msg);
    }

    protected void showSnackAndAction(String msg, String actionTitle, View.OnClickListener onClickListener) {
        Snackbar.make(getRootView(), msg, Snackbar.LENGTH_SHORT).setAction(actionTitle, onClickListener).show();
    }

    protected void showSnackAndAction(String msg, View.OnClickListener onClickListener) {
        showSnackAndAction(msg, getString(R.string.app_ok), onClickListener);
    }


    protected void kooSetToolbarTitle(String string) {
        if (setupToolbarTitleCenter()) {
            if (mToolbar_title_view != null) {
                mToolbar_title_view.setText(string);
                mToolbar.setTitle("");
                mToolbar_title_view.setVisibility(View.VISIBLE);
            }
            if (mCollapsingToolbarLayout!=null) {
                mCollapsingToolbarLayout.setTitle("");
            }
        } else {
            if (mToolbar_title_view != null) {
                mToolbar_title_view.setText("");
                mToolbar.setTitle(string);
                mToolbar_title_view.setVisibility(View.GONE);
            }
            if (mCollapsingToolbarLayout!=null) {
                mCollapsingToolbarLayout.setTitle(string);
            }
        }
    }
    @Deprecated
    protected String getToolbarTitle(){
        String backStr="";
        if (mToolbar!=null){
            backStr= mToolbar.getTitle().toString();
        }
        return  backStr;
    }


    /**
     * 获取根视图  xml跟节点
     */
    protected View getRootView() {
        //way two
        // View rootView = ((ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }

    /**
     * startAty
     */
    protected void startAty(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
