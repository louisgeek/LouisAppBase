package com.louisgeek.louisappbase.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.imagepicker.KooImagesLoader;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class KooHFImageListRecyclerAdapter extends HeaderFooterRecyclerAdapter<String>{

    private  List<String> mSelectedPhotoList;
    public KooHFImageListRecyclerAdapter(List<String> dataList, List<String> selectedPhotoList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
        mSelectedPhotoList=selectedPhotoList;
    }

    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, String data) {
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;

            ImageView id_tv_image=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            ImageView id_tv_checkbtn=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_checkbtn);
            id_tv_checkbtn.setVisibility(View.GONE);
            //GlideUtil.displayImage(id_tv_image.getContext(),id_tv_image,data);
           // idcb.setText("v"+realPosition);
            id_tv_image.setImageResource(R.mipmap.ic_image_no);
            KooImagesLoader.getInstance().loadImage(data,id_tv_image);
            // ImageLoader02.getInstance().loadImage(data,id_tv_image,true);
          /*  if (mSelectedPhotoList.contains(data)){
                id_tv_checkbtn.setSelected(true);
            }else{
                id_tv_checkbtn.setSelected(false);
            }*/
        }
    }

}
