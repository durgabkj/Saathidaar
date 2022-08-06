package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.HomeTablayoutAdapter;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.databinding.FragmentInboxBinding;

import org.json.JSONObject;


public class InboxFragment extends Fragment {
    public String url = "http://103.150.186.33:8080/saathidaar_backend/api/request/count/accept-request/";
    FragmentInboxBinding b;
    DataModelDashboard model;
    Context context;
    SessionManager sessionManager;
    InvitationFragment invitationFragment = new InvitationFragment();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
int count=0;
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
        context = getContext();
        sessionManager = new SessionManager(context);

        b.vpInbox.setPagingEnable(false);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvInbox, invitationFragment)
                .commit();

        b.tlInbox.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                b.vpMatch.setCurrentItem(tab.getPosition());
                Fragment fragment = null;

                switch (tab.getPosition()){
                    case 0:{
                        fragment= InvitationFragment.newInstance("","");
                        break;
                    }
                    case 1:{
                        fragment= AcceptedInboxFragment.newInstance("", "");
                        break;
                    }
                    case 2:{
                        fragment= SentInboxFragment.newInstance("", "");
                        break;
                    }
                    case 3:{
                        fragment= DeleteInboxFragment.newInstance("", "");
                        break;
                    }
                    case 4:{
                        fragment= BlockMemberFragment.newInstance("", "");
                        break;
                    }

                }

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvInbox, fragment)
                        .commit();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getDataCount();
        return b.getRoot();
    }



    private void getDataCount() {
        count++;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
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
refresh(1000);

    }

    private void setData() {
//        RequestAccept.setText(model.data.get(0).accept_request_count);
//        RequestSent.setText(model.data.get(0).sent_request_count);
//        Visitors.setText(model.data.get(0).recent_visitors_count);
        if (model.data != null && model.data.size() > 0) {

            BadgeDrawable badgeDrawable = b.tlInbox.getTabAt(1).getOrCreateBadge();
            badgeDrawable.setNumber(Integer.parseInt(model.data.get(0).accept_request_count));
            badgeDrawable.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            badgeDrawable.setBadgeTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);

            BadgeDrawable badgeDrawable1 = b.tlInbox.getTabAt(2).getOrCreateBadge();
            badgeDrawable1.setNumber(Integer.parseInt(model.data.get(0).sent_request_count));
            badgeDrawable1.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            badgeDrawable1.setBadgeTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            badgeDrawable1.setBadgeGravity(BadgeDrawable.TOP_END);


            BadgeDrawable badgeDrawable2 = b.tlInbox.getTabAt(3).getOrCreateBadge();
            badgeDrawable2.setNumber(Integer.parseInt(model.data.get(0).deleted_request_count));
            badgeDrawable2.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            badgeDrawable2.setBadgeTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            badgeDrawable2.setBadgeGravity(BadgeDrawable.TOP_END);


            BadgeDrawable badgeDrawable3 = b.tlInbox.getTabAt(4).getOrCreateBadge();
            badgeDrawable3.setNumber(Integer.parseInt(model.data.get(0).block_request_count));
            badgeDrawable3.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            badgeDrawable3.setBadgeTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            badgeDrawable3.setBadgeGravity(BadgeDrawable.TOP_END);


            BadgeDrawable badgeDrawable4= b.tlInbox.getTabAt(0).getOrCreateBadge();
            badgeDrawable4.setNumber(Integer.parseInt(model.data.get(0).invitations_count));
            badgeDrawable4.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            badgeDrawable4.setBadgeTextColor(ContextCompat.getColor(getActivity(), R.color.white));
            badgeDrawable4.setBadgeGravity(BadgeDrawable.TOP_END);

        }
    }

    private void refresh(int millisecond) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getDataCount();
            }
        };

        handler.postDelayed(runnable, millisecond);

    }


}