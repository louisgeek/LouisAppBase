package com.louisgeek.louisappbase.news;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooFragmentPagerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;
import com.louisgeek.louisappbase.news.bean.LocalNews_AreaBean;
import com.louisgeek.louisappbase.news.bean.NewsChannelBean;
import com.louisgeek.louisappbase.base.ShowAPIBaseBean;
import com.louisgeek.louisappbase.news.presenter.LocalNewsAreaPresenterImpl;
import com.louisgeek.louisappbase.news.presenter.NewsChannelPresenterImpl;
import com.louisgeek.louisappbase.news.view.INewsView;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainNewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainNewsFragment extends BaseFragment implements INewsView {

    KooFragmentPagerAdapter mKooFragmentPagerAdapter;

    NewsChannelPresenterImpl mNewsChannelPresenter;
    LocalNewsAreaPresenterImpl mLocalNewsAreaPresenter;
    public MainNewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainNewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainNewsFragment newInstance(String param1, String param2) {
        MainNewsFragment fragment = new MainNewsFragment();
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
        // View view=inflater.inflate(R.layout.fragment_main_news, container, false);
       return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_main_news;
    }

    @Override
    protected int setupViewPagerResId() {
        return R.id.id_view_pager;
    }

    @Override
    protected int setupTabLayoutResId() {
        return R.id.id_tab_layout;
    }

    @Override
    protected void initView(View rootView) {

        mNewsChannelPresenter=new NewsChannelPresenterImpl(this);
        mNewsChannelPresenter.gainNewsChannelData();

        /*mLocalNewsAreaPresenter=new LocalNewsAreaPresenterImpl(this);
        mLocalNewsAreaPresenter.gainLocalNewsAreaData();*/

    }
    @Override
    protected KooFragmentPagerAdapter setupKooFragmentPagerAdapter() {
        mKooFragmentPagerAdapter = new KooFragmentPagerAdapter(getChildFragmentManager());
        // KooFragmentPagerAdapter pagerAdapter = new KooFragmentPagerAdapter(getgetSupportFragmentManager());
      /*   mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance("头条", ""), "头条");
       mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance("娱乐", ""), "娱乐");
        mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance("手机", ""), "手机");
        mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance("体育", ""), "体育");*/
        return mKooFragmentPagerAdapter;
    }

    @Override
    public void setNewChannelData(ShowAPIBaseBean<NewsChannelBean> showAPIBaseBean) {
        List<NewsChannelBean.ChannelListBean> channelListBeanList=showAPIBaseBean.getShowapi_res_body().getChannelList();
        for (int i = 0; i < channelListBeanList.size(); i++) {
            String name=channelListBeanList.get(i).getName().replace("最新","").replace("焦点","");

            String key="ChannelListBean_key_"+i;
            InfoHolderSingleton.getInstance().putMapObj(key,channelListBeanList.get(i));
            /**
             *
             */
            mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance(name,String.valueOf(i)), name);
           // KLog.d("kkk"+channelListBeanList.get(i));
           // KLog.d("kkk:i"+i);


        }
    }

    @Override
    public void setLocalNewsArea(ShowAPIBaseBean<LocalNews_AreaBean> showAPIBaseBean) {
        List<LocalNews_AreaBean.CityListBean> cityListBeanList=showAPIBaseBean.getShowapi_res_body().getCityList();
        if (cityListBeanList!=null&&cityListBeanList.size()>0) {
            for (int i = 0; i < cityListBeanList.size(); i++) {
                String name = cityListBeanList.get(i).getAreaName();
                if (name != null) {
                    if (name.equals("北京") || name.equals("上海") || name.equals("广东") || name.equals("浙江")) {
                        String areaId = cityListBeanList.get(i).getAreaId();

                        //
                        InfoHolderSingleton.getInstance().putMapObj("cityListBeanList_key_" + areaId, cityListBeanList.get(i));

                        mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance(name, areaId), name);

                    }
                }
            }
        }
    }
}
