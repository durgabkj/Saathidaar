package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.databinding.ActivityMatchesDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchesDetailsActivity extends AppCompatActivity {
    Animation animation;
    ActivityMatchesDetailsBinding b;
NewMatchesModel model;
MemberProfileModel model1;
public  String memberDetail=Utils.memberUrl+"get-details/";
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMatchesDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context=MatchesDetailsActivity.this;

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, NewMatchesModel.class);
        animation = AnimationUtils.loadAnimation(context, R.anim.move);
      //  b.llDetailCad.startAnimation(animation);

listener();
        getData();
    }

    private void listener() {
        b.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                memberDetail+model.member_id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        model1 = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
                        setData();
                    }else {
                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);




    }

    private void setData() {
    b.tvNewMatchName.setText(model1.first_name+" "+model.last_name);
    b.tvNewMatchAge.setText(model1.age +" yrs");
        b.tvDetailHeight.setText(model1.height +" feet");
        b.tvMatchCityDetail.setText(model1.city);
        b.tvNewMatchWorkAsDetail.setText(model1.working_as);
b.tvAboutUserDetails.setText(model1.about_ourself);
    }
}