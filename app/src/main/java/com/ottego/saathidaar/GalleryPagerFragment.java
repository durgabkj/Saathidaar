package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.saathidaar.Adapter.ViewPagerGalleryAdapter;
import com.ottego.saathidaar.Adapter.ViewPagerMemberGalleryAdapter;
import com.ottego.saathidaar.databinding.FragmentGalleryPagerBinding;
import com.ottego.saathidaar.viewmodel.GalleryViewModel;

import java.util.Objects;


public class GalleryPagerFragment extends DialogFragment {
FragmentGalleryPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GalleryViewModel viewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GalleryPagerFragment() {
        // Required empty public constructor
    }
    @Override
    public int getTheme() {
        return R.style.FullScreenDialogTheme;
    }

    public static GalleryPagerFragment newInstance(String param1, String param2) {
        GalleryPagerFragment fragment = new GalleryPagerFragment();
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
        b=FragmentGalleryPagerBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(GalleryViewModel.class);


        // of ViewPager2Adapter
        ViewPagerGalleryAdapter viewPager2Adapter = new ViewPagerGalleryAdapter(requireActivity(), viewModel);
        ViewPagerMemberGalleryAdapter viewPagerMemberGalleryAdapter = new ViewPagerMemberGalleryAdapter(requireActivity(), viewModel);

        // adding the adapter to viewPager2
        // to show the views in recyclerview
        b.vp2DetailsImage.setAdapter(viewPagerMemberGalleryAdapter);
        b.vp2DetailsImage.setAdapter(viewPager2Adapter);
        b.vp2DetailsImage.setCurrentItem(Integer.parseInt(mParam1));
        // To get swipe event of viewpager2

        b.vp2DetailsImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
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
                    b.llPreviousImage.setVisibility(View.INVISIBLE);
                } else {
                    b.llPreviousImage.setVisibility(View.VISIBLE);
                }
                if (position < Objects.requireNonNull(b.vp2DetailsImage.getAdapter()).getItemCount() - 1) {
                    b.llNextImage.setVisibility(View.VISIBLE);
                } else {
                    b.llNextImage.setVisibility(View.INVISIBLE);
                }


            }
            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);

            }
        });



        b.vp2DetailsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.vp2DetailsImage.setCurrentItem(Integer.parseInt(mParam1));
            }
        });


        b.llNextImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b.vp2DetailsImage.setCurrentItem(b.vp2DetailsImage.getCurrentItem()+1, true);
            }
        });

        b.llPreviousImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b.vp2DetailsImage.setCurrentItem(b.vp2DetailsImage.getCurrentItem()-1, true);
            }
        });


        return b.getRoot();
    }
}