package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.gson.Gson;
import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.Model.PartnerPreferenceModel;
import com.ottego.saathidaar.Model.SearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PartnerPreferenceFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MultipleSelection tvMultipleCast, tvMultipleReligion, multi_SelectionProfessionArea, multi_SelectionCountry, multi_SelectionState, multi_SelectionMotherTongue, tvMultipleCity,multi_SelectionQualification, multi_SelectionWorkingWith;
    TextView tvMultipleMaritalStatus,tvPartnerPreferencesBtn;
    EditText etFromAgePartnerPreference, etToAgePartnerPreference, etfromHeightPartnerPreference, etToHeightPartnerPreference, etDietPreference, etProfilePreference,etIncomePartnerPreference;
    boolean[] selectedLanguage;
    SessionManager sessionManager;
    ChipGroup cpChild;
    RadioGroup rgManglikType;
    MaterialRadioButton mrbNoManglik,mrbOpenToAll,mrbOnlyManglik,mrbDontNoManglik;


    Context context;
    //For MaritalStatus....
    boolean[] selectedMaritalStatus;
    ArrayList<Integer> MaritalStatusList = new ArrayList<>();
    String[] MaritalStatusArray = {"Never Married", "Divorce", "Widowed", "Awaiting Divorce", "Married"};
    public String getPreference=Utils.memberUrl+"preference/get/";
    public String ReligionUrl = "http://192.168.1.40:9094/api/get/religion-name";
    public String countryUrl = "http://192.168.1.40:9094/api/get/country";
    public String castUrl = "http://192.168.1.40:9094/api/get/all/cast";
    public String stateUrl = "http://192.168.1.40:9094/api/get/state";
    public String cityUrl = "http://192.168.1.40:9094/api/get/all/city";
public String updatePreference=Utils.memberUrl+"preference/update/";
    // Initialize variables
    Spinner spMin, spMax, spFromHeight, spToHeight, UserAnnualIncome, tvEditDietPreference, tvEditProfile;

    ArrayList<String> AgeList = new ArrayList<>();
    ArrayList<String> minList = new ArrayList<>();
    ArrayList<String> maxList = new ArrayList<>();
    ArrayAdapter<String> minAdapter, maxAdapter;
