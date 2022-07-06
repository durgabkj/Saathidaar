package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.databinding.FragmentMatchDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MatchDetailsFragment extends Fragment {
    FragmentMatchDetailsBinding b;
    Animation animation;
    NewMatchesModel model;
    MemberProfileModel model1;
    public String memberDetail = Utils.memberUrl + "get-details/";
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MatchDetailsFragment newInstance(String param1, String param2) {
        MatchDetailsFragment fragment = new MatchDetailsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentMatchDetailsBinding.inflate(inflater, container, false);
        context = getContext();

        listener();
         getData();
        setData();
        return b.getRoot();
    }


    private void listener() {
        b.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("member_ID",mParam1);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, memberDetail, null, new Response.Listener<JSONObject>() {
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
        b.tvNewMatchName.setText(model1.first_name + " " + model.last_name);
        b.tvNewMatchAge.setText(model1.age + " yrs");
        b.tvDetailHeight.setText(model1.height + " feet");
        b.tvMatchCityDetail.setText(model1.city);
        b.tvNewMatchWorkAsDetail.setText(model1.working_as);
        b.tvAboutUserDetails.setText(model1.about_ourself);
    }
}