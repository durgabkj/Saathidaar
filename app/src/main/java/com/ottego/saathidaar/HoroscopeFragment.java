package com.ottego.saathidaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.databinding.FragmentHoroscopeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HoroscopeFragment extends Fragment {
    public String countryUrl = Utils.location + "country";
    ArrayList<String> countryList = new ArrayList<>();
    ArrayAdapter<String> countryAdapter;
    FragmentHoroscopeBinding b;
    Context context;
    HoroscopeModel model;
    SessionManager sessionManager;
    String countryName;
    String cityName;
    String hour;
    String minutes;
    String time;
    String timeStatus;
    String manglik;
    public String url = Utils.memberUrl + "horoscope/update/";
    public String urlGetHoroscope = Utils.memberUrl + "horoscope/get/";


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
        b = FragmentHoroscopeBinding.inflate(inflater,container,false);
        context = getContext();
        sessionManager = new SessionManager(context);
        setDropDownData();
        listener();
        getCountry();
        getData();


        return b.getRoot();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void setData1() {
        b.acvCountry.setText(model.country_of_birth);
        b.etHoroscopeBirthCity.setText(model.city_of_birth);
        b.acvHour.setText(model.hours);
        b.acvMinutes.setText(model.minutes);
        b.actvampm.setText(model.time);
        b.actvapprox.setText(model.time_status);

        if (model.manglik != null && model.manglik.equalsIgnoreCase("Yes")) {
            b.radioButton1.setChecked(true);
        } else if (model.manglik != null && model.manglik.equalsIgnoreCase("No")) {
            b.radioButton2.setChecked(true);
        } else if (model.manglik != null && model.manglik.equalsIgnoreCase("Don't Know")) {
            b.radioButton3.setChecked(true);
        }

    }

    private void getData() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                urlGetHoroscope + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
                        setData();
                        setData1();

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
                progressDialog.dismiss();
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


        b.btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.cvShowDetails.setVisibility(View.VISIBLE);
                b.cvEditDetails.setVisibility(View.GONE);
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
            public void onClick(View v) {
                if (checkForm()) {
                    submitForm();
                    hideKeyboard(v);
                }
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
        countryName = b.acvCountry.getText().toString().trim();
        cityName = b.etHoroscopeBirthCity.getText().toString().trim();
        hour = b.acvHour.getText().toString().trim();
        minutes = b.acvMinutes.getText().toString().trim();
        time = b.actvampm.getText().toString().trim();
        timeStatus = b.actvapprox.getText().toString().trim();

        Log.e("city name", cityName);


        if (countryName.isEmpty()) {
            b.tvc.setError("country of birth mandatory");
            b.tvc.setFocusableInTouchMode(true);
            b.tvc.requestFocus();
            return false;
        } else {
            b.tvc.setError(null);
        }

        if (cityName.isEmpty()) {
            b.tvc1.setError("city of birth mandatory");
            b.tvc1.setFocusableInTouchMode(true);
            b.tvc1.requestFocus();
            return false;
        } else {
            b.tvc1.setError(null);
        }
        return true;
    }

    private void submitForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("country_of_birth", countryName);
        params.put("city_of_birth", cityName);
        params.put("time", time);
        params.put("time_status", timeStatus);
        params.put("hours", hour);
        params.put("minutes", minutes);
        params.put("manglik", manglik);
        Log.e("params", String.valueOf(params));
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + sessionManager.getMemberId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {
                            if (response != null) {
                                Gson gson = new Gson();
                                model = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
                                sessionManager.createHoroscope(model);
                                b.cvShowDetails.setVisibility(View.VISIBLE);
                                b.cvEditDetails.setVisibility(View.GONE);
                                // getData(urlGetHoroscope);
                                 getData();
                                setData();
                                setData1();
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

    private void getCountry() {
        b.acvCountry.setItems(getCountryItems());
        b.acvCountry.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectionCleared() {
                Toast.makeText(getContext(), "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // dropDown With Search
    private List getCountryItems() {
        ArrayList<String> countryList = new ArrayList<>();
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
                            countryList.add(country);
                            Log.e("country-list", String.valueOf(countryList));
                        }
                    }

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

//            alphabetsList.add(Character.toString(i));
        return countryList;
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

    private void setData() {
        if (model != null) {
            b.tvCountryOfBirth.setText(model.country_of_birth);
            b.tvCityofBirth.setText(model.city_of_birth);
            b.tvTimeofBirth.setText(model.hours + ":" + model.minutes + ":" + model.time + "," + model.time_status);
            b.tvManglik.setText(model.manglik);
        }
    }
}