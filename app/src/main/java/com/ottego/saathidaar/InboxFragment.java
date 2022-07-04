package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.HomeTablayoutAdapter;
import com.ottego.saathidaar.databinding.FragmentHomeBinding;
import com.ottego.saathidaar.databinding.FragmentInboxBinding;


public class InboxFragment extends Fragment {

  FragmentInboxBinding b;
  InvitationFragment invitationFragment=new InvitationFragment();
  AcceptedInboxFragment acceptedInboxFragment=new AcceptedInboxFragment();
  DeleteInboxFragment deleteInboxFragment=new DeleteInboxFragment();
  SentInboxFragment sentInboxFragment=new SentInboxFragment();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InboxFragment() {
        // Required empty public constructor
    }


    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
        b = FragmentInboxBinding.inflate(inflater, container, false);

//        FragmentManager fragmentManager=requireActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fcvSathidarInbox,invitationFragment);
//        fragmentTransaction.commit();
//        listener();
        return b.getRoot();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(b.vpInbox);
        b.tlInbox.setupWithViewPager(b.vpInbox);
        b.vpInbox.setPagingEnable(false);

        b.tlInbox.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                b.vpInbox.setCurrentItem(tab.getPosition());
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
        adapter.addFragment(new InvitationFragment(), "Invitation");
        adapter.addFragment(new AcceptedInboxFragment(), "Accept");
        adapter.addFragment(new SentInboxFragment(), "Sent");
        adapter.addFragment(new DeleteInboxFragment(), "Delete");
        viewPager.setAdapter(adapter);
    }
}