package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.Timer;
import java.util.TimerTask;

public class UpgradeOnButtonActivity extends AppCompatActivity {
    public String UpgradeUrl = Utils.memberUrl + "plans-details";
    ActivityUpgradeOnButtonBinding b;
    Context context;
    DataModelUpgrade data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityUpgradeOnButtonBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());


       listener();
    }

    private void listener() {
        b.mtbUpgradeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

}