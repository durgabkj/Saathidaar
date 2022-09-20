package com.ottego.saathidaar.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.FamilyProfileActivity;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.MySingleton;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;
import com.ottego.saathidaar.databinding.FragmentFamilyInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class FamilyInfoFragment extends Fragment {
FragmentFamilyInfoBinding b;
    MemberProfileModel model;
    Context context;
    SessionManager sessionManager;
    int count = 0;
    String fatherStatus = "";
    String motherStatus = "";
    String companyNameF = "";
    String designationF = "";
    String natureBusinessF = "";
    String companyNameM = "";
    String designationM = "";
    String natureBusinessM = "";

    public static String url = Utils.memberUrl + "my-profile/";
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
        sessionManager=new SessionManager(context);
        getMemberData();

        if(b.tvUserBrothersMarried.getText().toString().equalsIgnoreCase("")) {
            b.tvUserBrothersMarried.setVisibility(View.GONE);
        }

        if(b.tvUserBrothersUMarried.getText().toString().equalsIgnoreCase("")) {
            b.tvUserBrothersUMarried.setVisibility(View.GONE);
        }


        return b.getRoot();
    }

    private void listener() {
        b.srlRecycleViewFamilyDetails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberData();
                setData();
            }
        });

        b.ivCameraEditFamilyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FamilyProfileActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                context.startActivity(intent);
            }
        });
        b.tvUserMotherStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                motherStatus = b.tvUserMotherStatus.getText().toString();
                if (motherStatus.equalsIgnoreCase("Retired")) {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Employed")) {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Business")) {
                    b.llMNatureBusiness.setVisibility(View.VISIBLE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Not Employed")) {
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Passed Away")) {
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }

                if (motherStatus.equalsIgnoreCase("Home Maker")) {
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.tvUserFatherStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                fatherStatus =b.tvUserFatherStatus.getText().toString();

                if (fatherStatus.equalsIgnoreCase("Retired")) {
                    b.llFCompany.setVisibility(View.VISIBLE);
                    b.llFDesignation.setVisibility(View.VISIBLE);
                    b.llBusiness.setVisibility(View.GONE);
                }

                if (fatherStatus.equalsIgnoreCase("Employed")) {
                    b.llFCompany.setVisibility(View.VISIBLE);
                    b.llFDesignation.setVisibility(View.VISIBLE);
                    b.llBusiness.setVisibility(View.GONE);
                }
                if (fatherStatus.equalsIgnoreCase("Business")) {
                    b.llBusiness.setVisibility(View.VISIBLE);
                    b.llFCompany.setVisibility(View.GONE);
                    b.llFDesignation.setVisibility(View.GONE);
                }

                if (fatherStatus.equalsIgnoreCase("Not Employed")) {
                    b.llBusiness.setVisibility(View.GONE);
                    b.llFCompany.setVisibility(View.GONE);
                    b.llFDesignation.setVisibility(View.GONE);
                }


                if (fatherStatus.equalsIgnoreCase("Passed Away")) {
                    b.llBusiness.setVisibility(View.GONE);
                    b.llFCompany.setVisibility(View.GONE);
                    b.llFDesignation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getMemberData() {
       final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
             progressDialog.dismiss();
                b.srlRecycleViewFamilyDetails.setRefreshing(false);
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
                progressDialog.dismiss();
                b.srlRecycleViewFamilyDetails.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    private void setData() {
        if (model != null) {
            b.tvUserSistersMarried.setVisibility(View.VISIBLE);
            b.tvUserSistersUMarried.setVisibility(View.VISIBLE);
            b.tvUserBrothersMarried.setVisibility(View.VISIBLE);
            b.tvUserBrothersUMarried.setVisibility(View.VISIBLE);

            b.tvUserFatherStatus.setText(model.father_status);
            b.tvUserMotherStatus.setText(model.mother_status);
            b.tvUserFamilyLocation.setText(model.family_location);
            b.tvUserNativePlace.setText(model.native_place);
            b.tvUserSistersMarried.setText(model.married_female+" : Married");
            b.tvUserSistersUMarried.setText(model.unmarried_female+" : UnMarried");
            b.tvUserBrothersMarried.setText(model.married_male + " : Married");
            b.tvUserBrothersUMarried.setText(model.unmarried_male + " : UnMarried");


            b.tvUserFatherCompany.setText(model.father_company_name);
            b.tvUserFatherDesignation.setText(model.father_designation);
            b.tvUserFatherNatureofBusiness.setText(model.father_business_name);

            b.tvUserMotherCompany.setText(model.mother_company_name);
            b.tvUserMotherDesignation.setText(model.mother_designation);
            b.tvUserMotherNatureofBusiness.setText(model.mother_business_name);


            if(model.married_female.equalsIgnoreCase(""))
            {
                b.tvUserSistersMarried.setVisibility(View.GONE);
            }


            if(model.unmarried_female.equalsIgnoreCase(""))
            {
                b.tvUserSistersUMarried.setVisibility(View.GONE);
            }

            if(model.married_male.equalsIgnoreCase(""))
            {
                b.tvUserBrothersMarried.setVisibility(View.GONE);
            }

            if(model.unmarried_male.equalsIgnoreCase(""))
            {
                b.tvUserBrothersUMarried.setVisibility(View.GONE);
            }


           b.tvUserFamilyType.setText(model.family_type);
            b.tvUserFamilyAffluence.setText(model.family_affluence);
            b.tvUserFatherCompany.setText(model.father_company_name);
            b.tvUserFatherDesignation.setText(model.father_designation);
            b.tvUserFatherNatureofBusiness.setText(model.father_business_name);
            b.tvUserMotherCompany.setText(model.mother_company_name);
            b.tvUserMotherDesignation.setText(model.mother_designation);
            b.tvUserMotherNatureofBusiness.setText(model.mother_business_name);
        }



    }

}