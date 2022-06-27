package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.HomeTablayoutAdapter;
import com.ottego.saathidaar.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment {
    FragmentHomeBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DashBoardFragment dashBoardFragment=new DashBoardFragment();
    MyProfileFragment myProfileFragment=new MyProfileFragment();
    HoroscopeFragment horoscopeFragment=new HoroscopeFragment();
    PartnerPreferenceFragment partnerPreferenceFragment=new PartnerPreferenceFragment();
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

//        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fcvSathidarDashboard,dashBoardFragment);
//        fragmentTransaction.commit();


//        b.btnDashboard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//
//                       fragmentTransaction.replace(R.id.fcvSathidarDashboard,dashBoardFragment);
//                        fragmentTransaction.commit();
//            }
//        });
//
//
//        b.tvMyProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//
//                fragmentTransaction.replace(R.id.fcvSathidarDashboard,myProfileFragment);
//                fragmentTransaction.commit();
//            }
//        });
//
//
//        b.tvHomePartnerPreference.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fcvSathidarDashboard,partnerPreferenceFragment);
//                fragmentTransaction.commit();
//            }
//        });
//
//        b.tvHoroscope.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fcvSathidarDashboard,horoscopeFragment);
//                fragmentTransaction.commit();
//            }
//        });
        return b.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(b.vpHome);
        b.tlHome.setupWithViewPager(b.vpHome);


        b.tlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                b.vpHome.setCurrentItem(tab.getPosition());
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

        HomeTablayoutAdapter adapter = new HomeTablayoutAdapter(getChildFragmentManager());
        adapter.addFragment(new DashBoardFragment(), "DashBoard");
        adapter.addFragment(new MyProfileFragment(), "Profile");
        adapter.addFragment(new PartnerPreferenceFragment(), "Partner Preference");
        adapter.addFragment(new HoroscopeFragment(), "Horoscope");
        viewPager.setAdapter(adapter);
    }



}