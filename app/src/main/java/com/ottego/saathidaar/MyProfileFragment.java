package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.ProfileViewPager;
import com.ottego.saathidaar.Fragment.FamilyInfoFragment;
import com.ottego.saathidaar.Fragment.PersonalInfoFragment;
import com.ottego.saathidaar.Fragment.ProfessionalInfoFragment;


public class MyProfileFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView tvUserName, tvUserEmail,tvUserDetailsReadLess,tvUserDetailsReadMore,tvAboutUs;
    SessionManager sessionManager;
    AppCompatImageView ivClear;
    Context context;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = getContext();
        sessionManager=new SessionManager(context);
        sessionManager = new SessionManager(context);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserName = view.findViewById(R.id.tvUserName);
        tabLayout = view.findViewById(R.id.tlProfile);
        viewPager = view.findViewById(R.id.vpMyProfile);
        tvUserDetailsReadMore=view.findViewById(R.id.tvUserDetailsReadMore);
        tvUserDetailsReadLess=view.findViewById(R.id.tvUserDetailsReadLess);
        tvAboutUs=view.findViewById(R.id.tvAboutUs);

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        listener();
        setPreLoadData();
        return view;

    }

    private void listener() {
        tvUserDetailsReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAboutUs.setMaxLines(30);
                tvUserDetailsReadMore.setVisibility(View.GONE);
                tvUserDetailsReadLess.setVisibility(View.VISIBLE);
            }
        });


        tvUserDetailsReadLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAboutUs.setMaxLines(4);
                tvUserDetailsReadMore.setVisibility(View.VISIBLE);
                tvUserDetailsReadLess.setVisibility(View.GONE);
            }
        });


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        ProfileViewPager adapter = new ProfileViewPager(getChildFragmentManager());
        adapter.addFragment(new PersonalInfoFragment(), "Personal Info");
        adapter.addFragment(new FamilyInfoFragment(), "Family Info");
        adapter.addFragment(new ProfessionalInfoFragment(), "Professional Info");
        viewPager.setAdapter(adapter);
    }

    private void setPreLoadData() {
        tvUserName.setText(sessionManager.getName());
        tvUserEmail.setText(sessionManager.getEmail());

    }
}