package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.AcceptInvitationAdapter;
import com.ottego.saathidaar.Adapter.HomeTablayoutAdapter;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentHomeBinding;
import com.ottego.saathidaar.databinding.FragmentInboxBinding;

import org.json.JSONObject;


public class InboxFragment extends Fragment {
    public String url = "http://192.168.1.36:9094/api/request/count/accept-request/";
  FragmentInboxBinding b;
    DataModelDashboard model;
    Context context;
    SessionManager sessionManager;
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
context=getContext();
sessionManager=new SessionManager(context);

        getDataCount();
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


    private void getDataCount() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);


    }

    private void setData() {
//        RequestAccept.setText(model.data.get(0).accept_request_count);
//        RequestSent.setText(model.data.get(0).sent_request_count);
//        Visitors.setText(model.data.get(0).recent_visitors_count);
        BadgeDrawable badgeDrawable= b.tlInbox.getTabAt(1).getOrCreateBadge();
        badgeDrawable.setNumber(Integer.parseInt(model.data.get(0).accept_request_count));
        b.tlInbox.getTabAt(2).getOrCreateBadge().setNumber(Integer.parseInt(model.data.get(0).sent_request_count));
       badgeDrawable.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
       badgeDrawable.setBadgeTextColor(ContextCompat.getColor(getActivity(),R.color.white));
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
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