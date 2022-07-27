package com.ottego.saathidaar.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.MySingleton;
import com.ottego.saathidaar.ProfessionalDetailEditActivity;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;
import com.ottego.saathidaar.databinding.FragmentProfessionalInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class ProfessionalInfoFragment extends Fragment {
    public  String url = Utils.memberUrl + "my-profile/";
   FragmentProfessionalInfoBinding b;
    Context context;
    SessionManager sessionManager;
    MemberProfileModel model;
    String memberId;
    int count=0;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfessionalInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProfessionalInfoFragment newInstance(String param1, String param2) {
        ProfessionalInfoFragment fragment = new ProfessionalInfoFragment();
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
        b= FragmentProfessionalInfoBinding.inflate(getLayoutInflater());
        context=getContext();
        sessionManager=new SessionManager(context);
        memberId=sessionManager.getMemberId();
        listener();
        getMemberData();
        return b.getRoot();
    }
    private void getMemberData() {
        count++;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+memberId, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlRecycleViewProfessionalDetails.setRefreshing(false);
             //   Log.e("response", String.valueOf(response));
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
                b.srlRecycleViewProfessionalDetails.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
refresh(1000);
    }

    private void setData() {
        if (model != null) {
            b.tvUserHigherEdu.setText(model.highest_qualification);
            b.tvUserCollegeName.setText(model.college_attended);
            b.tvUserIncome.setText(model.annual_income);
            b.tvUserWorkingWitht.setText(model.working_with);
            b.tvWorkingAs.setText(model.working_as);
            b.tvUserCurrentResi.setText(model.country_name);
            b.tvUserStateOfResidence.setText(model.state);
            b.tvUserResidenceStatus.setText(model.city);
            b.tvUserPinCode.setText(model.pincode);
        }


    }

    private void listener() {

        b.srlRecycleViewProfessionalDetails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberData();
            }
        });


        b.ivCameraEducationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProfessionalDetailEditActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                startActivity(intent);
            }
        });

    }


    private void refresh(int millisecond) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getMemberData();
            }
        };

        handler.postDelayed(runnable, millisecond);

    }
}