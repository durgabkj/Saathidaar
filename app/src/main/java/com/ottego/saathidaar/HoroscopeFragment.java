package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.DataModelHoroscope;
import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.databinding.FragmentHoroscopeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HoroscopeFragment extends Fragment {
    public String countryUrl = Utils.cityUrl + "country";
    ArrayList<String> countryList = new ArrayList<>();
    ArrayAdapter<String> countryAdapter;
    FragmentHoroscopeBinding b;
    Context context;
    HoroscopeModel model;
    String countryName;
    String cityName;
    String hour;
    String minutes;
    String time;
    String timeStatus;
    String manglik;
    public String url = Utils.memberUrl + "horoscope/update/22";
    public String urlGetHoroscope = Utils.memberUrl + "horoscope/get/22";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HoroscopeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HoroscopeFragment newInstance(String param1, String param2) {
        HoroscopeFragment fragment = new HoroscopeFragment();
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
        // Inflate the layout for this fragment
        b = FragmentHoroscopeBinding.inflate(getLayoutInflater());
        context = getContext();

        setDropDownData();
        listener();
        getCountry(countryUrl);
        setData();
       // getData(urlGetHoroscope);
        return b.getRoot();
    }

//    private void getData(String urlGetHoroscope) {
//        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
//                urlGetHoroscope, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                progressDialog.dismiss();
//                Log.e("response", String.valueOf(response));
//                try {
//                    String code = response.getString("results");
//                    if (code.equalsIgnoreCase("1")) {
//                        Gson gson = new Gson();
//                        model = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
//                        setData();
//
//                    } else {
//                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                error.printStackTrace();
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
//    }

    private void setData() {
        if (model != null) {
            b.tvCountryOfBirth.setText(model.country_of_birth);
            b.tvCityofBirth.setText(model.city_of_birth);
            b.tvTimeofBirth.setText(model.hours + ":" + model.minutes + ":" + model.time + "," + model.time_status);
            b.tvManglik.setText(model.manglik);
        }
    }

    private void getCountry(String countryUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                countryUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));

                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("country");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("country_name");
                            Log.e("country", country);
                            countryList.add(country);
                            Log.e("Country-list", String.valueOf(countryList));
                        }
                    }
                    countryAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, countryList);
                    // set adapter
                    countryAdapter.notifyDataSetChanged();
                    b.acvCountry.setAdapter(countryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
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
    private void listener() {
        b.btnEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cvShowDetails.setVisibility(View.GONE);
                b.cvEditDetails.setVisibility(View.VISIBLE);
            }
        });


        b.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_1:
                        manglik = "yes";

                    case R.id.radio_button_2:
                        manglik = "no";

                    case R.id.radio_button_3:
                        manglik = "Don't Know";
                }
            }
        });

        b.btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.btnSaveDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkForm()) {
                            submitForm();

                        }

                    }
                });
            }
        });


        b.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cvShowDetails.setVisibility(View.VISIBLE);
                b.cvEditDetails.setVisibility(View.GONE);
            }
        });


    }

    private boolean checkForm() {
        countryName = b.tvCountryOfBirth.getText().toString().trim();
        cityName = b.tvCityofBirth.getText().toString().trim();
        hour = b.acvHour.getText().toString().trim();
        minutes = b.acvMinutes.getText().toString().trim();
        time = b.actvampm.getText().toString().trim();
        timeStatus = b.actvapprox.getText().toString().trim();

        Log.e("city name",cityName);


        if (countryName.isEmpty()) {
            b.tvCountryOfBirth.setError("country of birth mandatory");
            b.tvCountryOfBirth.setFocusableInTouchMode(true);
            b.tvCountryOfBirth.requestFocus();
            return false;
        } else {
            b.tvCountryOfBirth.setError(null);
        }

        if (cityName.isEmpty()) {
            b.tvCityofBirth.setError("city of birth mandatory");
            b.tvCityofBirth.setFocusableInTouchMode(true);
            b.tvCityofBirth.requestFocus();
            return false;
        } else {
            b.tvCityofBirth.setError(null);
        }
        return true;
    }

    private void submitForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("country_of_birth", countryName);
        params.put("city_of_birth", cityName);

        Log.e("city",cityName);
        params.put("time", time);
        params.put("time_status", timeStatus);
        params.put("hours", hour);
        params.put("minutes", minutes);
        params.put("manglik", manglik);
        Log.e("params", String.valueOf(params));
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {

                            if (response!=null) {
                                Gson gson = new Gson();
                                model = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
                                b.cvShowDetails.setVisibility(View.VISIBLE);
                                b.cvEditDetails.setVisibility(View.GONE);
                               // getData(urlGetHoroscope);
                                setData();
                                //    Gson gson = new Gson();
//                                     //  Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }

    private void setDropDownData() {
        String[] hour = getResources().getStringArray(R.array.Hour);
        ArrayAdapter aa = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, hour);
        //Setting the ArrayAdapter data on the Spinner
        b.acvHour.setAdapter(aa);


        String[] minutes = getResources().getStringArray(R.array.minutes);
        ArrayAdapter min = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, minutes);
        //Setting the ArrayAdapter data on the Spinner
        b.acvMinutes.setAdapter(min);

        String[] am = getResources().getStringArray(R.array.ampm);
        ArrayAdapter ampm = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, am);
        //Setting the ArrayAdapter data on the Spinner
        b.actvampm.setAdapter(ampm);

        String[] aprox = getResources().getStringArray(R.array.aprox);
        ArrayAdapter apr = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, aprox);
        //Setting the ArrayAdapter data on the Spinner
        b.actvapprox.setAdapter(apr);
    }
}