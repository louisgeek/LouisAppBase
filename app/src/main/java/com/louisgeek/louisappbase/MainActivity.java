package com.louisgeek.louisappbase;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.louisgeek.louisappbase.base.BaseAppCompatAty;

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

    @Override
    protected void initView() {
        findById(R.id.id_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAty(NextActivity.class);
            }
        });

        setToolbarTitle("qqqqqqq");
    }

    @Override
    protected boolean setupUseKLog() {
        return false;
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
}
