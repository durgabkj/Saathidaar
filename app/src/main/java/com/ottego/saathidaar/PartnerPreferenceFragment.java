package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.saathidaar.databinding.FragmentPartnerPreferenceBinding;


public class PartnerPreferenceFragment extends Fragment {
FragmentPartnerPreferenceBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartnerPreferenceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PartnerPreferenceFragment newInstance(String param1, String param2) {
        PartnerPreferenceFragment fragment = new PartnerPreferenceFragment();
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
        b=FragmentPartnerPreferenceBinding.inflate(getLayoutInflater());
        return inflater.inflate(R.layout.fragment_partner_preference, container, false);
    }
}