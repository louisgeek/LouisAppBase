package com.louisgeek.louisappbase.jokes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooFragmentPagerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainJokeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainJokeFragment extends BaseFragment{

    KooFragmentPagerAdapter mKooFragmentPagerAdapter;

    public  static  final  int GIF_IMAGE=1;
    public  static  final  int NORMAL_IMAGE=2;
    public  static  final  int TEXT=3;

    public MainJokeFragment() {
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
    public static MainJokeFragment newInstance(String param1, String param2) {
        MainJokeFragment fragment = new MainJokeFragment();
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
        //return inflater.inflate(R.layout.fragment_main_joke, container, false);
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_main_joke;
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


    }



    @Override
    protected KooFragmentPagerAdapter setupKooFragmentPagerAdapter() {
        mKooFragmentPagerAdapter = new KooFragmentPagerAdapter(getChildFragmentManager());
        // KooFragmentPagerAdapter pagerAdapter = new KooFragmentPagerAdapter(getgetSupportFragmentManager());
       mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("动图", String.valueOf(GIF_IMAGE)), "动图");
       mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("图片", String.valueOf(NORMAL_IMAGE)), "图片");
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("文字", String.valueOf(TEXT)), "文字");
        return mKooFragmentPagerAdapter;
    }

   /* @Override
    public void setJokeListData(ShowAPIBaseBean<JokeImageListBean> jokeImageListBeanShowAPIBaseBean) {
        List<JokeImageListBean.ContentlistBean> JOKEcontentlistBean=jokeImageListBeanShowAPIBaseBean.getShowapi_res_body().getContentlist();

        for (int i = 0; i < JOKEcontentlistBean.size(); i++) {
            String name=JOKEcontentlistBean.get(i).getImg()
            mKooFragmentPagerAdapter.addFragment(NewsListFragment.newInstance(name,String.valueOf(i)), name);
            InfoHolderSingleton.getInstance().putMapObj("channelListBean_key_"+i,channelListBeanList.get(i));
        }
        mKooFragmentPagerAdapter.notifyDataSetChanged();

    }*/
}
