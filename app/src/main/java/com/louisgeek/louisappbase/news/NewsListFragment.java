package com.louisgeek.louisappbase.news;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.louisgeek.headerandfooterrecycleviewlib.HeaderFooterRecyclerAdapter;
import com.louisgeek.headerandfooterrecycleviewlib.RecyclerViewWithEmpty;
import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooFragmentPagerAdapter;
import com.louisgeek.louisappbase.adapter.KooHFLocalNewsRecyclerAdapter;
import com.louisgeek.louisappbase.adapter.KooHFNewsRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;
import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.LocalNews_NewsListBean;
import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.news.bean.NewsListBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.presenter.LocalNewsListPresenterImpl;
import com.louisgeek.louisappbase.news.presenter.NewsListPresenterImpl;
import com.louisgeek.louisappbase.news.view.INewsListView;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsListFragment extends BaseFragment implements INewsListView {

    RecyclerViewWithEmpty mRecyclerViewWithEmpty;
    KooHFNewsRecyclerAdapter mKooHFRecyclerAdapter;
    KooHFLocalNewsRecyclerAdapter mKooHFLocalRecyclerAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    NewsListPresenterImpl mNewsListPresenter;
    LocalNewsListPresenterImpl mLocalNewsListPresenter;
    NewsChannelBean.ChannelListBean mChannelListBean;
    LocalNews_AreaBean.CityListBean mCityListBean;

    private List<NewsListBean.PagebeanBean.ContentlistBean> mContentlistBeanList = new ArrayList<>();
    private List<LocalNews_NewsListBean.PagebeanBean.ContentlistBean> mLocalNews_NewsListBeanList = new ArrayList<>();

    public NewsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsListFragment newInstance(String param1, String param2) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //
            KLog.d("kkk:mParam2" + mParam2);
            String key = "ChannelListBean_key_" + mParam2;
            KLog.d("key:" + key);
            mChannelListBean = (NewsChannelBean.ChannelListBean) InfoHolderSingleton.getInstance().getMapObj(key);
            KLog.d("mChannelListBean" + mChannelListBean);
            // InfoHolderSingleton.getInstance().showAllMapObj();
            if (mChannelListBean == null) {
                String keyci = "cityListBeanList_key_" + mParam2;
                mCityListBean = (LocalNews_AreaBean.CityListBean) InfoHolderSingleton.getInstance().getMapObj(keyci);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        KLog.d("onCreateView");
        // Inflate the layout for this fragment
        // View view=inflater.inflate(R.layout.fragment_news_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_news_list;
    }

    @Override
    protected int setupViewPagerResId() {
        return 0;
    }

    @Override
    protected int setupTabLayoutResId() {
        return 0;
    }

    @Override
    protected void initView(View rootView) {

        mSwipeRefreshLayout = findById(R.id.id_swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();
            }
        });


        iniRecyclerView();
        //
        mNewsListPresenter = new NewsListPresenterImpl(this);

        //
        mLocalNewsListPresenter = new LocalNewsListPresenterImpl(this);

        refreshData();
    }

    private void refreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        /**
         * 重置页数
         */
        mKooHFRecyclerAdapter.resetPageNum();
        if (mChannelListBean != null) {
            mNewsListPresenter.gainNewsListData(mChannelListBean, KooHFNewsRecyclerAdapter.PAGE_NUM_DEFAULT);
        } else {
            mLocalNewsListPresenter.gainLocalNewsListData(mCityListBean, KooHFLocalNewsRecyclerAdapter.PAGE_NUM_DEFAULT);
        }
    }


    private void iniRecyclerView() {

        mRecyclerViewWithEmpty = findById(R.id.id_recyclerview);
        View id_recyclerview_empty_view = findById(R.id.id_recyclerview_empty_view);
        id_recyclerview_empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshData();
            }
        });
        mRecyclerViewWithEmpty.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewWithEmpty.setHasFixedSize(true);
        mRecyclerViewWithEmpty.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewWithEmpty.setEmptyView(id_recyclerview_empty_view);
        //
        if (mChannelListBean != null) {
            mKooHFRecyclerAdapter = new KooHFNewsRecyclerAdapter(mContentlistBeanList,
                    mRecyclerViewWithEmpty, R.layout.item_normal);

            //
            mKooHFRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position, Object data) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    //intent.putExtra("newsbean",(NewsBean)data);
                    NewsListBean.PagebeanBean.ContentlistBean contentlistBean = (NewsListBean.PagebeanBean.ContentlistBean) data;
                    InfoHolderSingleton.getInstance().putMapObj("contentlistBean", contentlistBean);


                    View sharedElementView = itemView.findViewById(R.id.id_tv_item_image);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), sharedElementView,
                            getString(R.string.transition_name_imageview));
                    if (contentlistBean.getImageurls() != null && contentlistBean.getImageurls().size() > 0) {
                        sharedElementView.setEnabled(true);
                    } else {
                        sharedElementView.setEnabled(false);
                    }
                    //
                    startActivity(intent, optionsCompat.toBundle());
                    //startActivity(intent);
                }
            });
            //////mKooHFRecyclerAdapter.setFooterView(R.layout.recyclerview_footer);
            //
            mRecyclerViewWithEmpty.setAdapter(mKooHFRecyclerAdapter);
            mRecyclerViewWithEmpty.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (KooHFNewsRecyclerAdapter.isReachedBottomNormal(recyclerView)
                            && !mKooHFRecyclerAdapter.isDataLoading()
                            && !mKooHFRecyclerAdapter.isLoadComplete()) {
                        KLog.d("isReachedBottom");
                        //start
                        mKooHFRecyclerAdapter.setDataLoading(true);
                        mKooHFRecyclerAdapter.setFooterViewNormal();
                        //
                        KLog.d("isReachedBottom:getName:" + mChannelListBean.getName());
                        mNewsListPresenter.gainNewsListData(mChannelListBean, mKooHFRecyclerAdapter.turnNextAndBackPageNum());
                        KLog.d("isReachedBottom:getPageNum:" + mKooHFRecyclerAdapter.getPageNum());
                 /*   GainData.gainListPageData(mParam1, mKooHFRecyclerAdapter.turnNextAndBackPageNum(), KooHFNewsRecyclerAdapter.PageSize, new GainData.PageDataCallBack() {
                        @Override
                        public void backData(List<String> strings) {
                            mKooHFRecyclerAdapter.setDataLoading(false);

                            mKooHFRecyclerAdapter.appendDataList(mContentlistBeanList);

                            if (strings == null || strings.size() <= 0) {
                                //数据加载完后
                                mKooHFRecyclerAdapter.setFooterViewComplete();
                                mKooHFRecyclerAdapter.setLoadComplete(true);
                            } else {
                                //每页数据加载完后
                                mKooHFRecyclerAdapter.setFooterView(null);
                            }
                        }

                        @Override
                        public void startLoadData() {
                            mKooHFRecyclerAdapter.setDataLoading(true);
                            mKooHFRecyclerAdapter.setFooterViewNormal();
                        }
                    });*/
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });

        } else {
            mKooHFLocalRecyclerAdapter = new KooHFLocalNewsRecyclerAdapter(mLocalNews_NewsListBeanList,
                    mRecyclerViewWithEmpty, R.layout.item_normal);


            //
            mKooHFLocalRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int position, Object data) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    //intent.putExtra("newsbean",(NewsBean)data);
                    LocalNews_NewsListBean.PagebeanBean.ContentlistBean contentlistBean_local = (LocalNews_NewsListBean.PagebeanBean.ContentlistBean) data;
                    InfoHolderSingleton.getInstance().putMapObj("contentlistBean_local", contentlistBean_local);


                    View sharedElementView = itemView.findViewById(R.id.id_tv_item_image);

                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), sharedElementView,
                            getString(R.string.transition_name_imageview));
                    if (contentlistBean_local.getImageurls() != null && contentlistBean_local.getImageurls().size() > 0) {
                        sharedElementView.setEnabled(true);
                    } else {
                        sharedElementView.setEnabled(false);
                    }
                    //
                    startActivity(intent, optionsCompat.toBundle());
                    //startActivity(intent);
                }
            });
            //////mKooHFRecyclerAdapter.setFooterView(R.layout.recyclerview_footer);
            //
            mRecyclerViewWithEmpty.setAdapter(mKooHFLocalRecyclerAdapter);
            mRecyclerViewWithEmpty.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (KooHFNewsRecyclerAdapter.isReachedBottomNormal(recyclerView)
                            && !mKooHFLocalRecyclerAdapter.isDataLoading()
                            && !mKooHFLocalRecyclerAdapter.isLoadComplete()) {
                        KLog.d("isReachedBottom");
                        //start
                        mKooHFLocalRecyclerAdapter.setDataLoading(true);
                        mKooHFLocalRecyclerAdapter.setFooterViewNormal();
                        //
                        KLog.d("isReachedBottom:getName:" + mCityListBean.getAreaName());
                        mLocalNewsListPresenter.gainLocalNewsListData(mCityListBean, mKooHFLocalRecyclerAdapter.turnNextAndBackPageNum());
                        KLog.d("isReachedBottom:getPageNum:" + mKooHFLocalRecyclerAdapter.getPageNum());

                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                }
            });

        }


    }


    @Override
    protected KooFragmentPagerAdapter setupKooFragmentPagerAdapter() {
        return null;
    }

    @Override
    public void setNewListData(ShowAPIBaseBean<NewsListBean> showAPIBaseBean) {
        //第一页的时候
        if (mKooHFRecyclerAdapter.getPageNum() == KooHFNewsRecyclerAdapter.PAGE_NUM_DEFAULT) {
            KLog.d("第一页:");
            mSwipeRefreshLayout.setRefreshing(false);
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist() != null) {
                mKooHFRecyclerAdapter.refreshDataList(showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist());
            }else {
                Toast.makeText(KooApplication.getAppContext(), "刷新数据出错，请重试", Toast.LENGTH_SHORT).show();
            }
        } else {
            KLog.d("第" + mKooHFRecyclerAdapter.getPageNum() + "页:");
            //
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist() != null) {

                mKooHFRecyclerAdapter.setDataLoading(false);

                mKooHFRecyclerAdapter.appendDataList(showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist());
                //每页数据加载完后
                mKooHFRecyclerAdapter.setFooterView(null);

            } else {
                //数据加载完后
                mKooHFRecyclerAdapter.setFooterViewComplete();
                mKooHFRecyclerAdapter.setLoadComplete(true);
            }
        }
    }

    @Override
    public void setLocalNewListData(ShowAPIBaseBean<LocalNews_NewsListBean> showAPIBaseBean) {
        //第一页的时候
        if (mKooHFLocalRecyclerAdapter.getPageNum() == KooHFLocalNewsRecyclerAdapter.PAGE_NUM_DEFAULT) {
            KLog.d("第一页:");
            mSwipeRefreshLayout.setRefreshing(false);
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist() != null) {
                mKooHFLocalRecyclerAdapter.refreshDataList(showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist());
            }else{
                Toast.makeText(KooApplication.getAppContext(), "刷新数据出错，请重试", Toast.LENGTH_SHORT).show();
            }

        } else {
            KLog.d("第" + mKooHFLocalRecyclerAdapter.getPageNum() + "页:");
            //
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean() != null &&
                    showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist() != null) {

                mKooHFLocalRecyclerAdapter.setDataLoading(false);

                mKooHFLocalRecyclerAdapter.appendDataList(showAPIBaseBean.getShowapi_res_body().getPagebean().getContentlist());
                //每页数据加载完后
                mKooHFLocalRecyclerAdapter.setFooterView(null);

            } else {
                //数据加载完后
                mKooHFLocalRecyclerAdapter.setFooterViewComplete();
                mKooHFLocalRecyclerAdapter.setLoadComplete(true);
            }
        }
    }
}
