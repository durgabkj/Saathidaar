package com.ottego.saathidaar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ottego.saathidaar.Adapter.ViewPagerMatchDetailAdapter;
import com.ottego.saathidaar.databinding.FragmentMatchPagerBinding;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import java.util.Objects;


public class MatchPagerFragment extends DialogFragment {
    FragmentMatchPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    //    model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    MatchViewModel viewModel;

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
        return R.style.FullScreenDialogTheme;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentMatchPagerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);


        // of ViewPager2Adapter
        ViewPagerMatchDetailAdapter viewPager2Adapter = new ViewPagerMatchDetailAdapter(requireActivity(), viewModel);

        // adding the adapter to viewPager2
        // to show the views in recyclerview
        b.vp2Details.setAdapter(viewPager2Adapter);
        b.vp2Details.setCurrentItem(Integer.parseInt(mParam1));
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


                if (position == 0) {
                    b.llPrevious.setVisibility(View.INVISIBLE);
                } else {
                    b.llPrevious.setVisibility(View.VISIBLE);
                }
                if (position < Objects.requireNonNull(b.vp2Details.getAdapter()).getItemCount() - 1) {
                    b.llNext.setVisibility(View.VISIBLE);
                } else {
                    b.llNext.setVisibility(View.INVISIBLE);
                }
            }
            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });

        b.vp2Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.vp2Details.setCurrentItem(Integer.parseInt(mParam1));
            }
        });
        b.llNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b.vp2Details.setCurrentItem(b.vp2Details.getCurrentItem()+1, true);
            }
        });

        b.llPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.vp2Details.setCurrentItem(b.vp2Details.getCurrentItem()-1, true);
            }
        });

        return b.getRoot();
    }
}

