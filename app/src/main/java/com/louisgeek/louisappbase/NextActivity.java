package com.louisgeek.louisappbase;

import android.os.Bundle;
import android.view.MenuItem;

import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;

public class NextActivity extends BaseAppCompatAty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_next;
    }

    @Override
    protected int setupToolbarResId() {
        return R.id.id_toolbar;
    }
    @Override
    protected int setupCollapsingToolbarLayoutResId() {
        return R.id.id_collapsing_toolbar_layout;
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
        return R.menu.base_menu_normal;
    }



    @Override
    protected TabHostShowData setupTabHostData() {
        return null;
    }

    @Override
    protected void initView() {

  /*
        //通过CollapsingToolbarLayout修改字体颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色*/

       /* AppBarLayout appBarLayout=findById(R.id.id_appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayoutOnOffsetChangedListener4CollapsingToolbarLayoutStateChange() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, CollapsingToolbarLayoutState state) {
                if(state == CollapsingToolbarLayoutState.EXPANDED ) {
                    //展开状态
                    setToolbarTitle("");

                }else if(state == CollapsingToolbarLayoutState.COLLAPSED){
                    //折叠状态
                    setToolbarTitle("用户名");

                }else {
                    //中间状态
                    setToolbarTitle("");
                }
            }
        });*/

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
        return false;
    }


    @Override
    protected void onNavigationViewItemSelected(MenuItem menuItem) {

    }

    @Override
    protected boolean onOptionsItemSelectedOutter(MenuItem menuItem, boolean superBackValue) {
        return superBackValue;
    }

}
