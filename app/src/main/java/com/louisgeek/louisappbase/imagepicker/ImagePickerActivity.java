package com.louisgeek.louisappbase.imagepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.RecyclerViewWithEmpty;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooHFImagePickerRecyclerAdapter;
import com.louisgeek.louisappbase.adapter.KooHFImagesDirRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.imagepicker.bean.ImageTextBean;
import com.louisgeek.louisappbase.imagepicker.contract.LocalImageContract;
import com.louisgeek.louisappbase.imagepicker.presenter.LocalImagePresenterImpl;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.news.NewsDetailActivity;
import com.louisgeek.louisappbase.util.SizeTool;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImagePickerActivity extends BaseAppCompatAty implements LocalImageContract.View {
    RecyclerViewWithEmpty mRecyclerViewWithEmpty;
    KooHFImagePickerRecyclerAdapter mKooHFImagePickerRecyclerAdapter;

    KooHFImagesDirRecyclerAdapter mKooHFImagesDirRecyclerAdapter;


    ProgressDialog mProgressDialog;

    List<String> mNowChildList = new ArrayList<>();

    List<String> mAllChildList = new ArrayList<>();

    List<ImageTextBean> mImageTextBeanList = new ArrayList<>();
    RecyclerViewWithEmpty recyclerview_dir;
    private LocalImageContract.Presenter mPresenter;
    private int recyclerview_dir_height;
    private View view_background;
    private String ALL_IMAGES_DIR_PATH_KEY = "所有图片";
    private String mNowDirPathKey = ALL_IMAGES_DIR_PATH_KEY;//默认是所有图片

    Map<String, List<String>> mGoupMap = new HashMap<>();

    /**
     * 已选择的图片
     */
    private List<String> mSelectedPhotoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_image_picker);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_image_picker;
    }

    @Override
    protected int setupToolbarResId() {
        return R.id.id_toolbar;
    }

    @Override
    protected int setupCollapsingToolbarLayoutResId() {
        return 0;
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
        return R.menu.menu_show_image_dir;
    }

    @Override
    protected TabHostShowData setupTabHostData() {
        return null;
    }

    @Override
    protected void initView() {

        view_background = findById(R.id.id_view_background);
        view_background.setVisibility(View.GONE);
        view_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImageDir();
            }
        });


        iniRecyclerView_Dir();
        iniRecyclerView_File();

