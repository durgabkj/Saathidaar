package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.databinding.ActivityShowImageBinding;
import com.ottego.saathidaar.databinding.FragmentShowImageBinding;


public class ShowImageFragment extends Fragment {
FragmentShowImageBinding b;
    Context context;
    ImageModel model;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public ShowImageFragment() {
        // Required empty public constructor
    }

    public static ShowImageFragment newInstance(String param1, String param2) {
        ShowImageFragment fragment = new ShowImageFragment();
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
       b=FragmentShowImageBinding.inflate(inflater, container, false);

//        Bundle bundle = requireActivity().getIntent().getExtras();
//        String data = bundle.getString("data");
//        model = new Gson().fromJson(data, ImageModel.class);


        Log.e("image",mParam1);
        setData();
        listener();
       return b.getRoot();
    }


    private void listener() {

    }


    private void setData() {
        Glide.with(getActivity())
                .load(Utils.imageUrl + mParam1)
                .into(b.ivProfilePic);
    }
}