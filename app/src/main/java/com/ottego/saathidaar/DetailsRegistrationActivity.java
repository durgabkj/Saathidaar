package com.ottego.saathidaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.Model.DataModelReligion;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.UserModel;
import com.ottego.saathidaar.databinding.ActivityDetailsRegistrationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailsRegistrationActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";
    private static String age;
    public String countryUrl = Utils.location + "country";
    public String ReligionUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/religion-name";
    public String Updateurl = Utils.memberUrl + "short-registration/update/";
    ActivityDetailsRegistrationBinding b;
    ArrayList<String> countryList = new ArrayList<>();
    SessionManager sessionManager;
    Context context;
    UserModel userModel;
    ArrayList<String> AgeList = new ArrayList<String>();
    ArrayAdapter<String> minAdapter;
    DataModelReligion data;
    ArrayList<String> motherTongueList;
    Dialog dialog;
    String[] stringArray = new String[0];
    //For MaritalStatus....
    ArrayList<String> maritalList = new ArrayList<>();
    String date = "";
    String image = "";
    Calendar calendar = Calendar.getInstance();
    ArrayList<String> religionList = new ArrayList<>();
    ArrayAdapter<String> religionAdapter;
    ArrayList<String> communityList = new ArrayList<>();
    ArrayAdapter<String> communityAdapter;
    EditText editText;
    ListView listView;
    List<String> imagePathList = new ArrayList<>();
    String Dob = "";
    String Marital_status = "";
    String Height = "";
    String Diet = "";
    String MotherTongue = "";
    String Religion = "";
    String cast = "";
    String selectedReligion;
    String myMaritalS;
    MemberProfileModel model;
    String memberId;
    String phone;
    String country = "";
    private String format = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static int getPerfectAgeInYears(int year, int month, int date) {

        Calendar dobCalendar = Calendar.getInstance();

        dobCalendar.set(Calendar.YEAR, year);
        dobCalendar.set(Calendar.MONTH, month);
        dobCalendar.set(Calendar.DATE, date);

        int ageInteger = 0;

        Calendar today = Calendar.getInstance();

        ageInteger = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH)) {

            if (today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH)) {

                ageInteger = ageInteger - 1;
            }

        } else if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH)) {

            ageInteger = ageInteger - 1;

        }
        Log.e("Age", String.valueOf(ageInteger));
        age = String.valueOf(ageInteger);
        return ageInteger;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityDetailsRegistrationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = DetailsRegistrationActivity.this;
        sessionManager = new SessionManager(context);

        phone = getIntent().getStringExtra("mobile");
        memberId = sessionManager.getUserMemberRegId();
