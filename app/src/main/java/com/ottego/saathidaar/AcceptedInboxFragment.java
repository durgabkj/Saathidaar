package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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
import com.ottego.saathidaar.Adapter.AcceptInvitationAdapter;
import com.ottego.saathidaar.Adapter.InboxInvitationAdapter;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentAcceptedInboxBinding;

import org.json.JSONObject;


public class AcceptedInboxFragment extends Fragment {
    Context context;

    SessionManager sessionManager;
    DataModelInbox data;
    String member_id;

    public String InvitationAcceptUrl = "http://192.168.1.37:9094/api/request/accepted/get/all/22";
    FragmentAcceptedInboxBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AcceptedInboxFragment() {
        // Required empty public constructor
    }


    public static AcceptedInboxFragment newInstance(String param1, String param2) {
        AcceptedInboxFragment fragment = new AcceptedInboxFragment();
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
        b = FragmentAcceptedInboxBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(context);
        member_id = sessionManager.getMemberId();
        getData();

        return b.getRoot();

    }

    private void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InvitationAcceptUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewAcceptInvitation.setRefreshing(false);
                progressDialog.dismiss();
                Log.e("Invitation response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelInbox.class);
                if (data.results == 1) {
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewAcceptInvitation.setRefreshing(false);
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvAcceptInvitation.setLayoutManager(layoutManager);
        b.rvAcceptInvitation.setHasFixedSize(true);
        b.rvAcceptInvitation.setNestedScrollingEnabled(true);
        AcceptInvitationAdapter adapter = new AcceptInvitationAdapter(context, data.data);
        b.rvAcceptInvitation.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() != 0) {
            b.llNoDataInvitation.setVisibility(View.GONE);
            b.rvAcceptInvitation.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataInvitation.setVisibility(View.VISIBLE);
        }
    }
}


