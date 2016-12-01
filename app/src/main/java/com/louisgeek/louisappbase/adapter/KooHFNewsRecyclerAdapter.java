package com.louisgeek.louisappbase.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.util.GlideUtil;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class KooHFNewsRecyclerAdapter extends HeaderFooterRecyclerAdapter<NewsListBean.PagebeanBean.ContentlistBean>{
    public static final int  PAGE_NUM_DEFAULT=1;
    private  int  mPageNum=PAGE_NUM_DEFAULT;
    public  static int  PageSize=20;
    private  boolean  mDataLoading;
    private  boolean  mLoadComplete;

    public KooHFNewsRecyclerAdapter(List<NewsListBean.PagebeanBean.ContentlistBean> dataList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
    }


    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, NewsListBean.PagebeanBean.ContentlistBean data) {
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;
            TextView id_tv=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title);
            TextView id_tv_more=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title_more);
            id_tv.setText(data.getTitle());
            id_tv_more.setText(data.getDesc());

            ImageView id_iv=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            if (data.getImageurls()!=null&&data.getImageurls().size()>0){
                String imageUrl=data.getImageurls().get(0).getUrl();
                GlideUtil.displayImage(id_iv, imageUrl);
            }else{
                //防止错乱
                GlideUtil.displayImageRes( id_iv, R.mipmap.ic_image_load_failed);
            }

        }
    }

    public int turnNextAndBackPageNum(){
       mPageNum=mPageNum+1;
       return  mPageNum;
    }

    public int getPageNum() {
        return mPageNum;
    }
    public void resetPageNum() {
         mPageNum=PAGE_NUM_DEFAULT;
    }

    public void setDataLoading(boolean dataLoading) {
        mDataLoading = dataLoading;
    }

    public boolean isDataLoading() {
        return mDataLoading;
    }

    public void setLoadComplete(boolean loadComplete) {
        mLoadComplete = loadComplete;
    }

    public boolean isLoadComplete() {
        return mLoadComplete;
    }

    /**
     * 加载更多 OnScrollListener
     */
    @Deprecated
    private int mLastVisibleItemPosition;
    @Deprecated
    private RecyclerView.OnScrollListener mOnLoadMoreScrollListener= new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //logD("mLastVisibleItemPosition:"+mLastVisibleItemPosition);
            //logD("getItemCount():"+getItemCount());
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && mLastVisibleItemPosition+1 == getItemCount()
                    &&getFooterView()!=null&&getFooterView().getVisibility()== View.VISIBLE) {
                //加载更多
                KLog.d("加载更多");
                  /*  pageIndex+= Urls.PAZE_SIZE;
                    mNewsPresenter.loadDataLogic(mType, pageIndex);*/
                mPageNum++;
                //
               // appendDataList(gainListPageData(mPageNum,20));
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
            mLastVisibleItemPosition=linearLayoutManager.findLastVisibleItemPosition();
        }
    };

    @Deprecated
    public RecyclerView.OnScrollListener getOnLoadMoreScrollListener() {
        return mOnLoadMoreScrollListener;
    }


    public void setFooterViewNormal() {
        //加载时候设置
        setFooterView(R.layout.recyclerview_footer);
        View footView=getFooterView();
        View id_douban_loading_view= footView.findViewById(R.id.id_douban_loading_view);
        View id_tv_load_finish= footView.findViewById(R.id.id_tv_load_finish);
        id_douban_loading_view.setVisibility(View.VISIBLE);
        id_tv_load_finish.setVisibility(View.GONE);
    }
    public void setFooterViewComplete() {
        //加载时候设置
        setFooterView(R.layout.recyclerview_footer);
        View footView=getFooterView();
        View id_douban_loading_view= footView.findViewById(R.id.id_douban_loading_view);
        View id_tv_load_finish= footView.findViewById(R.id.id_tv_load_finish);
        id_douban_loading_view.setVisibility(View.GONE);
        id_tv_load_finish.setVisibility(View.VISIBLE);
    }
}
