package com.ottego.saathidaar;

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
import com.ottego.saathidaar.Adapter.RecentVisitorAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentRecentViewBinding;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import org.json.JSONException;
import org.json.JSONObject;


public class RecentViewFragment extends Fragment implements ApiListener {
    FragmentRecentViewBinding b;
    SessionManager sessionManager;
    Context context;
    MatchViewModel viewModel;
    DataModelNewMatches data;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String recentVisitor = Utils.memberUrl + "recent-visitors/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecentViewFragment() {
        // Required empty public constructor
    }

    public static RecentViewFragment newInstance(String param1, String param2) {
        RecentViewFragment fragment = new RecentViewFragment();
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
        b = FragmentRecentViewBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);

        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        getData();
        listener();
        return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewRecentView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    public void getData() {
      //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                recentVisitor + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                 b.srlRecycleViewRecentView.setRefreshing(false);
             //   progressDialog.dismiss();
                Log.e("recent visitors response", String.valueOf(response));
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
                b.srlRecycleViewRecentView.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }


    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvRecentView.setLayoutManager(layoutManager);
        b.rvRecentView.setHasFixedSize(true);
        b.rvRecentView.setNestedScrollingEnabled(true);
        RecentVisitorAdapter adapter = new RecentVisitorAdapter(context, data.data,this);
        b.rvRecentView.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataRecentView.setVisibility(View.GONE);
            b.rvRecentView.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataRecentView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int position) {
        getData();
        viewModel.getDataCount();
    }

    @Override
    public void onFail(int position) {

    }
}