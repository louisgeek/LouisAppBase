package com.louisgeek.louisappbase.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.imagepicker.KooImagesLoader;
import com.louisgeek.louisappbase.util.ScreenUtil;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class KooHFImagePickerRecyclerAdapter extends HeaderFooterRecyclerAdapter<String>{

    private  List<String> mSelectedPhotoList;
    RecyclerView mRecyclerView;
    private  int COUNT=0;
    public KooHFImagePickerRecyclerAdapter(List<String> dataList,List<String> selectedPhotoList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
        mSelectedPhotoList=selectedPhotoList;
        mRecyclerView=recyclerView;
    }

    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, String data) {
        COUNT++;
        KLog.d("COUNT:"+COUNT);
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;

            ImageView id_tv_image=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            ImageView id_tv_checkbtn=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_checkbtn);

           // idcb.setText("v"+realPosition);
            GridLayoutManager gridLayoutManager= (GridLayoutManager) mRecyclerView.getLayoutManager();
            int imageWidth=ScreenUtil.getScreenWidth(id_tv_image.getContext())/gridLayoutManager.getSpanCount();
            KLog.d("imageWidth:"+imageWidth);
            id_tv_image.setMaxWidth(imageWidth);
            id_tv_image.setMaxHeight(imageWidth);
            /**
             *
             */
              id_tv_image.setImageResource(R.drawable.shape_default_trans_image);
             KooImagesLoader.getInstance().loadImage(data,id_tv_image);//19------59~76
            //  ImageLoader03.getInstance().loadImage(data,id_tv_image,false);//19----75~78
         //   ImageLoader.getInstance().loadImage(id_tv_image,data,0);//19----40~44
            //  GlideUtil.displayImage(id_tv_image,data);//19---60


            if (mSelectedPhotoList.contains(data)){
                id_tv_checkbtn.setSelected(true);
            }else{
                id_tv_checkbtn.setSelected(false);
            }
        }
    }

}
