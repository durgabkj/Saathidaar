package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.NewMatchesAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentMyMatchBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import org.json.JSONException;
import org.json.JSONObject;


public class MyMatchFragment extends Fragment implements ApiListener {
    Context context;
    FragmentMyMatchBinding b;
    SessionManager sessionManager;
    DataModelNewMatches data;
    MatchViewModel viewModel;
    InboxViewModel inboxViewModel;
    public String MyMatchUrl = Utils.memberUrl + "my/matches/";
    static  final int ALARM_REQ_CODE=100;
    AlertDialog dialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyMatchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyMatchFragment newInstance(String param1, String param2) {
        MyMatchFragment fragment = new MyMatchFragment();
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

        b = FragmentMyMatchBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(context);
        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        inboxViewModel=new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        getData("");
        listener();
        return b.getRoot();
    }




    void listener() {
        b.srlRecycleViewMyMatches.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData("");
            }
        });
    }

    public void getData(String id) {
        //final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Data Loading...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                MyMatchUrl + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                // progressDialog.dismiss();
                b.srlRecycleViewMyMatches.setRefreshing(false);
                Log.e("My Matches response", String.valueOf(response));
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
                //  progressDialog.dismiss();
                b.srlRecycleViewMyMatches.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvMyMatches.setLayoutManager(layoutManager);
        b.rvMyMatches.setHasFixedSize(true);
        b.rvMyMatches.setNestedScrollingEnabled(true);
        NewMatchesAdapter adapter = new NewMatchesAdapter(context, data.data, this);
        b.rvMyMatches.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataMatch.setVisibility(View.GONE);
            b.rvMyMatches.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataMatch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int position) {
        getData("");
        viewModel.getDataCount();
        inboxViewModel.getDataCount();
    }

    @Override
    public void onFail(int position) {

    }
}