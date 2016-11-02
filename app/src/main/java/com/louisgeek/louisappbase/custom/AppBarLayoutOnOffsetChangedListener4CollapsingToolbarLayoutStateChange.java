package com.louisgeek.louisappbase.custom;

import android.support.design.widget.AppBarLayout;

/**
 * Created by louisgeek on 2016/11/2.
 */

public abstract class AppBarLayoutOnOffsetChangedListener4CollapsingToolbarLayoutStateChange implements AppBarLayout.OnOffsetChangedListener {

    public enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private CollapsingToolbarLayoutState mCurrentState = CollapsingToolbarLayoutState.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            if (mCurrentState != CollapsingToolbarLayoutState.EXPANDED) {
                onStateChanged(appBarLayout, CollapsingToolbarLayoutState.EXPANDED);
            }
            mCurrentState = CollapsingToolbarLayoutState.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != CollapsingToolbarLayoutState.COLLAPSED) {
                onStateChanged(appBarLayout, CollapsingToolbarLayoutState.COLLAPSED);
            }
            mCurrentState = CollapsingToolbarLayoutState.COLLAPSED;
        } else {
            if (mCurrentState != CollapsingToolbarLayoutState.IDLE) {
                onStateChanged(appBarLayout, CollapsingToolbarLayoutState.IDLE);
            }
            mCurrentState = CollapsingToolbarLayoutState.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, CollapsingToolbarLayoutState state);
}