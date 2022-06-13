package com.ottego.saathidaar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.saathidaar.databinding.FragmentPersonalInfoBinding;
import com.ottego.saathidaar.databinding.FragmentProfessionalInfoBinding;


public class ProfessionalInfoFragment extends Fragment {

   FragmentProfessionalInfoBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfessionalInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfessionalInfoFragment newInstance(String param1, String param2) {
        ProfessionalInfoFragment fragment = new ProfessionalInfoFragment();
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
        b= FragmentProfessionalInfoBinding.inflate(getLayoutInflater());
        listener();

        return b.getRoot();
    }

    private void listener() {
        b.ivCameraEducationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),ProfessionalDetailEditActivity.class);
                startActivity(intent);
            }
        });

    }
}