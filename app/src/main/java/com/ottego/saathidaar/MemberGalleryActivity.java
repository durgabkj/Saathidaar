package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.ImageAdapter;
import com.ottego.saathidaar.Adapter.MemberImageGalleryAdapter;
import com.ottego.saathidaar.Model.DataModelImage;
import com.ottego.saathidaar.databinding.ActivityGalleryBinding;
import com.ottego.saathidaar.databinding.ActivityMemberGalleryBinding;
import com.ottego.saathidaar.viewmodel.GalleryViewModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MemberGalleryActivity extends AppCompatActivity {
ActivityMemberGalleryBinding b;
    SessionManager sessionManager;
    DataModelImage dataModelImage;
    String getImageURL = Utils.memberUrl + "app/get/approve/photo/";
    Context context;
    GalleryViewModel viewModel;
    String member_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMemberGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        context=MemberGalleryActivity.this;
        sessionManager=new SessionManager(context);
        member_id = getIntent().getStringExtra("Member_id");
      //  Log.e("hello durga",member_id);
        getData();
        listener();
    }
    private void listener() {
        b.srlRecycleViewMemberGallery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


        b.mtGalleryToolBarMemberImage.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getData() {
        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getImageURL+member_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   progressDialog.dismiss();
                b.srlRecycleViewMemberGallery.setRefreshing(false);
                // Log.e("image response", String.valueOf(response));
                Gson gson = new Gson();
                dataModelImage = gson.fromJson(String.valueOf(response), DataModelImage.class);
                if (dataModelImage.results == 1) {
                    viewModel._list.postValue(dataModelImage.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewMemberGallery.setRefreshing(false);
                // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }
    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        b.rvMemberImage.setLayoutManager(layoutManager);
        b.rvMemberImage.setHasFixedSize(true);
        b.rvMemberImage.setNestedScrollingEnabled(true);
        MemberImageGalleryAdapter adapter = new MemberImageGalleryAdapter(context, dataModelImage.data);
        b.rvMemberImage.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataImage.setVisibility(View.GONE);
            b.llCardMember.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataImage.setVisibility(View.VISIBLE);
        }
    }
}