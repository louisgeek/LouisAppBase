package com.louisgeek.louisappbase.jokes;


import android.content.Intent;
import android.os.Bundle;
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
import com.louisgeek.louisappbase.adapter.KooHFJokeRecyclerAdapter;
import com.louisgeek.louisappbase.adapter.KooHFNewsRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.imageshow.ImageShowActivity;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.jokes.bean.JokeImageAndTextListBean;
import com.louisgeek.louisappbase.jokes.presenter.JokePresenterImpl;
import com.louisgeek.louisappbase.jokes.view.IJokeView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JokeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JokeListFragment extends BaseFragment implements IJokeView{

    JokePresenterImpl mJokePresenter;

    List<JokeImageAndTextListBean.ContentlistBean> mContentlistBeanList=new ArrayList<>();

    RecyclerViewWithEmpty mRecyclerViewWithEmpty;

    KooHFJokeRecyclerAdapter mKooJokeHFRecyclerAdapter;

    int mJokeType;

    SwipeRefreshLayout mSwipeRefreshLayout;
    public JokeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JokeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JokeListFragment newInstance(String param1, String param2) {
        JokeListFragment fragment = new JokeListFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_joke_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_joke_list;
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


        mJokePresenter=new JokePresenterImpl(this);

        mJokeType=Integer.valueOf(mParam2);
        KLog.d("mJokeType:"+mJokeType);
        //
        iniRecyclerView();
        refreshData();
    }
    private void refreshData() {
        mSwipeRefreshLayout.setRefreshing(true);
        /**
         * 重置内部页数
         */
        mKooJokeHFRecyclerAdapter.resetPageNum();
        mJokePresenter.gainJokeData(String.valueOf(mJokeType),mKooJokeHFRecyclerAdapter.getPageNum());
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


        if (mJokeType==MainJokeFragment.TEXT){
            mKooJokeHFRecyclerAdapter = new KooHFJokeRecyclerAdapter(mJokeType,mContentlistBeanList,
                    mRecyclerViewWithEmpty, R.layout.item_long_text);
        }else{
        mKooJokeHFRecyclerAdapter = new KooHFJokeRecyclerAdapter(mJokeType,mContentlistBeanList,
                mRecyclerViewWithEmpty, R.layout.item_big_image);
        }

        //
        mKooJokeHFRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {
                Intent intent = new Intent(getActivity(), ImageShowActivity.class);
                //intent.putExtra("newsbean",(JokeImageAndTextListBean.ContentlistBean)data);
                JokeImageAndTextListBean.ContentlistBean contentlistBean= (JokeImageAndTextListBean.ContentlistBean) data;

                    ImageShowBean imageShowBean=new ImageShowBean();
                    imageShowBean.setTitle("标题"+contentlistBean.getTitle());
                    imageShowBean.setContent(contentlistBean.getText());
                    imageShowBean.setImageUrl(contentlistBean.getImg());

                ImageShowDataHelper.setDataAndToImageShow(getActivity(),imageShowBean);

               /*  View sharedElementView=itemView.findViewById(R.id.id_tv_item_image);

                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),sharedElementView,
                        getString(R.string.transition_name_imageview));

                //
                startActivity(intent,optionsCompat.toBundle());*/
              startActivity(intent);
            }
        });
        //////mKooHFRecyclerAdapter.setFooterView(R.layout.recyclerview_footer);
        //
       // mRecyclerViewWithEmpty.setEmptyView(id_recyclerview_empty_view);
        mRecyclerViewWithEmpty.setAdapter(mKooJokeHFRecyclerAdapter);
        mRecyclerViewWithEmpty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (KooHFNewsRecyclerAdapter.isReachedBottomNormal(recyclerView)
                        && !mKooJokeHFRecyclerAdapter.isDataLoading()
                        && !mKooJokeHFRecyclerAdapter.isLoadComplete()) {
                    KLog.d("isReachedBottom");
                    //start
                    mKooJokeHFRecyclerAdapter.setDataLoading(true);
                    mKooJokeHFRecyclerAdapter.setFooterViewNormal();
                    //
                  //  KLog.d("isReachedBottom:getName:"+mChannelListBean.getName());
                    mJokePresenter.gainJokeData(String.valueOf(mJokeType),mKooJokeHFRecyclerAdapter.turnNextAndBackPageNum());
                    KLog.d("isReachedBottom:getPageNum:"+mKooJokeHFRecyclerAdapter.getPageNum());
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
    }

    @Override
    protected KooFragmentPagerAdapter setupKooFragmentPagerAdapter() {
        return null;
    }

    @Override
    public void setJokeListData(ShowAPIBaseBean<JokeImageAndTextListBean> showAPIBaseBean) {
       // KLog.d(showAPIBaseBean.getShowapi_res_body().getContentlist().get(0).getTitle());
        //第一页的时候
        if (mKooJokeHFRecyclerAdapter.getPageNum()== KooHFJokeRecyclerAdapter.PAGE_NUM_DEFAULT){
            KLog.d("第一页:");
            mSwipeRefreshLayout.setRefreshing(false);
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getContentlist() != null){
                mKooJokeHFRecyclerAdapter.refreshDataList(showAPIBaseBean.getShowapi_res_body().getContentlist());
            }else{
                Toast.makeText(KooApplication.getAppContext(), "刷新数据出错，请重试", Toast.LENGTH_SHORT).show();
            }

        }else{
            KLog.d("第"+mKooJokeHFRecyclerAdapter.getPageNum()+"页:");
            //
            if (showAPIBaseBean.getShowapi_res_body() != null &&
                    showAPIBaseBean.getShowapi_res_body().getContentlist() != null) {

                mKooJokeHFRecyclerAdapter.setDataLoading(false);

                mKooJokeHFRecyclerAdapter.appendDataList(showAPIBaseBean.getShowapi_res_body().getContentlist());
                //每页数据加载完后
                mKooJokeHFRecyclerAdapter.setFooterView(null);

            }else {
                //数据加载完后
                mKooJokeHFRecyclerAdapter.setFooterViewComplete();
                mKooJokeHFRecyclerAdapter.setLoadComplete(true);
            }
        }
    }

}
