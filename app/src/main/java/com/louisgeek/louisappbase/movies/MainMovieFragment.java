package com.louisgeek.louisappbase.movies;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooFragmentPagerAdapter;
import com.louisgeek.louisappbase.base.BaseFragment;
import com.louisgeek.louisappbase.jokes.JokeListFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMovieFragment extends BaseFragment {

    KooFragmentPagerAdapter mKooFragmentPagerAdapter;
    public MainMovieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMovieFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMovieFragment newInstance(String param1, String param2) {
        MainMovieFragment fragment = new MainMovieFragment();
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
       // return inflater.inflate(R.layout.fragment_main_movie, container, false);
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_main_movie;
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
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("单日", "" ), "单日");
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("周末", ""), "周末");
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("单周", ""), "单周");
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("单月",  ""), "单月");
        mKooFragmentPagerAdapter.addFragment(JokeListFragment.newInstance("影院",  ""), "影院");
        return mKooFragmentPagerAdapter;
    }

}
