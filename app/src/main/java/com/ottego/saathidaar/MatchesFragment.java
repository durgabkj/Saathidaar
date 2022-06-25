package com.ottego.saathidaar;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ottego.saathidaar.databinding.FragmentMatchesBinding;


public class MatchesFragment extends Fragment {
    FragmentMatchesBinding b;
    NewMatchesFragment newMatchesFragment = new NewMatchesFragment();
    MyMatchFragment myMatchFragment = new MyMatchFragment();
    TodayMatchFragment todayMatchFragment = new TodayMatchFragment();
    ShortListFragment shortListFragment = new ShortListFragment();
    SearchFragment searchFragment = new SearchFragment();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
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
        b = FragmentMatchesBinding.inflate(getLayoutInflater());

//scroll auto
       // b.hsvMatch.fullScroll(ScrollView.FOCUS_LEFT);


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                b.TodayMatch.performClick();
            }
        }, 1000);


        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fcvNewMatches, newMatchesFragment);
        fragmentTransaction.commit();
        listener();
        return b.getRoot();
    }

    private void listener() {
        b.NewMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hsvMatch.scrollTo((int)b.hsvMatch.getScrollX() + 300, (int)b.hsvMatch.getScrollY());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fcvNewMatches, newMatchesFragment);
                fragmentTransaction.commit();
            }
        });


        b.MyMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.hsvMatch.scrollTo((int)b.hsvMatch.getScrollX() + 400, (int)b.hsvMatch.getScrollY());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fcvNewMatches, myMatchFragment);
                fragmentTransaction.commit();
            }
        });


        b.TodayMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.hsvMatch.scrollTo((int)b.hsvMatch.getScrollX() + 500, (int)b.hsvMatch.getScrollY());
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.fcvNewMatches, todayMatchFragment);
                fragmentTransaction.commit();
            }
        });

        b.MatchSortlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fcvNewMatches, shortListFragment);
                fragmentTransaction.commit();
            }
        });


        b.MatchSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fcvNewMatches, searchFragment);
                fragmentTransaction.commit();
            }
        });
    }


}