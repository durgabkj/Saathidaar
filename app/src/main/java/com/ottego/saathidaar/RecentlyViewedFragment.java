package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.NewMatchesAdapter;
import com.ottego.saathidaar.Adapter.RecentVisitorAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentRecentlyViewedBinding;

import org.json.JSONObject;

public class RecentlyViewedFragment extends Fragment {
FragmentRecentlyViewedBinding b;
    SessionManager sessionManager;
    Context context;
    DataModelNewMatches data;

    public String recentlyViewed = Utils.memberUrl + "view-to/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecentlyViewedFragment() {
        // Required empty public constructor
    }

    public static RecentlyViewedFragment newInstance(String param1, String param2) {
        RecentlyViewedFragment fragment = new RecentlyViewedFragment();
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
        b=FragmentRecentlyViewedBinding.inflate(inflater,container,false);
        context = getContext();
        sessionManager = new SessionManager(context);
        getData();
        return b.getRoot();
    }


    public void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                recentlyViewed + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  b.srlRecycleBookmark.setRefreshing(false);
                progressDialog.dismiss();
                Log.e("recent visitors response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                if (data.results == 1) {
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }


    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(context,2);
        b.rvRecentlyView.setLayoutManager(layoutManager);
        b.rvRecentlyView.setHasFixedSize(true);
        b.rvRecentlyView.setNestedScrollingEnabled(true);
        RecentVisitorAdapter adapter = new RecentVisitorAdapter(context, data.data);
        b.rvRecentlyView.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataRecentlyView.setVisibility(View.GONE);
            b.rvRecentlyView.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataRecentlyView.setVisibility(View.VISIBLE);
        }
    }
}