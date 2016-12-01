package com.louisgeek.louisappbase.testui;

import android.os.Bundle;
import android.view.MenuItem;

import com.louisgeek.louisappbase.explores.MainExploreFragment;
import com.louisgeek.louisappbase.jokes.MainJokeFragment;
import com.louisgeek.louisappbase.news.MainNewsFragment;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;

public class NormalActivity extends BaseAppCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_normal;
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
        return R.menu.base_menu_normal;
    }

    @Override
    protected TabHostShowData setupTabHostData(){
        TabHostShowData tabHostShowData=new TabHostShowData();
        tabHostShowData.addFragmentClass(MainNewsFragment.class,R.drawable.tab_icon_new,R.string.base_main_menu_title_1);
        tabHostShowData.addFragmentClass(MainJokeFragment.class,R.drawable.tab_icon_tweet,R.string.base_main_menu_title_2);
        tabHostShowData.addFragmentClass(MainExploreFragment.class,R.drawable.tab_icon_explore,R.string.base_main_menu_title_3);
        //tabHostShowData.addFragmentClass(MainMeFragment.class,R.drawable.tab_icon_me,R.string.base_main_menu_title_4);
        return  tabHostShowData;
    }

    @Override
    protected void initView() {

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
}
