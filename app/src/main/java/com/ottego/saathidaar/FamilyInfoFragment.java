package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.FragmentFamilyInfoBinding;
import com.ottego.saathidaar.databinding.FragmentPersonalInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class FamilyInfoFragment extends Fragment {
FragmentFamilyInfoBinding b;
    MemberProfileModel model;
    Context context;
    public static String url = Utils.memberUrl + "get-details/11";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FamilyInfoFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FamilyInfoFragment newInstance(String param1, String param2) {
        FamilyInfoFragment fragment = new FamilyInfoFragment();
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
        b = FragmentFamilyInfoBinding.inflate(inflater, container, false);
        context=getContext();
        listener();
        getMemberData();
        return b.getRoot();
    }

    private void listener() {
        b.ivCameraEditFamilyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),FamilyProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMemberData() {
        Log.e("url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
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
        b.tvUserFatherStatus.setText(model.father_status);
        b.tvUserMotherStatus.setText(model.marital_status);
        b.tvUserFamilyLocation.setText(model.family_location);
        b.tvUserNativePlace.setText(model.native_place);
        int brother=(Integer.parseInt(model.married_male) + Integer.parseInt(model.unmarried_male));
        int sister=(Integer.parseInt(model.married_female) + Integer.parseInt(model.unmarried_female));
        b.tvUserBrothers.setText(brother+","+model.married_male+" : Married"+","+model.unmarried_male+" : Unmarried");
        b.tvUserSisters.setText(sister+","+model.married_female+" : Married"+","+model.unmarried_female+" : Unmarried");
        b.tvUserFamilyType.setText(model.family_type);
        b.tvUserFamilyAffluence.setText(model.family_affluence);
    }
}