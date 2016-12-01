package com.louisgeek.louisappbase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.louisgeek.checkappupdatelib.tool.SimpleCheckUpdateTool;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.Pgyer;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.jokes.MainJokeFragment;
import com.louisgeek.louisappbase.jokes.MainJuheJokeFragment;
import com.louisgeek.louisappbase.news.MainNewsFragment;
import com.louisgeek.louisappbase.util.NetWorkTool;

public class ReaderActivity extends BaseAppCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_reader;
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
        return R.id.id_drawerLayout;
    }

    @Override
    protected int setupNavigationViewResId() {
        return R.id.id_navigation_view;
    }

    @Override
    protected int setupMenuResId() {
        return 0;
    }

    @Override
    protected TabHostShowData setupTabHostData() {
        TabHostShowData tabHostShowData = new TabHostShowData();
        tabHostShowData.addFragmentClass(MainNewsFragment.class, R.drawable.tab_icon_new, R.string.base_main_menu_title_1);
        tabHostShowData.addFragmentClass(MainJokeFragment.class, R.drawable.tab_icon_tweet, R.string.base_main_menu_title_2);
        tabHostShowData.addFragmentClass(MainJuheJokeFragment.class, R.drawable.tab_icon_explore, R.string.base_main_menu_title_3);
        //tabHostShowData.addFragmentClass(MainMovieFragment.class,R.drawable.tab_icon_explore,R.string.base_main_menu_title_3);
        //  tabHostShowData.addFragmentClass(MainExploreFragment.class,R.drawable.tab_icon_explore,R.string.base_main_menu_title_4);
        // tabHostShowData.addFragmentClass(MainMeFragment.class,R.drawable.tab_icon_me,R.string.base_main_menu_title_5);
        return tabHostShowData;
    }

    @Override
    protected void initView() {

        /**
         * 检测网络
         */
        NetWorkTool.checkInternetState(this, new NetWorkTool.OnInternetCallBack() {
            @Override
            public void onInternetBack(boolean isAccess) {
                if (!isAccess) {
                    Toast.makeText(mContext, "网络缓慢或未联网，请检查", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initCheckUpdate();

        /**
         *
         */
        kooSetToolbarTitle("新手上路");

    }

    private void initCheckUpdate() {
        SimpleCheckUpdateTool.updateNormal_HasNoMsg(ReaderActivity.this, Pgyer.APP_ID,Pgyer.API_KEY);
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
    protected void onDestroy() {
        super.onDestroy();
     /*  try{
           DownloadManagerCenter.unbindDownService();
       }catch (Exception e){
           e.printStackTrace();
       }*/
    }
}
