package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
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
import com.ottego.saathidaar.Adapter.NewMatchesAdapter;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.ActivityMoreBinding;

import org.json.JSONObject;

public class MoreActivity extends AppCompatActivity {
ActivityMoreBinding b;
Context context;
DataModelNewMatches data;
private String searchedUrl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
context=MoreActivity.this;
        getData();

    }

    public void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                searchedUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  b.srlRecycleBookmark.setRefreshing(false);
                progressDialog.dismiss();
                Log.e("My search Matches response", String.valueOf(response));
                Gson gson = new Gson();
                data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                if (data.results == 1) {
                    setRecyclerView();
                }
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


    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvSearch.setLayoutManager(layoutManager);
        b.rvSearch.setHasFixedSize(true);
        b.rvSearch.setNestedScrollingEnabled(true);
        NewMatchesAdapter adapter = new NewMatchesAdapter(context, data.data);
        b.rvSearch.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataMore.setVisibility(View.GONE);
            b.rvSearch.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataMore.setVisibility(View.VISIBLE);
        }
    }
}