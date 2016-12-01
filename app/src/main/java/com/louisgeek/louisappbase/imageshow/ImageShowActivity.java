package com.louisgeek.louisappbase.imageshow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooPagerAdapter;
import com.louisgeek.louisappbase.base.BaseAppCompatAty;
import com.louisgeek.louisappbase.data.TabHostShowData;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.imageshow.presenter.ImageShowPresenterImpl;
import com.louisgeek.louisappbase.imageshow.view.IImageShowView;
import com.louisgeek.louisappbase.util.RegexUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends BaseAppCompatAty implements IImageShowView{

    ViewPager mViewPager;
    TextView id_bottom_sheet_text;
    BottomSheetBehavior<View> mBottomSheetBehavior;
    AppBarLayout id_appbar_layout;
    //int  mToolbarHeight;
    ImageShowPresenterImpl mImageShowPresenter;

    KooPagerAdapter mKooPagerAdapter;
    List<ImageShowBean> mImageShowBeanList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.activity_image_show;
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
        return R.menu.menu_save;
    }

    @Override
    protected TabHostShowData setupTabHostData() {
        return null;
    }

    @Override
    protected void initView() {


        initBottomSheet();
        //
        initViewPager();

        //
        mImageShowPresenter=new ImageShowPresenterImpl(this);
        mImageShowPresenter.gainImageShowList();

    }


    private void initBottomSheet() {
        id_bottom_sheet_text = (TextView) findViewById(R.id.id_bottom_sheet_text);
     /*   if (contentlistBean.getText()!=null&&!contentlistBean.getText().equals("")) {
            id_bottom_sheet_text.setText(contentlistBean.getText());
        }else{
            id_bottom_sheet_text.setText(contentlistBean.getTitle());
        }*/

        View bottomSheet =  findViewById(R.id.id_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
               /** STATE_COLLAPSED: 关闭Bottom Sheets,显示peekHeight的高度，默认是STATE_COLLAPSED
                STATE_DRAGGING: 用户拖拽Bottom Sheets时的状态
                STATE_SETTLING: 当Bottom Sheets view释放时记录的状态。
                STATE_EXPANDED: 当Bottom Sheets 展开的状态
                STATE_HIDDEN: 当Bottom Sheets 隐藏的状态*/
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void initViewPager() {
        id_appbar_layout= (AppBarLayout) findViewById(R.id.id_appbar_layout);
        //
        mViewPager = findById(R.id.id_view_pager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                id_bottom_sheet_text.setText(mImageShowBeanList.get(position).getTitle());

                kooSetToolbarTitle((position+1)+"/"+mImageShowBeanList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mKooPagerAdapter = new KooPagerAdapter(mImageShowBeanList);
        mKooPagerAdapter.setPhotoViewAttacherListener(new KooPagerAdapter.PhotoViewAttacherListener() {

            @Override
            public void onViewTap(final View photoView, float x, float y) {
                //Toast.makeText(ImageShowActivity.this, "onViewTap", Toast.LENGTH_SHORT).show();
                        //
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED||mBottomSheetBehavior.getState() ==BottomSheetBehavior.STATE_COLLAPSED) {
                    //设置为隐藏
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    //
                    id_appbar_layout.setExpanded(false);


                }else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    //设置为展开
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    //
                    id_appbar_layout.setExpanded(true);

                }
                id_appbar_layout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                    @Override
                    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                        //  收起 0   ~  -84      展开 -84  ~0
                       // KLog.d("!!!onOffsetChanged:verticalOffset"+verticalOffset);
                        /**
                         * 修正当toobar慢慢收起后  mPhotoView相对位置不变化
                         */
                        photoView.setTranslationY(Math.abs(verticalOffset));
                    }
                });
            }
        });
        //
        mViewPager.setAdapter(mKooPagerAdapter);
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
    protected boolean onOptionsItemSelectedOutter(MenuItem menuItem,boolean superBackValue) {
        switch (menuItem.getItemId()) {
            case R.id.id_menu_item_save:
                saveImageOperater();
            return true;
        }
        /**
         * superBackValue super.onOptionsItemSelected();方法得返回值
         */
        return  superBackValue;
    }

    private void saveImageOperater() {
        int pos=mViewPager.getCurrentItem();
        String  imageUrl=mImageShowBeanList.get(pos)==null?"":mImageShowBeanList.get(pos).getImageUrl().toString();
            if (!imageUrl.equals("")&&RegexUtil.checkURL(imageUrl)){
              /*  OkHttpClientSingleton.getInstance().doGetAsyncFile(imageUrl, new SimpleFileOkHttpCallback() {
                    @Override
                    public void OnSuccess(String filePath, int statusCode) {
                       //Toast.makeText(ImageShowActivity.this, "filePath:"+filePath, Toast.LENGTH_SHORT).show();
                        KLog.d("filePath:"+filePath);
                    }
                });*/

                KLog.d("imageUrl:"+imageUrl);
                downloadImage(imageUrl);
            }else{
                Toast.makeText(mContext, "已经是本地图片："+imageUrl, Toast.LENGTH_SHORT).show();
            }
    }

    public void  downloadImage(String imageUrl){
        DownloadImageHelper.downLoadFile(imageUrl, new DownloadImageHelper.OnDownLoadImageListener() {
            @Override
            public void onSuccess(String filepath) {
                KLog.d("XXX onSuccess"+filepath);
                Toast.makeText(mContext, "已保存到"+filepath, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                KLog.d("XXX onError  保存失败");
            }
        });
    }


    @Override
    public void setImageShowList(List<ImageShowBean> imageShowBeanList,int nowSeletedPos) {

        kooSetToolbarTitle((nowSeletedPos+1)+"/"+imageShowBeanList.size());

        String title=imageShowBeanList.get(0).getTitle();
        id_bottom_sheet_text.setText(title);
        mKooPagerAdapter.refreshImageShowBeanList(imageShowBeanList);
        //
        mViewPager.setCurrentItem(nowSeletedPos);

    }

    @Override
    public void showDataError() {

    }
}
