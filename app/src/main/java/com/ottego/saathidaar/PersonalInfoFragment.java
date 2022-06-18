package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.FragmentPersonalInfoBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class PersonalInfoFragment extends Fragment {
    FragmentPersonalInfoBinding binding;
    public static String url = Utils.memberUrl + "get-details/11";
    Context context;
    SessionManager sessionManager;
    MemberProfileModel model;
    String id = "";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        binding = FragmentPersonalInfoBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(getContext());
        listener();
        getMemberData();
        return binding.getRoot();
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
        binding.tvUserAge.setText(model.age);
        binding.tvDob.setText(model.date_of_birth);
        binding.tvUserMaritalStatus.setText(model.marital_status);
        binding.tvUseNoOfChild.setText(model.no_of_children);

        binding.tvUserHeight.setText(model.height);
        binding.tvBloodGroup.setText(model.blood_group);
        binding.tvUserDiet.setText(model.lifestyles);

        binding.tvUserMotherTongue.setText(model.mother_tounge);
        binding.tvHealthDetail.setText(model.health_info);
        binding.tvUserReligion.setText(model.religion_name);

        binding.tvUserCommunity.setText(model.caste_name);
        binding.tvUserSubCommunity.setText(model.subcaste);
        binding.tvUserGotra.setText(model.gothra);

    }

    private void listener() {
        binding.ivCameraEditPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditPersonalActivity.class);
                startActivity(intent);
            }
        });

    }
}