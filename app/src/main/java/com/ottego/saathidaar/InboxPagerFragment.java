package com.ottego.saathidaar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.ottego.saathidaar.Adapter.ViewPagerInboxDetailAdapter;
import com.ottego.saathidaar.Adapter.ViewPagerMatchDetailAdapter;
import com.ottego.saathidaar.databinding.FragmentInboxPagerBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import java.util.Objects;

public class InboxPagerFragment extends DialogFragment {
    FragmentInboxPagerBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    InboxViewModel viewModel;
    int position=0;
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
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvInboxDetailsShow, InboxDetailFragment.newInstance(viewModel.list1.getValue().get(position).member_id, ""))
                .commit();


        b.llNextInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=  position+1;
                if (position == 0) {
                    b.llPreviousInbox.setVisibility(View.INVISIBLE);
                } else {
                    b.llPreviousInbox.setVisibility(View.VISIBLE);
                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvDetailsShow, MatchDetailsFragment.newInstance(viewModel.list1.getValue().get(position).member_id, ""))
                        .commit();

            }
        });
        b.llPreviousInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position=  position-1;
                if (position < Objects.requireNonNull(position - 1)) {
                    b.llNextInbox.setVisibility(View.VISIBLE);
                } else {
                    b.llNextInbox.setVisibility(View.INVISIBLE);
                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvDetailsShow, MatchDetailsFragment.newInstance(viewModel.list1.getValue().get(position).member_id, ""))
                        .commit();
            }
        });



        return b.getRoot();
    }
}