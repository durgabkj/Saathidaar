
package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.ottego.saathidaar.Adapter.BlockMemberAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentBlockMemberBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

import org.json.JSONException;
import org.json.JSONObject;


public class BlockMemberFragment extends Fragment implements ApiListener{
FragmentBlockMemberBinding b;
    SessionManager sessionManager;
    Context context;
    MatchViewModel viewModel;
    InboxViewModel inboxViewModel;
    DataModelNewMatches data;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String blockMember = Utils.memberUrl + "get/block-member/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlockMemberFragment() {
        // Required empty public constructor
    }

    public static BlockMemberFragment newInstance(String param1, String param2) {
        BlockMemberFragment fragment = new BlockMemberFragment();
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
        b = FragmentBlockMemberBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);

        viewModel = new ViewModelProvider(requireActivity()).get(MatchViewModel.class);
        inboxViewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        getData();
        listener();
        return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewBlock.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }


    public void getData() {
      //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                blockMember + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  b.srlRecycleBookmark.setRefreshing(false);
             b.srlRecycleViewBlock.setRefreshing(false);
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
                error.printStackTrace();
                b.srlRecycleViewBlock.setRefreshing(false);
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }


    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvBlock.setLayoutManager(layoutManager);
        b.rvBlock.setHasFixedSize(true);
        b.rvBlock.setNestedScrollingEnabled(true);
        BlockMemberAdapter adapter = new BlockMemberAdapter(context, data.data,this);
        b.rvBlock.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataBlock.setVisibility(View.GONE);
            b.rvBlock.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataBlock.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSuccess(int position) {
        inboxViewModel.getDataCount();
        getData();
    }

    @Override
    public void onFail(int position) {

    }
}