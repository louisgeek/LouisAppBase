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
 * Use the {@link MainJuheJokeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainJuheJokeFragment extends BaseFragment {

    KooFragmentPagerAdapter mKooFragmentPagerAdapter;
    public  static  final  int IMAGE=1;
    public  static  final  int TEXT=2;

    public MainJuheJokeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainJuheJokeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainJuheJokeFragment newInstance(String param1, String param2) {
        MainJuheJokeFragment fragment = new MainJuheJokeFragment();
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
       // return inflater.inflate(R.layout.fragment_main_juhe_joke, container, false);
        return super.onCreateView( inflater,  container,
                 savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_main_juhe_joke;
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
        mKooFragmentPagerAdapter.addFragment(JuheJokeListFragment.newInstance("笑图", String.valueOf(IMAGE)), "笑图");
        mKooFragmentPagerAdapter.addFragment(JuheJokeListFragment.newInstance("笑文", String.valueOf(TEXT)), "笑文");
        return mKooFragmentPagerAdapter;
    }

}
