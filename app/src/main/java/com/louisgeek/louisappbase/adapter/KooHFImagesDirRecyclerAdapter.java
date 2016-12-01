package com.louisgeek.louisappbase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.imagepicker.bean.ImageTextBean;
import com.louisgeek.louisappbase.util.GlideUtil;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class KooHFImagesDirRecyclerAdapter extends HeaderFooterRecyclerAdapter<ImageTextBean>{

    private  View mLastClickDirItemChildView=null;
    public KooHFImagesDirRecyclerAdapter(List<ImageTextBean> dataList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
    }

    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, ImageTextBean data) {
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;
            TextView id_tv_item_title=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title);
            ImageView id_tv_item_image=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            ImageView id_tv_item_image_more=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image_more);

            if (realPosition==0) {
                id_tv_item_image_more.setVisibility(View.VISIBLE);
                mLastClickDirItemChildView=id_tv_item_image_more;
            }else {
                id_tv_item_image_more.setVisibility(View.GONE);
            }

           id_tv_item_title.setText(data.getDirName().replace("/","")+"("+data.getImageCount()+"张)");

            KLog.d("getDirImageIconPath"+data.getDirImageIconPath());
            GlideUtil.displayImage(id_tv_item_image,data.getDirImageIconPath());

        }
    }

    public View getLastClickDirItemChildView() {
        return mLastClickDirItemChildView;
    }

    public void setLastClickDirItemChildView(View lastClickDirItemChildView) {
        mLastClickDirItemChildView = lastClickDirItemChildView;
    }
}