//
        Log.e("registered memberId", memberId);
        Log.e("user mobile", phone);

        // Initialize dialog
        dialog = new Dialog(context);
        // b.mbDatePicker.setText(currentDate);
        listener();
        maritalStatus();
        userHeight();
        getCountry(countryUrl);

        religionList();
        communityList();
        dietList();

    }

    private void getCountry(String countryUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                countryUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Log.e("response", String.valueOf(response));

                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("country");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("country_name");
                            //  Log.e("Country-list", String.valueOf(countryList));

                            countryList.add(country);
                            stringArray = new String[]{country};


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


    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void dietList() {

        final int[] checkedItem = {-1};
        b.etDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.food);

                // title of the alert dialog
                alertDialog.setTitle("Choose Life Style");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] dietGroup = getResources().getStringArray(R.array.DietGroupPersonal);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(dietGroup, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etDiet.setText(dietGroup[which]);

                        // when selected an item the dialog should be closed with the dismiss method
                        dialog.dismiss();
                    }
                });

                // set the negative button if the user
                // is not interested to select or change
                // already selected item
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create and build the AlertDialog instance
                // with the AlertDialog builder instance
                AlertDialog customAlertDialog = alertDialog.create();

                // show the alert dialog when the button is clicked
                customAlertDialog.show();
                Button buttonbackground = customAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setBackgroundColor(Color.BLACK);
            }
        });


    }

    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layout_dialog = LayoutInflater.from(context).inflate(R.layout.alert_sucess_dialog, null);
        builder.setView(layout_dialog);

        AppCompatButton btnokSuccess = layout_dialog.findViewById(R.id.btnokSuccess);
        // show dialog

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getWindow().setGravity(Gravity.CENTER);

        btnokSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, NavigationActivity.class);
                startActivity(intent);

            }
        });
    }

    private void listener() {
        b.etCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.etCountry.getText().toString().trim().equalsIgnoreCase("other")) {
                    b.etCountryNameShortReg.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        b.tvUserReligion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (b.tvUserReligion.getText().toString().trim().equalsIgnoreCase("christian")) {
                    b.tvUserCommunity.setText("Empty");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        b.etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // set custom dialog
                dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                dialog.getWindow().setLayout(800, 900);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, countryList);
                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        b.etCountry.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });
        b.btnSaveReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                if (checkForm()) {
                    submitForm();
                }

            }
        });
        b.mbDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker v, int year, int month, int day) {
                Calendar userAge = new GregorianCalendar(year, month, day);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                fmt.setCalendar(userAge);
                String dateFormatted = fmt.format(userAge.getTime());

                if (minAdultAge.before(userAge)) {
                    Toast.makeText(context, "Please Select Valid Date Of Birth", Toast.LENGTH_LONG).show();
                } else {
                    b.mbDatePicker.setText(dateFormatted);
                }

                getPerfectAgeInYears(year, month, day);
            }
        };
    }

    private boolean checkForm() {
        if (b.etCountry.getText().toString().trim().equalsIgnoreCase("other")) {
            country = b.etCountryNameShortReg.getText().toString().trim();
        } else {
            country = b.etCountry.getText().toString().trim();
        }
        Dob = b.mbDatePicker.getText().toString().trim();
        Marital_status = b.etMaritalStatus.getText().toString().trim();
        Height = b.etHeight.getText().toString().trim();
        Diet = b.etDiet.getText().toString().trim();
        Religion = b.tvUserReligion.getText().toString().trim();

        if (country.isEmpty()) {
            b.etCountry.setError("Please Select Country");
            b.etCountry.setFocusableInTouchMode(true);
            b.etCountry.requestFocus();
            return false;
        } else {
            b.etCountry.setError(null);

        }


        if (Marital_status.isEmpty()) {
            b.etMaritalStatus.setError("Please Select Marital Status");
            b.etMaritalStatus.setFocusableInTouchMode(true);
            b.etMaritalStatus.requestFocus();
            return false;
        } else {
            b.etMaritalStatus.setError(null);
        }

        if (Dob.isEmpty()) {
            b.mbDatePicker.setError("Please select Date of Birth");
            b.mbDatePicker.setFocusableInTouchMode(true);
            b.mbDatePicker.requestFocus();
            return false;
        } else {
            b.mbDatePicker.setError(null);
        }
        if (Height.isEmpty()) {
            b.etHeight.setError("Please select Height");
            b.etHeight.setFocusableInTouchMode(true);
            b.etHeight.requestFocus();
            return false;
        } else {
            b.etHeight.setError(null);
        }

        if (Religion.isEmpty()) {
            b.tvUserReligion.setError("Please Select Religion");
            b.tvUserReligion.setFocusableInTouchMode(true);
            b.tvUserReligion.requestFocus();
            return false;
        } else {
            b.tvUserReligion.setError(null);
        }
        if (Diet.isEmpty()) {
            b.etDiet.setError("Please select Diet");
            b.etDiet.setFocusableInTouchMode(true);
            b.etDiet.requestFocus();
            return false;
        } else {
            b.etDiet.setError(null);
        }

        return true;
    }

    private void submitForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date_of_birth", Dob);
        params.put("marital_status", Marital_status);
        params.put("height", Height);
        params.put("religion", Religion);
        params.put("country_name", country);
        params.put("lifestyles", Diet);
        params.put("age", age);
        Log.e("params", String.valueOf(params));
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Please wait....", false, false);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Updateurl + sessionManager.getUserMemberRegId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e(" short registration response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Intent intent = new Intent(context, OtpVerificationActivity.class);
                                intent.putExtra("mobile", phone);
                                startActivity(intent);
                            } else {
                                //  Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
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

    private void religionList() {
        b.tvUserReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                religionList.clear();
                getReligionList(ReligionUrl);

                // Initialize dialog
                dialog = new Dialog(context);
                    // set custom dialog
                    dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                dialog.getWindow().setLayout(800, 900);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                listView = dialog.findViewById(R.id.list_view);
                EditText editText = dialog.findViewById(R.id.edit_text);
                // Initialize array adapter
                // ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        religionAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        b.tvUserReligion.setText(religionAdapter.getItem(position));
                        Log.e("position", religionAdapter.getItem((int) id));
                        religionList.clear();

                        // communityData();

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });


            }
        });

    }

    private void communityData() {
        String selectedCommunity = b.tvUserReligion.getText().toString().trim();
        String url = "http://103.174.102.195:8080/saathidaar_backend/api/get/cast-name/by/religion_name/" + selectedCommunity;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cast");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String cast = jsonObject1.getString("cast_name");
//                                            Log.e("cast", cast);
                            communityList.add(cast);
                            //  Log.e("cast-list", String.valueOf(communityList));
                        }
                    }
                    communityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, communityList);
