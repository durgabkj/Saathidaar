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
import com.ottego.saathidaar.Adapter.SentInvitationAdapter;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentSentInboxBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;

import org.json.JSONObject;


public class SentInboxFragment extends Fragment  implements ApiListener {
  FragmentSentInboxBinding b;
    Context context;
    SessionManager sessionManager;
    DataModelInbox data;
    String member_id;
    InboxViewModel viewModel;
    public String InvitationSentUrl ="http://103.150.186.33:8080/saathidaar_backend/api/request/sent/get/all/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SentInboxFragment() {
        // Required empty public constructor
    }

    public static SentInboxFragment newInstance(String param1, String param2) {
        SentInboxFragment fragment = new SentInboxFragment();
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
       b=FragmentSentInboxBinding.inflate(getLayoutInflater());
        context=getContext();
        sessionManager=new SessionManager(context);
        member_id=sessionManager.getMemberId();
        viewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        getData();
        listener();
       return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewSentInvitation.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void getData() {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InvitationSentUrl+member_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewSentInvitation.setRefreshing(false);
              //  progressDialog.dismiss();
                Log.e(" deleted Invitation response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelInbox.class);
                if (data.results == 1) {
                    viewModel._list.postValue(data.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewSentInvitation.setRefreshing(false);
               // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvSentInvitation.setLayoutManager(layoutManager);
        b.rvSentInvitation.setHasFixedSize(true);
        b.rvSentInvitation.setNestedScrollingEnabled(true);
        SentInvitationAdapter adapter = new SentInvitationAdapter(context,data.data,this);
        b.rvSentInvitation.setAdapter(adapter);

        if (adapter.getItemCount() != 0) {
            b.llNoDataSentInvitation.setVisibility(View.GONE);
            b.rvSentInvitation.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataSentInvitation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int position) {
        getData();
    }

    @Override
    public void onFail(int position) {

    }
}