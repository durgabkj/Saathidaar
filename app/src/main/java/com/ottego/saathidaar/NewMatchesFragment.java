package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
import com.ottego.saathidaar.databinding.FragmentNewMatchesBinding;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

import org.json.JSONObject;


public class NewMatchesFragment extends Fragment {
    Context context;
    FragmentNewMatchesBinding b;
    Animation animation;
    DataModelNewMatches data;
    SessionManager sessionManager;
    public String MyMatchUrl = Utils.memberUrl + "new/matches/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    NewMatchViewModel viewModel;

    public NewMatchesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewMatchesFragment newInstance(String param1, String param2) {
        NewMatchesFragment fragment = new NewMatchesFragment();
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
        b = FragmentNewMatchesBinding.inflate(inflater,container,false);
        context = getContext();
        viewModel = new ViewModelProvider(requireActivity()).get(NewMatchViewModel.class);

        sessionManager=new SessionManager(context);
        animation = AnimationUtils.loadAnimation(context, R.anim.move);
        b.llCard.startAnimation(animation);
        getData("");
        listener();


        //   adapter.notifyDataSetChanged();
        return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewNewMatch.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData("");
            }
        });
    }

    public void getData(String id) {
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                MyMatchUrl+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewNewMatch.setRefreshing(false);
                Log.e("New Matches response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                if (data.results == 1) {
                    viewModel._list.postValue(data.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewNewMatch.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }


    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvNewMatches.setLayoutManager(layoutManager);
        b.rvNewMatches.setHasFixedSize(true);
        b.rvNewMatches.setNestedScrollingEnabled(true);
        NewMatchesAdapter adapter = new NewMatchesAdapter(context, data.data);
        b.rvNewMatches.setAdapter(adapter);

        if (adapter.getItemCount() != 0) {
            b.llNoData.setVisibility(View.GONE);
            b.rvNewMatches.setVisibility(View.VISIBLE);

        } else {
            b.llNoData.setVisibility(View.VISIBLE);
        }
    }
}