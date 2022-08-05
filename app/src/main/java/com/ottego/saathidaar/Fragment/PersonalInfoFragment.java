package com.ottego.saathidaar.Fragment;

import android.annotation.SuppressLint;
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
import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.MySingleton;
import com.ottego.saathidaar.ProfileEditPersonalActivity;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;
import com.ottego.saathidaar.databinding.FragmentPersonalInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;


public class PersonalInfoFragment extends Fragment {
    FragmentPersonalInfoBinding binding;
    public static String url = Utils.memberUrl + "my-profile/";
    public String urlGetHoroscope = Utils.memberUrl + "horoscope/get/";
    Context context;
    MemberProfileModel model;
    SessionManager sessionManager;
    HoroscopeModel horoscopeModel;
    String id = "";
    int count = 0;
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
        sessionManager = new SessionManager(context);
        listener();
        getMemberData();
        getData();
        return binding.getRoot();
    }


    private void getData() {
        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                urlGetHoroscope + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  progressDialog.dismiss();
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        horoscopeModel = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
                        setHoroData();

                    } else {
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
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    @SuppressLint("SetTextI18n")
    private void setHoroData() {

        if(horoscopeModel!=null && !horoscopeModel.equals(""))
        {
            if((horoscopeModel.hours!=null || !horoscopeModel.hours.isEmpty()) && (horoscopeModel.minutes!=null || !horoscopeModel.minutes.isEmpty()) && (horoscopeModel.time!=null || !horoscopeModel.time.isEmpty()) && (horoscopeModel.time_status!=null || !horoscopeModel.time_status.isEmpty()))
            {
                binding.tvUserTimeofBirth.setText(horoscopeModel.hours +":"+horoscopeModel.minutes + " "+horoscopeModel.time + " , "+horoscopeModel.time_status);
            }

            if((horoscopeModel.country_of_birth!=null || !horoscopeModel.country_of_birth.isEmpty()) && (horoscopeModel.city_of_birth!=null || !horoscopeModel.city_of_birth.isEmpty()))
            {
                binding.tvUserHPlaceofBirth.setText(horoscopeModel.country_of_birth +", "+ horoscopeModel.city_of_birth);
            }

     }

    }

    private void listener() {

        binding.srlRecycleViewPersonalDetails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberData();
                getData();
            }
        });


        binding.ivCameraEditPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditPersonalActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                context.startActivity(intent);
//                startActivity(intent);
            }
        });
        binding.ivAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditPersonalActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                context.startActivity(intent);
//                startActivity(intent);
            }
        });


        binding.tvUserMaritalStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String status = binding.tvUserMaritalStatus.getText().toString().trim();
                if (status.equalsIgnoreCase("Never Married")) {
                    binding.llChild.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        binding.tvUserReligion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String religion = binding.tvUserReligion.getText().toString().trim();
                if (religion.equalsIgnoreCase("Hindi")) {
                    binding.llGotra.setVisibility(View.VISIBLE);
                }else {
                    binding.llGotra.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getMemberData() {
     count++;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
              //  Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
//                        binding.llNoDataPersonal.setVisibility(View.VISIBLE);
//                        binding.scrvPersonalData.setVisibility(View.GONE);
                        model = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
                        // SessionProfileDetailModel model = gson.fromJson(String.valueOf(response.getJSONObject("data")), SessionProfileDetailModel.class);
//                        sessionManager.CreateProfileSession(model);
                        setData();
                    } else {

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
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
        refresh(3000);
    }

    private void setData() {

        if (model != null ) {
            binding.tvDob.setText(model.date_of_birth);
            binding.tvUserAge.setText(model.age);
            binding.tvUserMaritalStatus.setText(model.marital_status);
            binding.tvUseNoOfChild.setText(model.no_of_children);
            binding.tvUserHeight.setText(model.height);
            binding.tvBloodGroup.setText(model.blood_group);
            binding.tvUserDiet.setText(model.lifestyles);
            binding.tvUserMotherTongue.setText(model.mother_tounge);
            binding.tvHealthDetail.setText(model.health_info);
            binding.tvUserReligion.setText(model.religion_name);
            binding.tvUserCommunity.setText(model.caste);
            binding.tvGender.setText(model.gender);
            binding.tvUserSubCommunity.setText(model.sub_caste_name);
            binding.tvUserGotra.setText(model.gothra);
        }

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
