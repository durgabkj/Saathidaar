package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.DeleteInvitationAdapter;
import com.ottego.saathidaar.Adapter.InboxInvitationAdapter;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.databinding.FragmentDeleteInboxBinding;
import com.ottego.saathidaar.viewmodel.InboxViewModel;

import org.json.JSONObject;


public class DeleteInboxFragment extends Fragment {
    Context context;
    SessionManager sessionManager;
    DataModelInbox data;
    String member_id;
    InboxViewModel viewModel;
    public String InvitationDeleteUrl ="http://103.150.186.33:8080/saathidaar_backend/api/request/rejected/get/all/";
    FragmentDeleteInboxBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public DeleteInboxFragment() {
        // Required empty public constructor
    }

    public static DeleteInboxFragment newInstance(String param1, String param2) {
        DeleteInboxFragment fragment = new DeleteInboxFragment();
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
     b=FragmentDeleteInboxBinding.inflate(getLayoutInflater());
        context=getContext();
        sessionManager=new SessionManager(context);
        member_id=sessionManager.getMemberId();
        viewModel = new ViewModelProvider(requireActivity()).get(InboxViewModel.class);
        getData();
        listener();
     return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewDeleteInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });
    }

    private void getData() {
     //   final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                InvitationDeleteUrl+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewDeleteInvitation.setRefreshing(false);
               // progressDialog.dismiss();
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
                b.srlRecycleViewDeleteInvitation.setRefreshing(false);
              //  progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }
    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvDeleteInvitation.setLayoutManager(layoutManager);
        b.rvDeleteInvitation.setHasFixedSize(true);
        b.rvDeleteInvitation.setNestedScrollingEnabled(true);
        DeleteInvitationAdapter adapter = new DeleteInvitationAdapter(context,data.data);
        b.rvDeleteInvitation.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() != 0) {
            b.llNoDataDeleteInvitation.setVisibility(View.GONE);
            b.rvDeleteInvitation.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataDeleteInvitation.setVisibility(View.VISIBLE);
        }
    }
}