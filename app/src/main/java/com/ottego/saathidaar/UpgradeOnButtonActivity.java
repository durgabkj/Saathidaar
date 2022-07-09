package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.UpgradeAdapter;
import com.ottego.saathidaar.Model.DataModelUpgrade;
import com.ottego.saathidaar.databinding.ActivityUpgradeOnButtonBinding;

import org.json.JSONObject;

public class UpgradeOnButtonActivity extends AppCompatActivity {
ActivityUpgradeOnButtonBinding b;
    Context context;
    DataModelUpgrade data;
    public String UpgradeUrl = Utils.memberUrl + "plans-details";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityUpgradeOnButtonBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


        getData();
       listener();
    }

    private void listener() {
        b.UpgradeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }


    public void getData() {
        // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                UpgradeUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // progressDialog.dismiss();
                Log.e("Upgrade response", String.valueOf(response));
                Gson gson=new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelUpgrade.class);
                // model = new Gson().fromJson(String.valueOf(response), new TypeToken<List<UpgradeModel>>() {}.getType());
                Log.e("response", String.valueOf(response));
                setRecyclerView();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }


    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
        b.rvUpgrade1.setLayoutManager(layoutManager);
        b.rvUpgrade1.setHasFixedSize(true);
        b.rvUpgrade1.setNestedScrollingEnabled(true);
        UpgradeAdapter adapter = new UpgradeAdapter(context, data.data);
        b.rvUpgrade1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}