package com.louisgeek.louisappbase.util;

import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class ViewUtil {

    public static boolean isReachedTop(View view) {
        return !ViewCompat.canScrollVertically(view, -1);//顶部是否可以滚动
    }
    public static boolean isReachedBottom(View view) {
        return !ViewCompat.canScrollVertically(view, 1);//底部是否可以滚动
    }
    public static boolean isReachedLeft(View view) {
        return !ViewCompat.canScrollHorizontally(view, -1);//左侧是否可以滚动
    }
    public static boolean isReachedRight(View view) {
        return !ViewCompat.canScrollHorizontally(view, 1);//右侧是否可以滚动
    }
}
