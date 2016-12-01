package com.louisgeek.louisappbase;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.explores.MainExploreFragment;
import com.louisgeek.louisappbase.jokes.MainJokeFragment;
import com.louisgeek.louisappbase.news.MainNewsFragment;

public class MainActivity extends BaseAppCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_main;
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


 /*   @Override
    protected int setupTabLayoutResId() {
        return R.id.id_tab_layout;
    }

    @Override
    protected int setupViewPagerResId() {
        return 0;
    }*/

    @Override
    protected void initView() {
     /*   findById(R.id.id_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAty(NextActivity.class);
            }
        });*/

        kooSetToolbarTitle("qqqqqqq");

        String[] xxx={"111","222","33","444"};
       // setTabTitles(xxx);

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
        Toast.makeText(mContext, "xxx:"+menuItem.getItemId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected boolean onOptionsItemSelectedOutter(MenuItem menuItem, boolean superBackValue) {
        return superBackValue;
    }
}
