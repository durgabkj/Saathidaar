package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.ottego.saathidaar.databinding.FragmentHomeBinding;

import java.util.List;


public class HomeFragment extends Fragment {
    FragmentHomeBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DashBoardFragment dashBoardFragment=new DashBoardFragment();
  Context context;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        b = FragmentHomeBinding.inflate(inflater, container, false);
context=getContext();
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvHome, dashBoardFragment.newInstance("",""))
                .commit();

        b.chipGroupHome.check(b.chipGroupHome.getChildAt(0).getId());



        listener();
        return b.getRoot();

    }

    private void listener() {
        b.chipGroupHome.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                b.vpHome.setCurrentItem(group.getCheckedChipId());
                Fragment fragment = null;
                switch (group.getCheckedChipId()) {
                    case R.id.chipDashBoard: {
                        fragment = DashBoardFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipProfile: {
                        fragment = MyProfileFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipPreference: {
                        fragment = PartnerPreferenceFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipHoroscope: {
                        fragment = HoroscopeFragment.newInstance("", "");
                        break;
                    }
                    default: {
                        fragment = MyProfileFragment.newInstance("", "");
                        break;
                    }

                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvHome, fragment)
                        .commit();


            }

        });
    }


}