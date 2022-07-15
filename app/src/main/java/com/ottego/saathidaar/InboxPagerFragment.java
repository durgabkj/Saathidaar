package com.ottego.saathidaar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ottego.saathidaar.Adapter.ViewPagerInboxDetailAdapter;
import com.ottego.saathidaar.databinding.FragmentInboxPagerBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;

public class InboxPagerFragment extends DialogFragment {
    FragmentInboxPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    InboxViewModel viewModel;

    public InboxPagerFragment() {
        // Required empty public constructor
    }

    public static InboxPagerFragment newInstance(String param1, String param2) {
        InboxPagerFragment fragment = new InboxPagerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        b = FragmentInboxPagerBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);


        // of ViewPager2Adapter
        ViewPagerInboxDetailAdapter viewPager2AdapterInbox = new ViewPagerInboxDetailAdapter(requireActivity(), viewModel);

        // adding the adapter to viewPager2
        // to show the views in recyclerview
        b.vp2DetailsInbox.setAdapter(viewPager2AdapterInbox);
        b.vp2DetailsInbox.setCurrentItem(Integer.parseInt(mParam1));
        // To get swipe event of viewpager2
        b.vp2DetailsInbox.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if(position==0) {
                    b.llPreviousInbox.setVisibility(View.INVISIBLE);
                }else  {
                    b.llPreviousInbox.setVisibility(View.VISIBLE);
                }
                if(position < b.vp2DetailsInbox.getAdapter().getItemCount() - 1) {
                    b.llNextInbox.setVisibility(View.VISIBLE);
                }else  {
                    b.llNextInbox.setVisibility(View.INVISIBLE);
                }

            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        b.llPreviousInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.vp2DetailsInbox.setCurrentItem(Integer.parseInt(mParam1));
            }
        });


        b.llNextInbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b.vp2DetailsInbox.setCurrentItem(b.vp2DetailsInbox.getCurrentItem()+1, true);
            }
        });


        b.llPreviousInbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                b.vp2DetailsInbox.setCurrentItem(b.vp2DetailsInbox.getCurrentItem()-1, true);
            }
        });


        return b.getRoot();
    }
}