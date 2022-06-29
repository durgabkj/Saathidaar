package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.saathidaar.databinding.FragmentPrivacyOptionBinding;


public class PrivacyOptionFragment extends Fragment {

 FragmentPrivacyOptionBinding b;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivacyOptionFragment() {
        // Required empty public constructor
    }


    public static PrivacyOptionFragment newInstance(String param1, String param2) {
        PrivacyOptionFragment fragment = new PrivacyOptionFragment();
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
         b=FragmentPrivacyOptionBinding.inflate(getLayoutInflater());
         listener();
        return b.getRoot();
    }

    private void listener() {
        b.PrivacyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.PrivacyName.setVisibility(View.GONE);
                b.PrivacyPhone.setVisibility(View.GONE);
                b.tvPrivacyPhoto.setVisibility(View.GONE);
                b.PrivacyDob.setVisibility(View.GONE);
                b.PrivacyHoroscope.setVisibility(View.GONE);
                b.PrivacyIncome.setVisibility(View.GONE);
                b.PrivacyNotDisturb.setVisibility(View.GONE);
                b.PrivacyProfile.setVisibility(View.GONE);
                b.PrivacyShortlist.setVisibility(View.GONE);
                b.PrivacyVisitors.setVisibility(View.GONE);
                b.PrivacyWebNotifications.setVisibility(View.GONE);

            }
        });
    }
}