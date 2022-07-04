package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.saathidaar.Adapter.ViewPagerMatchDetailAdapter;
import com.ottego.saathidaar.databinding.FragmentMatchPagerBinding;


public class MatchPagerFragment extends DialogFragment {
FragmentMatchPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchPagerFragment() {
        // Required empty public constructor
    }


    public static MatchPagerFragment newInstance(String param1, String param2) {
        MatchPagerFragment fragment = new MatchPagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getTheme() {
        return com.google.android.material.R.style.ThemeOverlay_Material3_MaterialCalendar_Fullscreen;
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
      b=FragmentMatchPagerBinding.inflate(getLayoutInflater());



        // of ViewPager2Adapter
        ViewPagerMatchDetailAdapter viewPager2Adapter = new ViewPagerMatchDetailAdapter(getActivity());

        // adding the adapter to viewPager2
        // to show the views in recyclerview
       b.vp2Details.setAdapter(viewPager2Adapter);

        // To get swipe event of viewpager2
        b.vp2Details.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });




      return b.getRoot();
    }
}