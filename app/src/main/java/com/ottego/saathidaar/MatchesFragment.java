package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.ottego.saathidaar.databinding.FragmentMatchesBinding;

import org.json.JSONObject;

import java.util.Objects;


public class MatchesFragment extends Fragment {
    FragmentMatchesBinding b;
    SessionManager sessionManager;
    Context context;
    DataModelDashboard model;
    public String url = "http://103.150.186.33:8080/saathidaar_backend/api/request/count/accept-request/";

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

context=getContext();
sessionManager=new SessionManager(context);
      /*  setUpViewPager(b.vpMatch);
        b.tlMatch.setupWithViewPager(b.vpMatch);
          Objects.requireNonNull(b.tlMatch.getTabAt(1)).select();

        b.tlMatch.getTabAt(7).view.setVisibility(View.GONE);
        b.vpMatch.setPagingEnable(false);*/
        b.vpMatch.setPagingEnable(false);
        Objects.requireNonNull(b.tlMatch.getTabAt(1)).select();


        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvMatches, myMatchFragment)
                .commit();


        b.tlMatch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

//                b.vpMatch.setCurrentItem(tab.getPosition());
                Fragment fragment = null;

                switch (tab.getPosition()){
                    case 0:{
                        fragment= NewMatchesFragment.newInstance("","");
                        break;
                    }
                    case 1:{
                        fragment= MyMatchFragment.newInstance("", "");
                        break;
                    }
                    case 2:{
                        fragment= TodayMatchFragment.newInstance("", "");
                        break;
                    }

                    case 3:{
                        fragment= PremiumMatchesFragment.newInstance("", "");
                        break;
                    }

                    case 4:{
                        fragment= ShortListFragment.newInstance("", "");
                        break;
                    }
                    case 5:{
                        fragment= SearchFragment.newInstance("", "");
                        break;
                    }
                    case 6:{
                        fragment= RecentViewFragment.newInstance("", "");
                        break;
                    }
                    case 7:{
                        fragment= RecentlyViewedFragment.newInstance("", "");
                        break;
                    }
                }

                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvMatches, fragment)
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


    }

    private void setData() {
        if (model.data != null && model.data.size() > 0)  {
            BadgeDrawable badgeDrawable = b.tlMatch.getTabAt(0).getOrCreateBadge();
            badgeDrawable.setNumber(Integer.parseInt(model.data.get(0).new_matches_count));
            badgeDrawable.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            badgeDrawable.setBadgeTextColor(ContextCompat.getColor(context, R.color.white));
            badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);


            BadgeDrawable badgeDrawable1 = b.tlMatch.getTabAt(1).getOrCreateBadge();
            badgeDrawable1.setNumber(Integer.parseInt(model.data.get(0).my_matches_count));
            badgeDrawable1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            badgeDrawable1.setBadgeTextColor(ContextCompat.getColor(context, R.color.white));
            badgeDrawable1.setMaxCharacterCount(10);
            badgeDrawable1.setBadgeGravity(BadgeDrawable.TOP_END);
            badgeDrawable1.setVisible(true);


            BadgeDrawable badgeDrawable2 = b.tlMatch.getTabAt(2).getOrCreateBadge();
            badgeDrawable2.setNumber(Integer.parseInt(model.data.get(0).todays_matches_count));
            badgeDrawable2.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            badgeDrawable2.setBadgeTextColor(ContextCompat.getColor(context, R.color.white));
            badgeDrawable2.setBadgeGravity(BadgeDrawable.TOP_END);

        }
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         }

    private void setUpViewPager(ViewPager viewPager) {
        HomeTablayoutAdapter adapter = new HomeTablayoutAdapter(getChildFragmentManager());
        adapter.addFragment(new NewMatchesFragment(), "New Matches");
        adapter.addFragment(new MyMatchFragment(), "My Matches");
        adapter.addFragment(new TodayMatchFragment(), "Today's Matches ");
        adapter.addFragment(new ShortListFragment(), "Shortlisted");
        adapter.addFragment(new SearchFragment(), "Search");
        adapter.addFragment(new RecentViewFragment(), "Recent Visitors");
        adapter.addFragment(new RecentlyViewedFragment(), "Recently Viewed");
        adapter.addFragment(new MoreFragment(), "More");
        viewPager.setAdapter(adapter);
    }
}

