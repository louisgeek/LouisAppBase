package com.louisgeek.louisappbase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.util.GlideUtil;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class KooHFLocalNewsRecyclerAdapter extends HeaderFooterRecyclerAdapter<LocalNews_NewsListBean.PagebeanBean.ContentlistBean>{
    public static final int  PAGE_NUM_DEFAULT=1;
    private  int  mPageNum=PAGE_NUM_DEFAULT;
    public  static int  PageSize=20;
    private  boolean  mDataLoading;
    private  boolean  mLoadComplete;

    public KooHFLocalNewsRecyclerAdapter(List<LocalNews_NewsListBean.PagebeanBean.ContentlistBean> dataList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
    }


    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, LocalNews_NewsListBean.PagebeanBean.ContentlistBean data) {
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;
            TextView id_tv=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title);
            TextView id_tv_more=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title_more);
            id_tv.setText(data.getTitle());
            id_tv_more.setText(data.getDesc());

            ImageView id_iv=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            if (data.getImageurls()!=null&&data.getImageurls().size()>0){
                String imageUrl=data.getImageurls().get(0).getUrl();
                GlideUtil.displayImage( id_iv, imageUrl);
            }else{
                //防止错乱
                GlideUtil.displayImageRes(id_iv, R.mipmap.ic_image_load_failed);
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
