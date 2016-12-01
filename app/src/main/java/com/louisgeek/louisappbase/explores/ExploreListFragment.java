package com.louisgeek.louisappbase.explores;


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
 * Use the {@link ExploreListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreListFragment extends BaseFragment {



    public ExploreListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreListFragment newInstance(String param1, String param2) {
        ExploreListFragment fragment = new ExploreListFragment();
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
       // return inflater.inflate(R.layout.fragment_explore_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int setupLayoutId() {
        return R.layout.fragment_explore_list;
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

    }

    @Override
    protected KooFragmentPagerAdapter setupKooFragmentPagerAdapter() {
        return null;
    }

}
