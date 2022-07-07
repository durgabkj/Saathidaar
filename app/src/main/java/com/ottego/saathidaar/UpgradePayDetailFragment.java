package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.databinding.FragmentUpgradePayDetailBinding;

public class UpgradePayDetailFragment extends BottomSheetDialogFragment {
FragmentUpgradePayDetailBinding b;
    UpgradeModel model;
    Context context;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpgradePayDetailFragment() {
        // Required empty public constructor
    }


    public static UpgradePayDetailFragment newInstance(String param1, String param2) {
        UpgradePayDetailFragment fragment = new UpgradePayDetailFragment();
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
        b=FragmentUpgradePayDetailBinding.inflate(inflater, container, false);
context=getContext();

        String data = getArguments().getString("data");
        model = new Gson().fromJson(data, UpgradeModel.class);
        return  b.getRoot();
    }
}