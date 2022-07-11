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
    TextView tvUserName, tvUserEmail,tvUserDetails;
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
        tvUserDetails=view.findViewById(R.id.tvUserDetails);

//
//        tabLayout.addTab(tabLayout.newTab().setText("Personal Info"));
//        tabLayout.addTab(tabLayout.newTab().setText("Family Info"));
//        tabLayout.addTab(tabLayout.newTab().setText("Professional Info"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        ProfileViewPager adapter = new ProfileViewPager(getContext(), requireActivity().getSupportFragmentManager(), tabLayout.getTabCount());
//        viewPager.setAdapter(adapter);

        // viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        setPreLoadData();
        listener();
        return view;

    }

    private void listener() {
        tvUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View layout_dialog= LayoutInflater.from(context).inflate(R.layout.detail_layout,null);
                builder.setView(layout_dialog);
                AppCompatTextView tvDetail =layout_dialog.findViewById(R.id.tvDetail);
                 AppCompatTextView tvDetailName =layout_dialog.findViewById(R.id.tvDetailName);
                AppCompatImageView ivClear =layout_dialog.findViewById(R.id.ivClear);
               tvDetail.setText("ViewPager2 is a ViewGroup backed by a RecyclerView and thus the handling method is similar to that for RecyclerView. ViewPager2 requires an adapter to show its contents and the adapter can be either RecyclerView adapter or FragmentStateAdapter.\n" +
                       "\n" +
                       "This article will cover the basic handling of ViewPager2 and the linking with a TabLayout. If you have been familiar with the setup of ViewPager2, you could jump to “Trick” session directly.\n" +
                       "\n" +
                       "Setup\n" +
                       "ViewPager2 is packed inside the latest AndroidX library of JetPack instead of Material Component library. Thus, we have to import it separately with the following gradle code:\n" +
                       "\n" +
                       "\n" +
                       "Bear in mind that AndroidX library should not be kept together support library to prevent any unexpected results.\n" +
                       "\n" +
                       "XML layout\n" +
                       "Simply add ViewPager2 widget to you  \"ViewPager2 is packed inside the latest AndroidX library of JetPack instead of Material Component library. Thus, we have to import it separately with the following gradle code:\\n\" +\n" +
                       "                       \"\\n\" +\n" +
                       "                       \"\\n\" +\n" +
                       "                       \"Bear in mind that AndroidX library should not be kept together support library to prevent any unexpected results.\\n\" +\n" +
                       "                       \"\\n\" +");
                // show dialog

                tvDetailName.setText("About  "+sessionManager.getName());
                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.setCancelable(false);

                dialog.getWindow().setGravity(Gravity.CENTER);


                ivClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
dialog.dismiss();
                    }
                });

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