PartnerPreferenceModel model;

    String fromAge = "";
    String toAge = "";
    String fromHeight = "";
    String toHeight = "";
    String maritalStatus = "";
    String motherTongue = "";
    String religion = "";
    String cast = "";
    String country = "";
    String state = "";
    String qualification = "";
    String workingWith = "";
    String professionalArea = "";
    String manglik;
    String diet;
    String createdBy;
    String city;
    String income;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartnerPreferenceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PartnerPreferenceFragment newInstance(String param1, String param2) {
        PartnerPreferenceFragment fragment = new PartnerPreferenceFragment();
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

        View view = inflater.inflate(R.layout.fragment_partner_preference, container, false);
        // assign variable
        context = getContext();
        sessionManager = new SessionManager(context);
        tvMultipleReligion = view.findViewById(R.id.tvMultipleReligion);
        etFromAgePartnerPreference = view.findViewById(R.id.etFromAgePartnerPreference);
        etfromHeightPartnerPreference = view.findViewById(R.id.etfromHeightPartnerPreference);
        etToAgePartnerPreference = view.findViewById(R.id.etToAgePartnerPreference);
        multi_SelectionCountry = view.findViewById(R.id.multi_SelectionCountry);
        multi_SelectionState = view.findViewById(R.id.multi_SelectionState);
        tvPartnerPreferencesBtn = view.findViewById(R.id.tvPartnerPreferencesBtn);
        etToHeightPartnerPreference = view.findViewById(R.id.etToHeightPartnerPreference);
        tvMultipleCast = view.findViewById(R.id.tvMultipleCaste);
        multi_SelectionMotherTongue = view.findViewById(R.id.multi_SelectionMotherTongue);
        multi_SelectionQualification = view.findViewById(R.id.multi_SelectionQualification);
        tvMultipleMaritalStatus = view.findViewById(R.id.tvMultipleMaritalStatus);
        multi_SelectionWorkingWith = view.findViewById(R.id.multi_SelectionWorkingWith);
        multi_SelectionProfessionArea = view.findViewById(R.id.multi_SelectionProfessionArea);
        spToHeight = view.findViewById(R.id.spToHeight);
        spFromHeight = view.findViewById(R.id.spFromHeight);
        spToHeight = view.findViewById(R.id.spToHeight);
        // assign variables
        spMin = view.findViewById(R.id.sp_min);
        spMax = view.findViewById(R.id.sp_max);
        UserAnnualIncome = view.findViewById(R.id.UserAnnualIncome);

        rgManglikType = view.findViewById(R.id.rgManglikType);
        tvMultipleCity=view.findViewById(R.id.tvMultipleCity);
        tvEditDietPreference = view.findViewById(R.id.tvEditDietPreference);
        etProfilePreference = view.findViewById(R.id.etProfilePreference);
        etDietPreference = view.findViewById(R.id.etDietPreference);
        tvEditProfile = view.findViewById(R.id.tvEditProfile);
        etIncomePartnerPreference=view.findViewById(R.id.etIncomePartnerPreference);

        mrbOpenToAll = view.findViewById(R.id.mrbOpenToAll);
        mrbOnlyManglik = view.findViewById(R.id.mrbOnlyManglik);
        mrbNoManglik =view.findViewById(R.id.mrbNoManglik);
        mrbDontNoManglik=view.findViewById(R.id.mrbDontNoManglik);


        multi_SelectionWorkingWith();
        multipleSelectionMotherTongue();
        multipleSelectionQualification();
        multipleMaritalStatusSelectionCheckBox();
        multipleReligionSelectionCheckBox();
        multi_SelectionmultiProfessionArea();
        incomeSelection();
        multipleCastSelectionCheckBox();
        multipleCountrySelectionCheckBox();
        multipleStateSelectionCheckBox();
        multipleCityCheckBox();
        Height();
        SpinnerAge();
        dietList();
        profileCreatedBy();
        listener();

        getData();


        return view;
    }

    private void profileCreatedBy() {
        String[] ProfileCreatedGroup = getResources().getStringArray(R.array.ProfileCreated);
        ArrayAdapter ProfileCreatedAdapter = new ArrayAdapter(context, R.layout.dropdown_item, ProfileCreatedGroup);
        //Setting the ArrayAdapter data on the Spinner
        tvEditProfile.setAdapter(ProfileCreatedAdapter);
    }

    private void dietList() {
        String[] dietGroup = getResources().getStringArray(R.array.DietGroup);
        ArrayAdapter diet = new ArrayAdapter(context, R.layout.dropdown_item, dietGroup);
        //Setting the ArrayAdapter data on the Spinner
        tvEditDietPreference.setAdapter(diet);
    }

    private void listener() {


        rgManglikType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mrbOpenToAll:
                        manglik = "All";
                        break;
                    case R.id.mrbOnlyManglik:
                        manglik = "Yes";
                        break;
                    case R.id.mrbDontNoManglik:
                        manglik = "No";
                        break;
                    case R.id.mrbNoManglik:
                        manglik = "Don't know";
                        break;
                }
            }
        });


        tvEditDietPreference.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myDiet = tvEditDietPreference.getSelectedItem().toString();

                if (myDiet.equals("select")) {

                } else {
                    etDietPreference.setText(myDiet);
                }
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tvEditProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myProfile = tvEditProfile.getSelectedItem().toString();

                if (myProfile.equals("select")) {

                } else {
                    etProfilePreference.setText(myProfile);
                }
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        UserAnnualIncome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myIncome = spToHeight.getSelectedItem().toString();

                if (myIncome.equals("select")) {

                } else {
                    etIncomePartnerPreference.setText(myIncome);
                }
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        tvPartnerPreferencesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });


        spToHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myToHeight = spToHeight.getSelectedItem().toString();

                if (myToHeight.equals("select")) {

                } else {
                    etToHeightPartnerPreference.setText(myToHeight);
                }
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spFromHeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String myFromHeight = spFromHeight.getSelectedItem().toString();

                if (myFromHeight.equals("select")) {

                } else {
                    etfromHeightPartnerPreference.setText(myFromHeight);
                }
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spMax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String temp1 = spMax.getSelectedItem().toString().trim();

                if (temp1.equalsIgnoreCase("19 Years")) {
                    etToAgePartnerPreference.setText("");
                } else {
                    etToAgePartnerPreference.setText(spMax.getSelectedItem().toString().trim());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void submitForm() {
        fromAge = etFromAgePartnerPreference.getText().toString().trim();
        toAge = etToAgePartnerPreference.getText().toString().trim();
        fromHeight = etfromHeightPartnerPreference.getText().toString().trim();
        toHeight = etToHeightPartnerPreference.getText().toString().trim();
        maritalStatus = tvMultipleMaritalStatus.getText().toString().trim();
        motherTongue = multi_SelectionMotherTongue.getText().toString().trim();
        religion = tvMultipleReligion.getText().toString().trim();
        cast = tvMultipleCast.getText().toString().trim();
        country = multi_SelectionCountry.getText().toString().trim();
        state = multi_SelectionState.getText().toString().trim();
        qualification = multi_SelectionQualification.getText().toString().trim();
        workingWith = multi_SelectionWorkingWith.getText().toString().trim();
        professionalArea = multi_SelectionProfessionArea.getText().toString().trim();
        createdBy = etProfilePreference.getText().toString().trim();
        diet = etDietPreference.getText().toString().trim();
city=tvMultipleCity.getText().toString().toString();
        income=etIncomePartnerPreference.getText().toString().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("partner_from_age", fromAge);
        params.put("partner_to_age", toAge);
        params.put("partner_from_height", fromHeight);
        params.put("partner_to_height", toHeight);
        params.put("partner_marital_status", maritalStatus);
        params.put("partner_mother_tongue", motherTongue);
        params.put("partner_cast", cast);
        params.put("partner_qualification", qualification);
        params.put("partner_working_with", workingWith);
        params.put("partner_professional_area", professionalArea);
        params.put("partner_religions", religion);
        params.put("partner_country", country);
        params.put("partner_state", state);
        params.put("partner_city", city);
        params.put("partner_manglik_all", manglik);
        params.put("partner_annual_income", income);
        params.put("partner_profile_created", createdBy);
        params.put("partner_lifestyles", diet);

//        params.put("search_marital_status", country);
//        params.put("search_mother_tongue",state);
//        params.put("search_cast", city);

        params.put("member_id", sessionManager.getMemberId());
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, updatePreference + sessionManager.getMemberId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (!code.equalsIgnoreCase("") && code != null) {
//                                    Gson gson = new Gson();
//                                    UserModel sessionModel = gson.fromJson(String.valueOf((response)), UserModel.class);
//                                   // sessionManager.createSUserDetails(sessionModel);
                                Toast.makeText(context, code, Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
                                //Intent intent = new Intent(context, OtpVerificationActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // startActivity(intent);
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
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }
    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getPreference+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), PartnerPreferenceModel.class);
                setData();
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

        tvMultipleMaritalStatus.setText(model.partner_marital_status);
        etFromAgePartnerPreference.setText(model.partner_from_age);
        etToAgePartnerPreference.setText(model.partner_to_age);
        tvMultipleCity.setText(model.partner_city);
        multi_SelectionState.setText(model.partner_state);
        etfromHeightPartnerPreference.setText(model.partner_from_height);
        tvMultipleCity.setText(model.partner_city);
        etToHeightPartnerPreference.setText(model.partner_to_height);
        multi_SelectionCountry.setText(model.partner_country);
        multi_SelectionMotherTongue.setText(model.partner_mother_tongue);
etProfilePreference.setText(model.partner_profile_created);
        multi_SelectionProfessionArea.setText(model.partner_professional_area);
        etIncomePartnerPreference.setText(model.partner_annual_income);
        etDietPreference.setText(model.partner_lifestyles);
        tvMultipleCity.setText(model.partner_city);
        etToHeightPartnerPreference.setText(model.partner_to_height);
        multi_SelectionCountry.setText(model.partner_country);
        multi_SelectionMotherTongue.setText(model.partner_mother_tongue);
multi_SelectionWorkingWith.setText(model.partner_working_with);
multi_SelectionQualification.setText(model.partner_qualification);
tvMultipleCast.setText(model.partner_cast);
tvMultipleReligion.setText(model.partner_religions);

        if (model.partner_manglik_all!=null && model.partner_manglik_all.equalsIgnoreCase("All")){
            mrbOpenToAll.setChecked(true);
        }else if (model.partner_manglik_all!=null && model.partner_manglik_all.equalsIgnoreCase("Yes")){
            mrbOnlyManglik.setChecked(true);
        }else if (model.partner_manglik_all!=null && model.partner_manglik_all.equalsIgnoreCase("No")) {
            mrbNoManglik.setChecked(true);
        }else if (model.partner_manglik_all!=null && model.partner_manglik_all.equalsIgnoreCase("Don't Know")) {
            mrbDontNoManglik.setChecked(true);
        }
    }

    private void multipleCountrySelectionCheckBox() {
        multi_SelectionCountry.setItems(getCountryItems());
        multi_SelectionCountry.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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

    private void multipleStateSelectionCheckBox() {
        multi_SelectionState.setItems(getStateItems());
        multi_SelectionState.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
    private List getStateItems() {
        ArrayList<String> stateList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                stateUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("state");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state = jsonObject1.getString("state_name");
                            stateList.add(state);
                            Log.e("state-list", String.valueOf(stateList));
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
        return stateList;
    }

    private void multipleCityCheckBox() {
        tvMultipleCity.setItems(getCityItems());
        tvMultipleCity.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
    private List getCityItems() {
        ArrayList<String> cityList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                cityUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("city");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String city = jsonObject1.getString("city_name");
                            cityList.add(city);
                            Log.e("city-list", String.valueOf(cityList));
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
        return cityList;
    }


    private void multipleCastSelectionCheckBox() {
        tvMultipleCast.setItems(getCastItems());
        tvMultipleCast.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
    private List getCastItems() {
        ArrayList<String> castList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                castUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cast");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("cast_name");
                            castList.add(religion);
                            Log.e("cast-list", String.valueOf(castList));
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
        return castList;
    }

    private void multipleReligionSelectionCheckBox() {
        tvMultipleReligion.setItems(getReligionItems());
        tvMultipleReligion.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
    private List getReligionItems() {
        ArrayList<String> religionList = new ArrayList<>();
//        for (char i = 'A'; i <= 'Z'; i++)
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ReligionUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("religion");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("religion_name");
                            religionList.add(religion);
                            Log.e("Religion-list", String.valueOf(religionList));
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
        return religionList;
    }

    private void incomeSelection() {
        String[] income = getResources().getStringArray(R.array.Income);
        ArrayAdapter incomeAdapter = new ArrayAdapter(getContext(), R.layout.dropdown_item, income);
        //Setting the ArrayAdapter data on the Spinner
        UserAnnualIncome.setAdapter(incomeAdapter);
    }

    private void Height() {
        String[] fromHeight = getResources().getStringArray(R.array.Height);
        ArrayAdapter aa = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, fromHeight);
        //Setting the ArrayAdapter data on the Spinner
        spFromHeight.setAdapter(aa);
        spToHeight.setAdapter(aa);
    }

    private void multi_SelectionmultiProfessionArea() {
        multi_SelectionProfessionArea.setItems(getProfessionAreaItems());
        multi_SelectionProfessionArea.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List getProfessionAreaItems() {
        ArrayList<String> ProfessionAreaList = new ArrayList<>();
        ProfessionAreaList.add("Banking Professional");
        ProfessionAreaList.add("Chartered Accountant");
        ProfessionAreaList.add("Company Secretary");
        ProfessionAreaList.add("Finance Professional");
        ProfessionAreaList.add("M.Eng (Hons)");
        ProfessionAreaList.add("Engineering Diploma");
        ProfessionAreaList.add("Investment Professional");
        ProfessionAreaList.add("Accounting Professional");
        ProfessionAreaList.add("Admin Professional");
        ProfessionAreaList.add("Actor");
        ProfessionAreaList.add("Advertising Professional");
        ProfessionAreaList.add("Entertainment Professional");
        ProfessionAreaList.add("Event Manager");
        ProfessionAreaList.add("Journalist");
        ProfessionAreaList.add("Media Professional");
        ProfessionAreaList.add("Public Relations Professional");
        ProfessionAreaList.add("Farming");
        ProfessionAreaList.add("Horticulturist");
        ProfessionAreaList.add("Agricultural Professional (Others)");
        ProfessionAreaList.add("Air Hostess / Flight Attendant");
        ProfessionAreaList.add("Pilot / Co-Pilot");
        ProfessionAreaList.add("Other Airline Professional");
        ProfessionAreaList.add("Architect");
        ProfessionAreaList.add("Interior Designer");
        ProfessionAreaList.add("Landscape Architect");
        ProfessionAreaList.add("Animator");
        ProfessionAreaList.add("Commercial Artist");
        ProfessionAreaList.add("Web / UX Designers");
        ProfessionAreaList.add("Artist (Others)");
        ProfessionAreaList.add("Beautician");
        ProfessionAreaList.add("Fashion Designer");
        ProfessionAreaList.add("Hairstylist");
        ProfessionAreaList.add("Jewellery Designer");
        ProfessionAreaList.add("Designer (Others)");
        ProfessionAreaList.add("Customer Support / BPO / KPO Professional");
        ProfessionAreaList.add("IAS / IRS / IES / IFS");
        ProfessionAreaList.add("Indian Police Services (IPS)");
        ProfessionAreaList.add("BCom (Hons)");
        ProfessionAreaList.add("PGD Finance");
        ProfessionAreaList.add("BCA");
        ProfessionAreaList.add("B.IT");
        ProfessionAreaList.add("BCS");
        ProfessionAreaList.add("BA Computer Science");
        ProfessionAreaList.add("Law Enforcement Employee (Others)");
        ProfessionAreaList.add("Airforce");
        ProfessionAreaList.add("Army");
        ProfessionAreaList.add("Navy");
        ProfessionAreaList.add("Defense Services (Others)");
        ProfessionAreaList.add("Lecturer");
        ProfessionAreaList.add("Professor");
        ProfessionAreaList.add("Research Assistant");
        ProfessionAreaList.add("Research Scholar");
        ProfessionAreaList.add("Teacher");
        ProfessionAreaList.add("Training Professional (Others)");
        ProfessionAreaList.add("Civil Engineer");
        ProfessionAreaList.add("Electronics / Telecom Engineer");
        ProfessionAreaList.add("Mechanical / Production Engineer");
        ProfessionAreaList.add("Non IT Engineer (Others)");
        ProfessionAreaList.add("Chef / Sommelier / Food Critic");
        ProfessionAreaList.add("Catering Professional");
        ProfessionAreaList.add("Hotel");
        ProfessionAreaList.add("Software Developer / Programmer");
        ProfessionAreaList.add("Software Consultant");
        ProfessionAreaList.add("Hardware");
        ProfessionAreaList.add("Software Professional (Others)");
        ProfessionAreaList.add("Lawyer");
        ProfessionAreaList.add("Legal Professional (Others)");
        ProfessionAreaList.add("Legal Assistant");
        ProfessionAreaList.add("Dentist");
        ProfessionAreaList.add("Doctor");
        ProfessionAreaList.add("Medical Transcriptionist");
        ProfessionAreaList.add("Nurse");
        ProfessionAreaList.add("Pharmacist");
        ProfessionAreaList.add("Physician Assistant");
        ProfessionAreaList.add("Physiotherapist / Occupational Therapist");
        ProfessionAreaList.add("Psychologist");
        ProfessionAreaList.add("Surgeon");
        ProfessionAreaList.add("Veterinary Doctor");
        ProfessionAreaList.add("Therapist (Others)");
        ProfessionAreaList.add("Medical / Healthcare Professional (Others)T");
        ProfessionAreaList.add("Merchant Naval Officer");
        ProfessionAreaList.add("Mariner");
        ProfessionAreaList.add("Marketing Professional");
        ProfessionAreaList.add("Sales Professional");
        ProfessionAreaList.add("Biologist / Botanist");
        ProfessionAreaList.add("Physicist");
        ProfessionAreaList.add("Science Professional (Others)");
        ProfessionAreaList.add("CxO / Chairman / Director / President");
        ProfessionAreaList.add("VP / AVP / GM / DGM");
        ProfessionAreaList.add("Sr. Manager / Manager");
        ProfessionAreaList.add("Consultant / Supervisor / Team Leads");
        ProfessionAreaList.add("Team Member / Staff");
        ProfessionAreaList.add("Agent / Broker / Trader / Contractor");
        ProfessionAreaList.add("Business Owner / Entrepreneur");
        ProfessionAreaList.add("Politician");
        ProfessionAreaList.add("Social Worker / Volunteer / NGO");
        ProfessionAreaList.add("Sportsman");
        ProfessionAreaList.add("Travel");
        ProfessionAreaList.add("Writer");
        ProfessionAreaList.add("Student");
        ProfessionAreaList.add("Retired");
        ProfessionAreaList.add("Not working");
        return ProfessionAreaList;
    }

    private void multi_SelectionWorkingWith() {
        multi_SelectionWorkingWith.setItems(getWorkingWithItems());
        multi_SelectionWorkingWith.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List getWorkingWithItems() {
        ArrayList<String> workingWithItemsList = new ArrayList<>();
        workingWithItemsList.add("Private Company");
        workingWithItemsList.add("Government / Public Sector");
        workingWithItemsList.add("Defense / Civil Services");
        workingWithItemsList.add("Business / Self Employed");
        workingWithItemsList.add("Not Working");
        workingWithItemsList.add("Engineering Diploma");
        return workingWithItemsList;
    }

    private void multipleSelectionQualification() {
        multi_SelectionQualification.setItems(getQualificationItems());
        multi_SelectionQualification.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List getQualificationItems() {
        ArrayList<String> qualificationList = new ArrayList<>();
        qualificationList.add("B.E / B.Tech");
        qualificationList.add("M.E / M.Tech");
        qualificationList.add("M.S Engineering");
        qualificationList.add("B.Eng (Hons)");
        qualificationList.add("M.Eng (Hons)");
        qualificationList.add("Engineering Diploma");
        qualificationList.add("AE");
        qualificationList.add("AET");
        qualificationList.add("High school");
        qualificationList.add("B.A");
        qualificationList.add("B.Ed");
        qualificationList.add("BJMC");
        qualificationList.add("BFA");
        qualificationList.add("B.Arch");
        qualificationList.add("B.Des");
        qualificationList.add("BMM");
        qualificationList.add("MFA");
        qualificationList.add("M.Ed");
        qualificationList.add("M.A");
        qualificationList.add("MSW");
        qualificationList.add("MJMC");
        qualificationList.add("M.Arch");
        qualificationList.add("M.Des");
        qualificationList.add("BA (Hons)");
        qualificationList.add("DFA");
        qualificationList.add("D.Ed");
        qualificationList.add("D.Arch");
        qualificationList.add("AA");
        qualificationList.add("AFA");
        qualificationList.add("Less than high school");
        qualificationList.add("B.Com");
        qualificationList.add("CA / CPA");
        qualificationList.add("CFA");
        qualificationList.add("CS");
        qualificationList.add("BSc / BFin");
        qualificationList.add("M.Com");
        qualificationList.add("MSc / MFin / MS");
        qualificationList.add("BCom (Hons)");
        qualificationList.add("PGD Finance");
        qualificationList.add("BCA");
        qualificationList.add("B.IT");
        qualificationList.add("BCS");
        qualificationList.add("BA Computer Science");
        qualificationList.add("MCA");
        qualificationList.add("PGDCA");
        qualificationList.add("IT Diploma");
        qualificationList.add("ADIT");
        qualificationList.add("B.Sc");
        qualificationList.add("M.Sc");
        qualificationList.add("BSc (Hons)");
        qualificationList.add("MBBS");
        qualificationList.add("BDS");
        qualificationList.add("BPT");
        qualificationList.add("BAMS");
        qualificationList.add("BHMS");
        qualificationList.add("B.Pharma");
        qualificationList.add("BVSc");
        qualificationList.add("BSN / BScN");
        qualificationList.add("MDS");
        qualificationList.add("MCh");
        qualificationList.add("M.D");
        qualificationList.add("M.S Medicine");
        qualificationList.add("MPT");
        qualificationList.add("DM");
        qualificationList.add("M.Pharma");
        qualificationList.add("MMVSc");
        qualificationList.add("MMed");
        qualificationList.add("PGD Medicine");
        qualificationList.add("BBA");
        qualificationList.add("BHM");
        qualificationList.add("BBM");
        qualificationList.add("MBA");
        qualificationList.add("PGDM");
        qualificationList.add("ABA");
        qualificationList.add("ADBus");
        qualificationList.add("BL / LLB");
        qualificationList.add("ML / LLM");
        qualificationList.add("LLB (Hons)");
        qualificationList.add("ALA");
        qualificationList.add("Ph.D");
        qualificationList.add("M.Phil");
        qualificationList.add("Bachelor");
        qualificationList.add("Master");
        qualificationList.add("Diploma");
        qualificationList.add("Honours");
        qualificationList.add("Doctorate");
        qualificationList.add("Associate");
        return qualificationList;
    }

    private void multipleSelectionMotherTongue() {
        multi_SelectionMotherTongue.setItems(getMotherTongueItems());
        multi_SelectionMotherTongue.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List getMotherTongueItems() {
        ArrayList<String> motherTongueList = new ArrayList<>();
//        for (char i = 'A'; i <= 'Z'; i++)
//            alphabetsList.add(Character.toString(i));
        motherTongueList.add("Hindi");
        motherTongueList.add("Marathi");
        motherTongueList.add("Punjabi");
        motherTongueList.add("Bengali");
        motherTongueList.add("Gujarati");
        motherTongueList.add("Telugu");
        motherTongueList.add("Urdu");
        motherTongueList.add("Kannada");
        motherTongueList.add("English");

        motherTongueList.add("Tamil");
        motherTongueList.add("Odia");
        motherTongueList.add("Marwari");
        motherTongueList.add("Arunachali");
        motherTongueList.add("Assamese");
        motherTongueList.add("Awadhi");
        motherTongueList.add("Bhojpuri");
        motherTongueList.add("Chattisgarhi");
        motherTongueList.add("Haryanavi");

        motherTongueList.add("Himachali/Pahari");
        motherTongueList.add("Kashmiri");
        motherTongueList.add("Malayalam");
        motherTongueList.add("Khandesi");
        motherTongueList.add("Manipuri");
        motherTongueList.add("Rajasthani");
        motherTongueList.add("Sanskrit");
        motherTongueList.add("Sindhi");
        motherTongueList.add("Other");


        return motherTongueList;
    }

    private void multipleMaritalStatusSelectionCheckBox() {


        // initialize selected language array
        selectedMaritalStatus = new boolean[MaritalStatusArray.length];

        tvMultipleMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set title
                builder.setTitle("Select Marital Status");

                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(MaritalStatusArray, selectedMaritalStatus, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            MaritalStatusList.add(i);
                            // Sort array list
                            Collections.sort(MaritalStatusList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            MaritalStatusList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < MaritalStatusList.size(); j++) {
                            // concat array value
                            stringBuilder.append(MaritalStatusArray[MaritalStatusList.get(j)]);
                            // check condition
                            if (j != MaritalStatusList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        tvMultipleMaritalStatus.setText(stringBuilder.toString());
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dismiss dialog
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // use for loop
                        for (int j = 0; j < selectedMaritalStatus.length; j++) {
                            // remove all selection
                            selectedMaritalStatus[j] = false;
                            // clear language list
                            MaritalStatusList.clear();
                            // clear text view value
                            tvMultipleMaritalStatus.setText("");
                        }
                    }
                });
                // show dialog
                builder.show();
            }
        });
    }


    private void SpinnerAge() {

        // use for loop
        for (int i = 18; i <= 70; i++) {
            // add values in price list
            AgeList.add(" " + i + " Years");
            // check condition
            if (i > 1) {
                // Not include first value  in max list
                maxList.add(i + " Years");
            }

            if (i != 70) {
                // Not include  last value in min list
                minList.add(i + " Years");
            }
        }

        // Initialize adapter
        minAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, minList);
        maxAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, maxList);

        // set adapter
        spMin.setAdapter(minAdapter);
        spMax.setAdapter(maxAdapter);

        // set item selected listener on min spinner
        spMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // clear max list
                maxList.clear();
                // use for loop
                for (int i = position + 1; i < AgeList.size(); i++) {
                    // add values in max list
                    maxList.add(AgeList.get(i));
                }
                String temp = spMin.getSelectedItem().toString().trim();
                if (temp.equalsIgnoreCase("18 Years")) {
                    etFromAgePartnerPreference.setText("");
                    etToAgePartnerPreference.setText("");
                } else {
                    etFromAgePartnerPreference.setText(spMin.getSelectedItem().toString().trim());
                    etToAgePartnerPreference.setText(spMax.getSelectedItem().toString().trim());
                }
                // notify adapter
                maxAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
