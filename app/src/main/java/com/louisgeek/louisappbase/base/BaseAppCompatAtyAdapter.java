package com.louisgeek.louisappbase.base;

import android.view.MenuItem;

import com.louisgeek.louisappbase.data.TabHostShowData;

/**
 * Created by louisgeek on 2016/11/18.
 */

public abstract class BaseAppCompatAtyAdapter extends BaseAppCompatAty{
    @Override
    protected abstract int setupLayoutId();
    @Override
    protected abstract int setupToolbarResId();

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
    protected abstract void initView();

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
        return false;
    }
}
