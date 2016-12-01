package com.louisgeek.louisappbase.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.RecyclerViewWithEmpty;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooHFImageListRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseAppCompatAtyAdapter;
import com.louisgeek.louisappbase.imagepicker.contract.NetImageContract;
import com.louisgeek.louisappbase.imagepicker.presenter.NetImagePresenterImpl;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.news.NewsDetailActivity;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends BaseAppCompatAtyAdapter implements NetImageContract.View{

    RecyclerViewWithEmpty mRecyclerViewWithEmpty;
    private  NetImageContract.Presenter mPresenter;
    KooHFImageListRecyclerAdapter mKooHFImageListRecyclerAdapter;
    List<String> mStringList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_image_list);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_image_list;
    }

    @Override
    protected int setupToolbarResId() {
        return R.id.id_toolbar;
    }

    @Override
    protected void initView() {

        iniRecyclerView();


        mPresenter=new NetImagePresenterImpl(this);
        mPresenter.gainNetData();

    }

    @Override
    protected boolean setupToolbarTitleCenter() {
        return  true;
    }


    private void iniRecyclerView() {

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
        mRecyclerViewWithEmpty.setLayoutManager(new GridLayoutManager(this, 2));
        /**
         * 初始化时候防止测量   回调ALL 卡死和OOM
         */
        mRecyclerViewWithEmpty.getLayoutManager().setAutoMeasureEnabled(false);
        mRecyclerViewWithEmpty.setHasFixedSize(true);
        mRecyclerViewWithEmpty.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewWithEmpty.setEmptyView(id_recyclerview_empty_view);
        //

        mKooHFImageListRecyclerAdapter = new KooHFImageListRecyclerAdapter(mStringList, null,
                mRecyclerViewWithEmpty, R.layout.item_small_image);

        //
        mKooHFImageListRecyclerAdapter.setOnItemLongClickListener(new HeaderFooterRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position, Object data) {
                /**
                 * 长按查看图片
                 */
             String pathNow = (String) data;
                int nowSelectedPos = 0;
                List<ImageShowBean> imageShowBeanList = new ArrayList<>();
                for (int i = 0; i < mStringList.size(); i++) {
                    ImageShowBean isb = new ImageShowBean();
                    if (pathNow.equals(mStringList.get(i))){
                        nowSelectedPos=i;
                    }
                    isb.setImageUrl(mStringList.get(i));
                    isb.setTitle("图片"+(i+1));

                    imageShowBeanList.add(isb);
                }


                ImageShowDataHelper.setDataAndToImageShow(mContext, imageShowBeanList, nowSelectedPos);

            }
        });
        mKooHFImageListRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {
              /*  String path = (String) data;
                if (mSelectedPhotoList.contains(path)) {
                    mSelectedPhotoList.remove(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(false);
                } else {
                    mSelectedPhotoList.add(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(true);
                }
                kooSetToolbarTitle("已选" + mSelectedPhotoList.size() + "张");*/

                Intent intent = new Intent(mContext, NewsDetailActivity.class);
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
                //startActivity(intent);
            }
        });
        //////mKooHFRecyclerAdapter.setFooterView(R.layout.recyclerview_footer);
        //
        mRecyclerViewWithEmpty.setAdapter(mKooHFImageListRecyclerAdapter);
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
    public void backNetData(List<String> imageUrls) {
        KLog.d("imageUrls SIZE:"+imageUrls.size());
        mKooHFImageListRecyclerAdapter.refreshDataList(imageUrls);
    }

    @Override
    public void showError(String error) {
        KLog.d("error:"+error);
    }
}
