package com.ottego.saathidaar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.datatransport.runtime.scheduling.jobscheduling.AlarmManagerSchedulerBroadcastReceiver;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.databinding.FragmentMatchesBinding;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;


public class MatchesFragment extends Fragment {
    FragmentMatchesBinding b;
    SessionManager sessionManager;
    Context context;
    DataModelDashboard model;
    public String url = "http://103.174.102.195:8080/saathidaar_backend/api/request/count/accept-request/";
     int count=0;
    MyMatchFragment myMatchFragment=new MyMatchFragment();
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
        b = FragmentMatchesBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);
        b.vpMatch.setPagingEnable(false);

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvMatches, MyMatchFragment.newInstance("",""))
                .commit();

        b.chipGroup.check(b.chipGroup.getChildAt(1).getId());

        getDataCount();
        listener();
        return b.getRoot();

    }



    private void listener() {
        b.chipGroup.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                b.vpMatch.setCurrentItem(group.getCheckedChipId());
                Fragment fragment = null;
                switch (group.getCheckedChipId()) {
                    case R.id.chipNewMatch: {
                        fragment = NewMatchesFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipMyMatch: {
                        fragment = MyMatchFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipTodayMatch: {
                        fragment = TodayMatchFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipPremiumMatch: {
                        fragment = PremiumMatchesFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipShortListed:{
                        fragment= ShortListFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipSearch:{
                        fragment= SearchFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipRecentView:{
                        fragment= RecentViewFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipRecentlyView:{
                        fragment= RecentlyViewedFragment.newInstance("", "");
                        break;
                    }
                    default:{
                        fragment= RecentlyViewedFragment.newInstance("", "");
                        break;
                    }

                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvMatches,fragment)
                        .commit();


            }

        });


        b.srlMatches.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataCount();
            }
        });
    }

    private void getDataCount() {
        b.srlMatches.setRefreshing(false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response count inbox", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlMatches.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    private void setData() {
        if (model.data != null && model.data.size() > 0) {
            b.chipNewMatch.setText("New Matches " + "(" + model.data.get(0).new_matches_count + ")");
            b.chipMyMatch.setText("My Matches " + "(" + model.data.get(0).my_matches_count + ")");
            b.chipTodayMatch.setText("Today's Matches " + "(" + model.data.get(0).todays_matches_count + ")");
            // b.chip1.setText("Premium Matches "+"("+model.data.get(0).new_matches_count+")");
            // b.chip1.setText("Search "+"("+model.data.get(0).new_matches_count+")");
            b.chipRecentView.setText("Recent Visitors " + "(" + model.data.get(0).recent_visitors_count + ")");

            b.chipShortListed.setText("ShortListed " + "(" + model.data.get(0).shortlists_count + ")");
        }
    }

//    @Override
//    public void onStart() {
//        getDataCount();
//        super.onStart();
//    }

    private void refresh(int millisecond) {
        final Handler handler= new Handler();
        final  Runnable runnable=new Runnable() {
            @Override
            public void run() {
                getDataCount();
            }
        };

        handler.postDelayed(runnable, millisecond);
    }

//    @Override
//    public void onResume() {
//        getDataCount();
//        super.onResume();
//    }
}