mPresenter=new LocalImagePresenterImpl(this);
        mPresenter.gainDirAndFileListViewData(this);
        kooSetToolbarTitle("");
    }

    private void iniRecyclerView_Dir() {
        recyclerview_dir = findById(R.id.id_recyclerview_dir);

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
         linearLayoutManager.setAutoMeasureEnabled(true);*/
        recyclerview_dir.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_dir.setHasFixedSize(true);
        recyclerview_dir.setItemAnimator(new DefaultItemAnimator());
        //初始化
        // recyclerview_dir.setNestedScrollingEnabled(false);

        mKooHFImagesDirRecyclerAdapter = new KooHFImagesDirRecyclerAdapter(mImageTextBeanList,
                recyclerview_dir, R.layout.item_comm);

        mKooHFImagesDirRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {

                if (mKooHFImagesDirRecyclerAdapter.getLastClickDirItemChildView() != null) {
                    mKooHFImagesDirRecyclerAdapter.getLastClickDirItemChildView().setVisibility(View.GONE);
                }
                View imv = itemView.findViewById(R.id.id_tv_item_image_more);
                imv.setVisibility(View.VISIBLE);
                mKooHFImagesDirRecyclerAdapter.setLastClickDirItemChildView(imv);
                /**
                 *
                 */
                ImageTextBean imageTextBean = (ImageTextBean) data;
                /**
                 *
                 */
                imageDirOperater();
                if (mNowDirPathKey.equals(imageTextBean.getDirPath())) {
                    return;
                }
                mNowDirPathKey = imageTextBean.getDirPath();
                refreshFileData();


            }
        });


        recyclerview_dir.setAdapter(mKooHFImagesDirRecyclerAdapter);

    }

    private void refreshFileData() {

        mNowChildList.clear();
        if (mNowDirPathKey.equals(ALL_IMAGES_DIR_PATH_KEY)) {
            //
            mNowChildList.addAll(mAllChildList);
        } else {
            List<String> stringList = mGoupMap.get(mNowDirPathKey);
            if (stringList != null && stringList.size() > 0) {
                for (int i = 0; i < stringList.size(); i++) {
                    mNowChildList.add(mNowDirPathKey + File.separator + stringList.get(i));
                }
            }
        }
        KLog.d("ASD 333");
        mKooHFImagePickerRecyclerAdapter.notifyDataSetChanged();

    }


    private void iniRecyclerView_File() {

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
        mRecyclerViewWithEmpty.setLayoutManager(new GridLayoutManager(this, 3));
        /**
         * 初始化时候防止测量   回调ALL 卡死和OOM
         */
        mRecyclerViewWithEmpty.getLayoutManager().setAutoMeasureEnabled(false);
        mRecyclerViewWithEmpty.setHasFixedSize(true);
        mRecyclerViewWithEmpty.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewWithEmpty.setEmptyView(id_recyclerview_empty_view);
        //

        mKooHFImagePickerRecyclerAdapter = new KooHFImagePickerRecyclerAdapter(mNowChildList, mSelectedPhotoList,
                mRecyclerViewWithEmpty, R.layout.item_small_image);

        //
        mKooHFImagePickerRecyclerAdapter.setOnItemLongClickListener(new HeaderFooterRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View itemView, int position, Object data) {
                /**
                 * 长按查看图片
                 */
                String pathNow = (String) data;
                int nowSelectedPos = 0;
                List<ImageShowBean> imageShowBeanList = new ArrayList<>();
                for (int i = 0; i < mNowChildList.size(); i++) {
                    ImageShowBean isb = new ImageShowBean();
                    String pathLL = mNowChildList.get(i);
                    isb.setImageUrl(pathLL);
                    isb.setTitle("图片" + (i + 1));
                    if (pathNow.equals(pathLL)) {
                        nowSelectedPos = i;
                    }
                    imageShowBeanList.add(isb);
                }
                ImageShowDataHelper.setDataAndToImageShow(mContext, imageShowBeanList, nowSelectedPos);

            }
        });
        mKooHFImagePickerRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {
                String path = (String) data;
                if (mSelectedPhotoList.contains(path)) {
                    mSelectedPhotoList.remove(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(false);
                } else {
                    mSelectedPhotoList.add(path);
                    itemView.findViewById(R.id.id_tv_checkbtn).setSelected(true);
                }
                kooSetToolbarTitle("已选" + mSelectedPhotoList.size() + "张");

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
        mRecyclerViewWithEmpty.setAdapter(mKooHFImagePickerRecyclerAdapter);
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
    protected boolean needEventBus() {
        return false;
    }

    @Override
    protected boolean needSwipeBack() {
        return false;
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
        switch (menuItem.getItemId()) {
            case R.id.id_menu_item_show_image_dir:
                imageDirOperater();
                return true;
            case R.id.id_menu_item_show_image_finish:
                imagePickFinishOperater();
                return true;
        }
        /**
         * superBackValue super.onOptionsItemSelected();方法得返回值
         */
        return superBackValue;
    }

    private void imagePickFinishOperater() {
        KLog.d("完成选择：" + mSelectedPhotoList.size() + "张");
    }

    /**
     * 是否正在显示
     */
    private boolean isAnimationIng = false;

    private void imageDirOperater() {
        if (isAnimationIng) {
            return;
        }
        if (view_background.getVisibility() == View.VISIBLE) {
            hideImageDir();
        } else {
            showImageDir();
        }
    }

    private void showImageDir() {
        view_background.setVisibility(View.VISIBLE);
      /*  WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=0.4f;
        getWindow().setAttributes(params);*/
        isAnimationIng = true;
        /**
         * 坐标0
         */
        recyclerview_dir.animate().translationY(0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationIng = false;

            }
        });

        /**
         * 改变蒙版
         */
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view_background, "alpha", 0f, 0.5f);
        //objectAnimator.setDuration(duration);
        objectAnimator.start();

       /*view_background.animate().alpha(0.5f);*/

    }

    private void hideImageDir() {
        isAnimationIng = true;
      /*  WindowManager.LayoutParams params=getWindow().getAttributes();
        params.alpha=1f;
        getWindow().setAttributes(params);*/

        recyclerview_dir.animate().translationY(-recyclerview_dir_height).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimationIng = false;
                view_background.setVisibility(View.GONE);
            }
        });

        /**
         * 改变蒙版
         */
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view_background, "alpha", 0.5f, 0f);
        //objectAnimator.setDuration(duration);
        objectAnimator.start();

       /*view_background.animate().alpha(0f);*/

    }

    @Override
    public void backDirAndFileData(Map<String, List<String>> groupMap) {
        KLog.d("groupMap size " + groupMap.size());
        mGoupMap = groupMap;
        KLog.d("ASD 111");
/**
 * 刷新文件夹数据*/


        mImageTextBeanList.clear();
        mAllChildList.clear();

        ImageTextBean showAllItem_bean = null;
        for (String key : groupMap.keySet()) {
            ImageTextBean imageTextBean = new ImageTextBean();
            imageTextBean.setDirPath(key);
            KLog.d("dir path" + key);
            String first = groupMap.get(key).get(0);
            KLog.d("first Image" + first);
            imageTextBean.setDirImageIconPath(key + File.separator + first);
            imageTextBean.setImageCount(groupMap.get(key).size());
/**
 * 所有图片
 */

            List<String> slll = groupMap.get(key);
            if (slll != null && slll.size() > 0) {
                for (int i = 0; i < slll.size(); i++) {
                    mAllChildList.add(key + File.separator + slll.get(i));
                }
            }
            mImageTextBeanList.add(imageTextBean);


            if (showAllItem_bean == null) {
                //所有图片的文件夹
                showAllItem_bean = new ImageTextBean();
                showAllItem_bean.setDirImageIconPath(key + File.separator + first);
            }
        }
        KLog.d("ASD 222");

/**
 * 添加到最前面
 */

        showAllItem_bean.setDirPath(ALL_IMAGES_DIR_PATH_KEY);
        showAllItem_bean.setImageCount(mAllChildList.size());
        mImageTextBeanList.add(0, showAllItem_bean);

        //
        mKooHFImagesDirRecyclerAdapter.notifyDataSetChanged();

/**
 * 数据初始化完后 测量高度   当然 recyclerview_dir 是 wrap-content
 */

        recyclerview_dir_height = SizeTool.getMeasuredHeightMy(recyclerview_dir);

/**
 * 偏移 到看不见的顶部
 */

        recyclerview_dir.setTranslationY(-recyclerview_dir_height);


/**
 * 刷新文件数据
 */

        refreshFileData();


    }


    @Override
    public void showProgressLoading() {
        //显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "loading");
    }

    @Override
    public void hideProgressLoading() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showError(String error) {
    }
}
