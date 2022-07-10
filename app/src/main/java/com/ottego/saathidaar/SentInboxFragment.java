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
import com.ottego.saathidaar.Adapter.InboxInvitationAdapter;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentSentInboxBinding;

import org.json.JSONObject;


public class SentInboxFragment extends Fragment {

  FragmentSentInboxBinding b;
    Context context;
    SessionManager sessionManager;
    DataModelInbox data;
    String member_id;
    public String InvitationSentUrl ="http://192.168.14.120:9094/api/request/sent/get/all/";
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
        getData();
        listener();
       return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewSentInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InvitationSentUrl+member_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewSentInvitation.setRefreshing(false);
                progressDialog.dismiss();
                Log.e(" deleted Invitation response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelInbox.class);
                if (data.results == 1) {
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewSentInvitation.setRefreshing(false);
                progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }
    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvSentInvitation.setLayoutManager(layoutManager);
        b.rvSentInvitation.setHasFixedSize(true);
        b.rvSentInvitation.setNestedScrollingEnabled(true);
        InboxInvitationAdapter adapter = new InboxInvitationAdapter(context,data.data);
        b.rvSentInvitation.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() != 0) {
            b.llNoDataSentInvitation.setVisibility(View.GONE);
            b.rvSentInvitation.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataSentInvitation.setVisibility(View.VISIBLE);
        }
    }
}