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
import com.louisgeek.louisappbase.adapter.KooHFJuheJokeRecyclerAdapter;
import com.louisgeek.louisappbase.adapter.KooHFNewsRecyclerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;
import com.louisgeek.louisappbase.base.JuHeBaseBean;
import com.louisgeek.louisappbase.imageshow.ImageShowActivity;
import com.louisgeek.louisappbase.imageshow.ImageShowDataHelper;
import com.louisgeek.louisappbase.imageshow.bean.ImageShowBean;
import com.louisgeek.louisappbase.jokes.bean.Juhe_JokeImageAndTextListBean;
import com.louisgeek.louisappbase.jokes.model.JuheJokeModelImpl;
import com.louisgeek.louisappbase.jokes.presenter.JuheJokePresenterImpl;
import com.louisgeek.louisappbase.jokes.view.IJuheJokeView;
import com.louisgeek.louisappbase.util.SharedPreferencesTool;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link JuheJokeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JuheJokeListFragment extends BaseFragment implements IJuheJokeView {

    SwipeRefreshLayout mSwipeRefreshLayout;

    RecyclerViewWithEmpty mRecyclerViewWithEmpty;

    KooHFJuheJokeRecyclerAdapter mKooHFJuheJokeRecyclerAdapter;

    JuheJokePresenterImpl mJuheJokePresenter;

    List<Juhe_JokeImageAndTextListBean.DataBean> mDataBeanList=new ArrayList<>();
    public JuheJokeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JuheJokeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JuheJokeListFragment newInstance(String param1, String param2) {
        JuheJokeListFragment fragment = new JuheJokeListFragment();
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
       // return inflater.inflate(R.layout.fragment_juhe_joke_list, container, false);
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_juhe_joke_list;
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


        mJuheJokePresenter=new JuheJokePresenterImpl(this);

        //
        iniRecyclerView();
        /**
         *
         */
        refreshData();

    }
    private void refreshData() {
        int sort;
        String saveLastRefreshDataTime_key="saveLastRefreshDataTime_key_"+mParam2;
        String saveLastRefreshDataTime= (String) SharedPreferencesTool.get(getActivity(),saveLastRefreshDataTime_key,"");
        if (saveLastRefreshDataTime.equals("")){
            //没有刷新时间
            //第一次下拉
            saveLastRefreshDataTime=String.valueOf(System.currentTimeMillis()/1000);
            sort=JuheJokeModelImpl.SORT_DESC;//刷新出当前时间的之前的数据
        }else{
            sort=JuheJokeModelImpl.SORT_ASC;//刷新出上一次刷新时间的之后的数据
        }

        mSwipeRefreshLayout.setRefreshing(true);
        /**
         * 重置内部页数
         */
        mKooHFJuheJokeRecyclerAdapter.resetPageNum();
        mJuheJokePresenter.gainJuheJokeData("",saveLastRefreshDataTime,sort,mKooHFJuheJokeRecyclerAdapter.getPageNum());

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


      /*  if (mJokeType==MainJokeFragment.TEXT){
            mKooHFJuheJokeRecyclerAdapter = new KooHFJokeRecyclerAdapter(mJokeType,mContentlistBeanList,
                    mRecyclerViewWithEmpty, R.layout.item_long_text);
        }else{*/
            mKooHFJuheJokeRecyclerAdapter = new KooHFJuheJokeRecyclerAdapter(11,mDataBeanList,
                    mRecyclerViewWithEmpty, R.layout.item_big_image);
        //###}

        //
        mKooHFJuheJokeRecyclerAdapter.setOnItemClickListener(new HeaderFooterRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position, Object data) {
                Intent intent = new Intent(getActivity(), ImageShowActivity.class);
                //intent.putExtra("newsbean",(JokeImageAndTextListBean.ContentlistBean)data);
                Juhe_JokeImageAndTextListBean.DataBean dataBean= (Juhe_JokeImageAndTextListBean.DataBean) data;


                    ImageShowBean imageShowBean=new ImageShowBean();
                    imageShowBean.setTitle("标题"+dataBean.getContent());
                    //imageShowBean.setContent(dataBean.getText());
                    imageShowBean.setImageUrl(dataBean.getUrl());
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
        mRecyclerViewWithEmpty.setAdapter(mKooHFJuheJokeRecyclerAdapter);
        mRecyclerViewWithEmpty.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (KooHFNewsRecyclerAdapter.isReachedBottomNormal(recyclerView)
                        && !mKooHFJuheJokeRecyclerAdapter.isDataLoading()
                        && !mKooHFJuheJokeRecyclerAdapter.isLoadComplete()) {
                    KLog.d("isReachedBottom");
                    //start
                    mKooHFJuheJokeRecyclerAdapter.setDataLoading(true);
                    mKooHFJuheJokeRecyclerAdapter.setFooterViewNormal();
                    //
                    String saveLastLoadDataTime_key="saveLastLoadDataTime_key_"+mParam2;
                    String saveLastLoadDataTime= (String) SharedPreferencesTool.get(getActivity(),saveLastLoadDataTime_key,"");

                    //  KLog.d("isReachedBottom:getName:"+mChannelListBean.getName());
                    mJuheJokePresenter.gainJuheJokeData("",saveLastLoadDataTime,JuheJokeModelImpl.SORT_DESC,mKooHFJuheJokeRecyclerAdapter.turnNextAndBackPageNum());
                    KLog.d("isReachedBottom:getPageNum:"+mKooHFJuheJokeRecyclerAdapter.getPageNum());
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
    public void setJuheJokeListData(JuHeBaseBean<Juhe_JokeImageAndTextListBean> juHeBaseBean) {
        /**
         *
         */
        //数据返回成功保存时间
        String time=String.valueOf(System.currentTimeMillis()/1000);
        SharedPreferencesTool.put(getActivity(),"saveLastRefreshDataTime_key_"+mParam2,time);
        SharedPreferencesTool.put(getActivity(),"saveLastLoadDataTime_key_"+mParam2,time);

        //
        //第一页的时候
        if (mKooHFJuheJokeRecyclerAdapter.getPageNum()== KooHFJuheJokeRecyclerAdapter.PAGE_NUM_DEFAULT){
            KLog.d("第一页:");
            mSwipeRefreshLayout.setRefreshing(false);
            if (juHeBaseBean.getResult() != null &&
                    juHeBaseBean.getResult().getData() != null){
                mKooHFJuheJokeRecyclerAdapter.refreshDataList(juHeBaseBean.getResult().getData());
            }else{
                Toast.makeText(KooApplication.getAppContext(), "刷新数据出错，请重试", Toast.LENGTH_SHORT).show();
            }

        }else{
            KLog.d("第"+mKooHFJuheJokeRecyclerAdapter.getPageNum()+"页:");
            //
            if (juHeBaseBean.getResult() != null &&
                    juHeBaseBean.getResult().getData() != null) {

                mKooHFJuheJokeRecyclerAdapter.setDataLoading(false);

                mKooHFJuheJokeRecyclerAdapter.appendDataList(juHeBaseBean.getResult().getData());
                //每页数据加载完后
                mKooHFJuheJokeRecyclerAdapter.setFooterView(null);

            }else {
                //数据加载完后
                mKooHFJuheJokeRecyclerAdapter.setFooterViewComplete();
                mKooHFJuheJokeRecyclerAdapter.setLoadComplete(true);
            }
        }
        //
    }
}
