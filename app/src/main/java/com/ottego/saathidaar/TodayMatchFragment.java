package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
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
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentTodayMatchBinding;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

import org.json.JSONException;
import org.json.JSONObject;


public class TodayMatchFragment extends Fragment implements ApiListener {
    public String MyMatchUrl = Utils.memberUrl + "todays/matches/";
    Context context;
    SessionManager sessionManager;
    DataModelNewMatches data;
    FragmentTodayMatchBinding b;
    NewMatchViewModel viewModel;
    int count=0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayMatchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static TodayMatchFragment newInstance(String param1, String param2) {
        TodayMatchFragment fragment = new TodayMatchFragment();
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
       b=FragmentTodayMatchBinding.inflate(getLayoutInflater());
        context = getContext();
sessionManager=new SessionManager(context);
        viewModel = new ViewModelProvider(requireActivity()).get(NewMatchViewModel.class);
        getData();
        listener();
        return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewTodayMatch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData() {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                MyMatchUrl+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewTodayMatch.setRefreshing(false);
              //  progressDialog.dismiss();
                Log.e("Today Matches response", String.valueOf(response));
                Gson gson = new Gson();
                try {
                    if (response.getInt("results")==1) {
                        data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                        viewModel._list.postValue(data.data);
                        setRecyclerView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();
                error.printStackTrace();
                b.srlRecycleViewTodayMatch.setRefreshing(false);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }


    private void setRecyclerView() {
       // count++;
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvTodayMatches.setLayoutManager(layoutManager);
        b.rvTodayMatches.setHasFixedSize(true);
        b.rvTodayMatches.setNestedScrollingEnabled(true);
        NewMatchesAdapter adapter = new NewMatchesAdapter(context, data.data,this);
        b.rvTodayMatches.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataToday.setVisibility(View.GONE);
            b.rvTodayMatches.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataToday.setVisibility(View.VISIBLE);
        }
     //   refresh(1000);
    }


    private void refresh(int millisecond) {

        final Handler handler= new Handler();
        final  Runnable runnable=new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        handler.postDelayed(runnable,millisecond);
    }

    @Override
    public void onSuccess(int position) {
        getData();
    }

    @Override
    public void onFail(int position) {

    }
}