package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ottego.saathidaar.databinding.FragmentNewMatchesBinding;


public class NewMatchesFragment extends Fragment {
Context context;
FragmentNewMatchesBinding b;
    Animation animation;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NewMatchesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewMatchesFragment newInstance(String param1, String param2) {
        NewMatchesFragment fragment = new NewMatchesFragment();
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
        b=FragmentNewMatchesBinding.inflate(getLayoutInflater());
        context=getContext();

        animation = AnimationUtils.loadAnimation(context, R.anim.move);
        b.llCard.startAnimation(animation);
       return b.getRoot();
    }
}