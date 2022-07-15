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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

    }

}