package com.louisgeek.louisappbase.musicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.RecyclerViewWithEmpty;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooHFMusicListRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseAppCompatAtyAdapter;
import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.musicplayer.contract.MusicPlayerContract;
import com.louisgeek.louisappbase.musicplayer.presenter.MusicPlayerPresenterImpl;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

public class MusicPlayListActivity extends BaseAppCompatAtyAdapter implements MusicPlayerContract.View{
    RecyclerViewWithEmpty mRecyclerViewWithEmpty;
    List<MusicBean> mMusicBeanList=new ArrayList<>();
    KooHFMusicListRecyclerAdapter mKooHFMusicListRecyclerAdapter;
    MusicPlayerContract.Presenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /// setContentView(R.layout.activity_play_list);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_play_list;
    }

    @Override
    protected int setupToolbarResId() {
        return R.id.id_toolbar;
    }

    @Override
    protected void initView() {

        initRecyclerView();
        mPresenter=new MusicPlayerPresenterImpl(this);
        mPresenter.gainData();
    }



    private void initRecyclerView() {

        mRecyclerViewWithEmpty = findById(R.id.id_recyclerview);
        //初始化
        //mRecyclerViewWithEmpty.setNestedScrollingEnabled(false);

        View id_recyclerview_empty_view = findById(R.id.id_recyclerview_empty_view);
        id_recyclerview_empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///###refreshData();
            }
        });
        mRecyclerViewWithEmpty.setLayoutManager(new LinearLayoutManager(this));
        /**
         * 初始化时候防止测量   回调ALL 卡死和OOM
         */
        mRecyclerViewWithEmpty.getLayoutManager().setAutoMeasureEnabled(false);
        mRecyclerViewWithEmpty.setHasFixedSize(true);
        mRecyclerViewWithEmpty.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewWithEmpty.setEmptyView(id_recyclerview_empty_view);
        //

        mKooHFMusicListRecyclerAdapter = new KooHFMusicListRecyclerAdapter(mMusicBeanList,
                mRecyclerViewWithEmpty, R.layout.item_normal);

        //
        mKooHFMusicListRecyclerAdapter.setOnItemLongClickListener(new HeaderFooterRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position, Object data) {


            }
        });
        mKooHFMusicListRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {
              //###### MusicBean musicBean = (MusicBean) data;
               /*  if (mSelectedPhotoList.contains(path)) {
                    mSelectedPhotoList.remove(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(false);
                } else {
                    mSelectedPhotoList.add(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(true);
                }
                kooSetToolbarTitle("已选" + mSelectedPhotoList.size() + "张");*/

                Intent intent = new Intent(mContext, MusicPlayerActivity.class);

                if (InfoHolderSingleton.getInstance().getMapObj("musicNowPosition")==null){
                    //没有在播放的music
                    intent.putExtra("needRestartMusicService",true);
                }else{
                     int musicNowPosition= (int) InfoHolderSingleton.getInstance().getMapObj("musicNowPosition");
                     if (musicNowPosition==position){
                        //同一首歌
                         intent.putExtra("needRestartMusicService",false);
                     }else{
                        //不同一首歌
                         intent.putExtra("needRestartMusicService",true);
                     }
                }
                InfoHolderSingleton.getInstance().putMapObj("musicNowPosition", position);
                InfoHolderSingleton.getInstance().putMapObj("musicBeanList", mMusicBeanList);

                //intent.putExtra("newsbean",(NewsBean)data);
                /*    NewsListBean.PagebeanBean.ContentlistBean contentlistBean = (NewsListBean.PagebeanBean.ContentlistBean) data;
                    InfoHolderSingleton.getInstance().putMapObj("contentlistBean", contentlistBean);


                    View sharedElementView = itemView.findViewById(R.id.id_tv_item_image);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElementView,
                            getString(R.string.transition_name_imageview));
                    if (contentlistBean.getImageurls() != null && contentlistBean.getImageurls().size() > 0) {
                        sharedElementView.setEnabled(true);
                    } else {
                        sharedElementView.setEnabled(false);
                    }
                    //
                   ///#### startActivity(intent, optionsCompat.toBundle());*/
                startActivity(intent);
            }
        });
        //////mKooHFRecyclerAdapter.setFooterView(R.layout.recyclerview_footer);
        //
        mRecyclerViewWithEmpty.setAdapter(mKooHFMusicListRecyclerAdapter);
        mRecyclerViewWithEmpty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                   /* if (KooHFNewsRecyclerAdapter.isReachedBottomNormal(recyclerView)
                            && !mKooHFImagePickerRecyclerAdapter.isDataLoading()
                            && !mKooHFImagePickerRecyclerAdapter.isLoadComplete()) {
                        KLog.d("isReachedBottom");
                        //start
                        mKooHFImagePickerRecyclerAdapter.setDataLoading(true);
                        mKooHFImagePickerRecyclerAdapter.setFooterViewNormal();
                        //
                        KLog.d("isReachedBottom:getName:" + mChannelListBean.getName());
                        mNewsListPresenter.gainNewsListData(mChannelListBean, mKooHFImagePickerRecyclerAdapter.turnNextAndBackPageNum());
                        KLog.d("isReachedBottom:getPageNum:" + mKooHFImagePickerRecyclerAdapter.getPageNum());

                    }*/
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });


    }

    @Override
    public void backData(List<MusicBean> musicBeanList) {
        KLog.d("xxx size"+musicBeanList.size());
        mKooHFMusicListRecyclerAdapter.refreshDataList(musicBeanList);
    }
}
