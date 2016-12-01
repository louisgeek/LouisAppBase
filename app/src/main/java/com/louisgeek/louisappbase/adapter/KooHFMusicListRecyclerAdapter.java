package com.louisgeek.louisappbase.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerViewHolder;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.musicplayer.MusicPlayerHelper;
import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class KooHFMusicListRecyclerAdapter extends HeaderFooterRecyclerAdapter<MusicBean>{

    public KooHFMusicListRecyclerAdapter(List<MusicBean> dataList, RecyclerView recyclerView, int mItemLayoutId) {
        super(dataList, recyclerView, mItemLayoutId);
    }

    @Override
    public void onBindViewHolderInner(RecyclerView.ViewHolder viewHolder, int realPosition, MusicBean data) {
        if(viewHolder instanceof HeaderFooterRecyclerViewHolder) {
            HeaderFooterRecyclerViewHolder headerFooterRecyclerViewHolder= (HeaderFooterRecyclerViewHolder) viewHolder;

            ImageView id_tv_image=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_image);
            TextView id_tv_item_title=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title);
            TextView id_tv_item_title_more=headerFooterRecyclerViewHolder.onBindView(R.id.id_tv_item_title_more);

            id_tv_item_title.setText(data.getName());
            id_tv_item_title_more.setText(String.valueOf(data.getArtist()));
            id_tv_image.setImageResource(R.mipmap.ic_launcher);

           // Bitmap bm= MusicPlayerHelper.createAlbumArtBitmap(data.getUrl());
          Bitmap bm= MusicPlayerHelper.getAlbumArtBitmap(id_tv_image.getContext(),data.getAlbumid());
            if (bm!=null){
                id_tv_image.setImageBitmap(bm);
            }

        }
    }

}
