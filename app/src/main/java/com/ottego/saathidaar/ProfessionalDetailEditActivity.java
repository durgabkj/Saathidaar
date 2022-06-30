package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.ActivityProfessionalDetailEditBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfessionalDetailEditActivity extends AppCompatActivity {
    ActivityProfessionalDetailEditBinding b;
    Context context;
    MemberProfileModel model;
    String higherEducation = "";
    String collegeAttends = "";
    String income = "";
    String workingWith = "";
    String workingAs = "";
    String country = "";
    String state = "";
    String city = "";
    String origin = "";
    String pinCode = "";
    String mobile = "";
    String relationWith = "";
    String callTime = "";
    String displatOption = "";
    public String url = Utils.memberUrl + "app/basic-lifestyles/update/";
    public String countryUrl = Utils.cityUrl + "country";
    public String stateUrl = Utils.cityUrl + "state-name/by/country-name/";
    public String cityUrl = Utils.cityUrl + "city-name/by/state-name/";
    ArrayList<String> countryList = new ArrayList<>();
    ArrayAdapter<String> countryAdapter;
    ArrayList<String> stateList = new ArrayList<>();
    ArrayAdapter<String> stateAdapter;
    String countryName;
    SessionManager sessionManager;

    ArrayList<String> cityList = new ArrayList<>();
    ArrayAdapter<String> cityAdapter;
    String stateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfessionalDetailEditBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = ProfessionalDetailEditActivity.this;
        sessionManager = new SessionManager(context);

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, MemberProfileModel.class);
        userAnnualIncome();
        userWorkAs();
        UserWorkingWith();
        listener();
        getCountry(countryUrl);
        getState();
        getCity();
        setData();


    }

    private void setData() {
        b.etAddUserEducation.setText(model.education);
        b.etAddUserCollegeAttended.setText(model.college_attended);
        b.etIncome.setText(model.income);
        b.etAddUserCollegeAttended.setText(model.college_attended);
        b.etWorkingWith.setText(model.working_with);
        b.etWorkingAs.setText(model.working_as);

        b.etCountry.setText(model.country_name);
        b.etState.setText(model.state_name);
        b.etCity.setText(model.city);
        b.etAddUserCorigin.setText(model.ethnic_corigin);
        b.etAddUserZipPinCode.setText(model.pincode);
    }




    private void getCity() {
        b.etAddUserStateOfResidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateName = b.etAddUserStateOfResidence.getSelectedItem().toString().trim();
                cityList.clear();
                cityList(cityUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void cityList(String cityUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                cityUrl + stateName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cities");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String city = jsonObject1.getString("city_name");
                            Log.e("city", city);
                            cityList.add(city);
                            Log.e("city-list", String.valueOf(cityList));
                        }
                    }
                    cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, cityList);
                    // set adapter
                    cityAdapter.notifyDataSetChanged();
                    b.etAddUserResidenceStatus.setAdapter(cityAdapter);
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

    private void getState() {
        b.etAddUserCurrentResidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                countryName = b.etAddUserCurrentResidence.getSelectedItem().toString().trim();
                stateList.clear();
                stateList(stateUrl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void stateList(String stateUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                stateUrl + countryName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("states");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state = jsonObject1.getString("state_name");
                            Log.e("state", state);
                            stateList.add(state);
                            Log.e("state-list", String.valueOf(stateList));
                        }
                    }
                    stateAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, stateList);
                    // set adapter
                    stateAdapter.notifyDataSetChanged();

                    b.etAddUserStateOfResidence.setAdapter(stateAdapter);
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
                            countryList.add("select");
                            countryList.add(country);
                            Log.e("Country-list", String.valueOf(countryList));
                        }
                    }
                    countryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, countryList);
                    // set adapter
                    countryAdapter.notifyDataSetChanged();
                    b.etAddUserCurrentResidence.setAdapter(countryAdapter);

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

    private void userAnnualIncome() {
        String[] income = getResources().getStringArray(R.array.Income);
        ArrayAdapter incomeAdapter = new ArrayAdapter(context, R.layout.dropdown_item, income);
        //Setting the ArrayAdapter data on the Spinner
        b.UserAnnualIncome.setAdapter(incomeAdapter);
    }

    private void userWorkAs() {
        ArrayList<String> ProfessionAreaList = new ArrayList<>();
        ProfessionAreaList.add("Select");
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

        ArrayAdapter workingAsAdapter = new ArrayAdapter(context, R.layout.dropdown_item, ProfessionAreaList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserWorkingAs.setAdapter(workingAsAdapter);
    }

    private void UserWorkingWith() {
        ArrayList<String> workingWithItemsList = new ArrayList<>();
        workingWithItemsList.add("Select");
        workingWithItemsList.add("Private Company");
        workingWithItemsList.add("Government / Public Sector");
        workingWithItemsList.add("Defense / Civil Services");
        workingWithItemsList.add("Business / Self Employed");
        workingWithItemsList.add("Not Working");
        workingWithItemsList.add("Engineering Diploma");

        ArrayAdapter workingWithAdapter = new ArrayAdapter(context, R.layout.dropdown_item, workingWithItemsList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserWorkingWith.setAdapter(workingWithAdapter);
    }

    private void listener() {
        b.btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });



        b.etAddUserCurrentResidence.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String country=b.etAddUserCurrentResidence.getSelectedItem().toString().trim();
                if(country.equalsIgnoreCase("select")){

                }else
                {
                    b.etCountry.setText(country);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void submitForm() {
        higherEducation = b.etAddUserEducation.getText().toString().trim();
        collegeAttends = b.etAddUserCollegeAttended.getText().toString().trim();
        income = b.UserAnnualIncome.getSelectedItem().toString().trim();
        workingWith = b.UserWorkingWith.getSelectedItem().toString().trim();
        workingAs = b.UserWorkingAs.getSelectedItem().toString().trim();
        country = b.etAddUserCurrentResidence.getSelectedItem().toString().trim();
        state = b.etAddUserStateOfResidence.getSelectedItem().toString().trim();
        city = b.etAddUserResidenceStatus.getSelectedItem().toString().trim();
        origin = b.etAddUserCorigin.getText().toString().trim();
        pinCode = b.etAddUserZipPinCode.getText().toString().trim();
//        mobile = b.etAddUserMobile.getText().toString().trim();
//        relationWith = b.etAddUserRelationship.getText().toString().trim();
//        callTime = b.etAddUserConvenientCall.getText().toString().trim();
//        displatOption = b.etAddUserDisplayOption.getText().toString().trim();

        Map<String, String> params = new HashMap<String, String>();
        params.put("highest_qualification", higherEducation);
        params.put("college_attended", collegeAttends);
        params.put("working_with", workingWith);
        params.put("working_as", workingAs);
        params.put("annual_income", income);
        params.put("pincode", pinCode);
        params.put("city_name", country);
        params.put("state_name", state);
        params.put("ethnic_corigin", origin);
        params.put("country_name", country);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url+sessionManager.getMemberId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {

                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
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
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }
}


