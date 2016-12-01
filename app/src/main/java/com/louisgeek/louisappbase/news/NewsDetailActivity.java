package com.louisgeek.louisappbase.news;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;
import android.widget.ImageView;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.util.GlideUtil;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;
import com.socks.library.KLog;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class NewsDetailActivity extends BaseAppCompatAty {

    NewsListBean.PagebeanBean.ContentlistBean mContentlistBean;
    LocalNews_NewsListBean.PagebeanBean.ContentlistBean mContentlistBean_local;
    HtmlTextView mHtmlTextView;
    ImageView id_iv_big_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_news_detail;
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
        return 0;
    }

    @Override
    protected TabHostShowData setupTabHostData() {
        return null;
    }

    @Override
    protected void initView() {
        mContentlistBean = (NewsListBean.PagebeanBean.ContentlistBean) InfoHolderSingleton.getInstance().getMapObj("contentlistBean");
        mContentlistBean_local = (LocalNews_NewsListBean.PagebeanBean.ContentlistBean) InfoHolderSingleton.getInstance().getMapObj("contentlistBean_local");

        AppBarLayout id_appbar_layout = findById(R.id.id_appbar_layout);

        id_iv_big_image = findById(R.id.id_iv_big_image);
        mHtmlTextView = findById(R.id.id_html_tv_content);

        if (mContentlistBean != null) {
            kooSetToolbarTitle(mContentlistBean.getTitle());
            if (mContentlistBean.isHavePic()) {
                String firstImage = mContentlistBean.getImageurls().get(0).getUrl();
                GlideUtil.displayImage(id_iv_big_image, firstImage);
                KLog.d(mContentlistBean.getHtml());
                String html = mContentlistBean.getHtml();
                html = html.replace(firstImage,"");
                mHtmlTextView.setHtml(html, new HtmlHttpImageGetter(mHtmlTextView, null, true));
                KLog.d(html);
                id_appbar_layout.setExpanded(true);
            } else {
                id_appbar_layout.setExpanded(false,false);

                mHtmlTextView.setHtml(mContentlistBean.getHtml(), new HtmlHttpImageGetter(mHtmlTextView, null, true));
            }
        } else {
            kooSetToolbarTitle(mContentlistBean_local.getTitle());
            mHtmlTextView.setHtml(mContentlistBean_local.getDesc(), new HtmlHttpImageGetter(mHtmlTextView, null, true));
            //KLog.d(mContentlistBean.getHtml());
            if (mContentlistBean_local.getImageurls() != null && mContentlistBean_local.getImageurls().size() > 0) {
                GlideUtil.displayImage(id_iv_big_image, mContentlistBean_local.getImageurls().get(0).getUrl());
            }
        }

 /*       AppBarLayout appBarLayout=findById(R.id.id_appbar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayoutOnOffsetChangedListener4CollapsingToolbarLayoutStateChange() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, CollapsingToolbarLayoutState state) {
                if(state == CollapsingToolbarLayoutState.EXPANDED ) {
                    //展开状态
                    kooSetToolbarTitle(mContentlistBean.getTitle());

                }else if(state == CollapsingToolbarLayoutState.COLLAPSED){
                    //折叠状态
                    kooSetToolbarTitle(mContentlistBean.getTitle());

                }else {
                    //中间状态
                    kooSetToolbarTitle(mContentlistBean.getTitle());
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
        return true;
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