//                                    // set adapter
                    listView.setAdapter(communityAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    private void getReligionList(String url) {
        Log.e("url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ReligionUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("religion");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("religion_name");
                            religionList.add(religion);
                            //  Log.e("Religion-list", String.valueOf(religionList));
                        }
                    }
                    religionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, religionList);
                    // set adapter
                    listView.setAdapter(religionAdapter);
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

        // listView.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);
    }

    private void communityList() {
        b.tvUserCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                communityData();

                // Initialize dialog
                dialog = new Dialog(context);

                // set custom dialog
                dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                dialog.getWindow().setLayout(800, 900);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                listView = dialog.findViewById(R.id.list_view);
                EditText editText = dialog.findViewById(R.id.edit_text);
                // Initialize array adapter
                communityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, communityList);
                // set adapter
                listView.setAdapter(communityAdapter);

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        communityAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        b.tvUserCommunity.setText(communityAdapter.getItem(position));
                        // Log.e("position", communityAdapter.getItem((int) id));
                        communityList.clear();
                        dialog.dismiss();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private void userHeight() {
        final int[] checkedItem = {-1};
        b.etHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.height);

                // title of the alert dialog
                alertDialog.setTitle("Choose Height");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] fromHeight = getResources().getStringArray(R.array.Height);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(fromHeight, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etHeight.setText(fromHeight[which]);

                        // when selected an item the dialog should be closed with the dismiss method
                        dialog.dismiss();
                    }
                });

                // set the negative button if the user
                // is not interested to select or change
                // already selected item
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create and build the AlertDialog instance
                // with the AlertDialog builder instance
                AlertDialog customAlertDialog = alertDialog.create();

                // show the alert dialog when the button is clicked
                customAlertDialog.show();
                Button buttonbackground = customAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setBackgroundColor(Color.BLACK);
            }
        });
    }

    private void maritalStatus() {

        final int[] checkedItem = {-1};
        b.etMaritalStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.marital);

                // title of the alert dialog
                alertDialog.setTitle("Choose Marital Status");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] maritalstatus = getResources().getStringArray(R.array.MaritalStatusProfileUser);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(maritalstatus, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etMaritalStatus.setText(maritalstatus[which]);

                        // when selected an item the dialog should be closed with the dismiss method
                        dialog.dismiss();
                    }
                });

                // set the negative button if the user
                // is not interested to select or change
                // already selected item
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create and build the AlertDialog instance
                // with the AlertDialog builder instance
                AlertDialog customAlertDialog = alertDialog.create();

                // show the alert dialog when the button is clicked
                customAlertDialog.show();
                Button buttonbackground = customAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setBackgroundColor(Color.BLACK);

            }
        });
    }
}