package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.databinding.FragmentMemberGalleryShowBinding;


public class MemberGalleryShowFragment extends Fragment {
FragmentMemberGalleryShowBinding b;
    Context context;
    SessionManager sessionManager;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public MemberGalleryShowFragment() {
        // Required empty public constructor
    }

    public static MemberGalleryShowFragment newInstance(String param1, String param2) {
        MemberGalleryShowFragment fragment = new MemberGalleryShowFragment();
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
        b=FragmentMemberGalleryShowBinding.inflate(inflater, container, false);

        context = getContext();
        sessionManager = new SessionManager(context);

        Log.e("image_path", mParam1);
        Log.e("image_id", mParam2);
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