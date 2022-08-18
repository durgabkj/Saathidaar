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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.UpgradeAdapter;
import com.ottego.saathidaar.Model.DataModelUpgrade;
import com.ottego.saathidaar.databinding.FragmentUpgradeBinding;

import org.json.JSONObject;


public class UpgradeFragment extends Fragment {
    FragmentUpgradeBinding b;
    Context context;
    DataModelUpgrade data;
    public String UpgradeUrl = Utils.memberUrl + "plans-details";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpgradeFragment() {
        // Required empty public constructor
    }

    public static UpgradeFragment newInstance(String param1, String param2) {
        UpgradeFragment fragment = new UpgradeFragment();
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
        b = FragmentUpgradeBinding.inflate(getLayoutInflater());
        context=getContext();
        getData();
        listener();
        return b.getRoot();
    }
    void listener() {

    }

    public void getData() {
       final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                UpgradeUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.e("Upgrade response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelUpgrade.class);
                // model = new Gson().fromJson(String.valueOf(response), new TypeToken<List<UpgradeModel>>() {}.getType());
                Log.e("response", String.valueOf(response));
               setRecyclerView();

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


    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        b.rvUpgrade.setLayoutManager(layoutManager);
        b.rvUpgrade.setHasFixedSize(true);
        b.rvUpgrade.setNestedScrollingEnabled(true);
        UpgradeAdapter adapter = new UpgradeAdapter(context, data.data);
        b.rvUpgrade.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}