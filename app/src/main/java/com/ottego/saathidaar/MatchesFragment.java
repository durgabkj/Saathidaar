package com.ottego.saathidaar;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.HomeTablayoutAdapter;
import com.ottego.saathidaar.databinding.FragmentMatchesBinding;


public class MatchesFragment extends Fragment {
    FragmentMatchesBinding b;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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
        b = FragmentMatchesBinding.inflate(getLayoutInflater());

//scroll auto
       // b.hsvMatch.fullScroll(ScrollView.FOCUS_LEFT);


//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                b.TodayMatch.performClick();
//            }
//        }, 1000);



        return b.getRoot();
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(b.vpMatch);
        b.tlMatch.setupWithViewPager(b.vpMatch);
        b.tlMatch.getTabAt(1).select();


        b.vpMatch.setPagingEnable(false);

        b.tlMatch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                b.vpMatch.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        HomeTablayoutAdapter adapter = new HomeTablayoutAdapter(getChildFragmentManager());
        adapter.addFragment(new NewMatchesFragment(), "New Matches");
        adapter.addFragment(new MyMatchFragment(), "My Matches");
        adapter.addFragment(new TodayMatchFragment(), "Today's Matches ");
        adapter.addFragment(new ShortListFragment(), "Shortlisted");
        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new RecentViewFragment(), "Recent View");
        adapter.addFragment(new RecentlyViewedFragment(), "Recently Viewed");
        viewPager.setAdapter(adapter);
    }
}

