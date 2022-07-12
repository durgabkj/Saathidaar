package com.ottego.saathidaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Fragment.PersonalInfoFragment;
import com.ottego.saathidaar.Model.DataModelReligion;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.SessionProfileDetailModel;
import com.ottego.saathidaar.databinding.ActivityProfileEditPersonalBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditPersonalActivity extends AppCompatActivity {
    // String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
// Permissions for accessing the storage
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";
    public String ReligionUrl = "http://192.168.1.38:9094/api/get/religion-name";
    public String Updateurl = Utils.memberUrl + "app/basic-lifestyles/update/";
    SessionManager sessionManager;
    ActivityProfileEditPersonalBinding b;
    Context context;
    ArrayList<String> AgeList = new ArrayList<String>();
    ArrayAdapter<String> minAdapter;
    DataModelReligion data;
    ArrayList<String> motherTongueList;
    Dialog dialog;
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
    private String format = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String gender = "";
    String email = "";
    String description = "";
    String age = "";
    String Dob = "";
    String Marital_status = "";
    String Height = "";
    String GrewUpIn = "";
    String bloodGroup = "";
    String Diet = "";
    String Location = "";
    String MotherTongue = "";
    String HealthDetail = "";
    String Religion = "";
    String cast = "";
    String subCast = "";
    String Gothram = "";
    String child = "";
    String selectedReligion;
    String myMaritalS;
    MemberProfileModel model;
    String memberId;
    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfileEditPersonalBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = ProfileEditPersonalActivity.this;
        sessionManager = new SessionManager(context);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, MemberProfileModel.class);

         memberId=sessionManager.getMemberId();

        Log.e("personal data", data);


        // Initialize dialog
        dialog = new Dialog(context);
        // b.mbDatePicker.setText(currentDate);
        listener();
        ageDropDown();
        maritalStatus();
        userHeight();
        bloodGroup();
        motherTongueSpinner();
        limitWord();
        HealthDetails();
        religionList();
        communityList();
        dietList();
        gender();
        setData();

    }

    private void setData() {
        if (model != null) {
            b.etAddUserDescription.setText(model.about_ourself);
            b.mbDatePicker.setText(model.date_of_birth);
            b.etAge.setText(model.age);
            b.etMaritalStatus.setText(model.marital_status);
            b.etHeight.setText(model.height);
            b.etBloodGroup.setText(model.blood_group);
            b.etDiet.setText(model.lifestyles);
            b.tvGender.setText(model.gender);
            b.tvMotherTongue.setText(model.mother_tounge);
            b.tvUserSubCommunity.setText(model.subcaste);
            b.tvUserCommunity.setText(model.caste_name);
            b.etAddUserNoOfChild.setText(model.no_of_children);
            b.tvUserReligion.setText(model.religion_name);
            b.etHealth.setText(model.health_info);
        }


        //   b.multiSelectionAge.setSelected(minAdapter.getPosition(model.age));
        //   b.tvEditMaritalStatus.setSelection(Integer.parseInt(model.marital_status));

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
                alertDialog.setTitle("Choose LifeStyle");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] dietGroup = getResources().getStringArray(R.array.DietGroup);
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

    private void gender() {

        final int[] checkedItem = {-1};
        b.etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.height);

                // title of the alert dialog
                alertDialog.setTitle("Choose Gender");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] dietGroup = getResources().getStringArray(R.array.gender);
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
                        b.etGender.setText(dietGroup[which]);

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

    private void listener() {
        b.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    submitForm();
                    hideKeyboard(v);
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
                SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
                fmt.setCalendar(userAge);
                String dateFormatted = fmt.format(userAge.getTime());

                if (minAdultAge.before(userAge)) {
                    Toast.makeText(context, "Please Select valid date", Toast.LENGTH_LONG).show();
                } else {
                    b.mbDatePicker.setText(dateFormatted);
                }
            }
        };

//        b.ivCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openImageChooser();
//            }
//        });

        b.tvUserReligion.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                selectedReligion = b.tvUserReligion.getText().toString().trim();
                if (selectedReligion.equalsIgnoreCase("Hindu")) {
                    b.llCast.setVisibility(View.VISIBLE);
                    b.llSubCast.setVisibility(View.VISIBLE);
                    b.llGotra.setVisibility(View.VISIBLE);
                }

                if (selectedReligion.equalsIgnoreCase("Muslim")) {
                    b.llCast.setVisibility(View.VISIBLE);
                    b.llSubCast.setVisibility(View.VISIBLE);
                    b.llGotra.setVisibility(View.GONE);
                }

                if (selectedReligion.equalsIgnoreCase("Parsi")) {
                    b.llCast.setVisibility(View.GONE);
                    b.llSubCast.setVisibility(View.GONE);
                    b.llGotra.setVisibility(View.GONE);
                }
                if (selectedReligion.equalsIgnoreCase("Jain")) {
                    b.llCast.setVisibility(View.VISIBLE);
                    b.llSubCast.setVisibility(View.VISIBLE);
                    b.llGotra.setVisibility(View.VISIBLE);
                }

                if (selectedReligion.equalsIgnoreCase("Buddhist")) {
                    b.llCast.setVisibility(View.GONE);
                    b.llSubCast.setVisibility(View.GONE);
                    b.llGotra.setVisibility(View.GONE);
                }

                if (selectedReligion.equalsIgnoreCase("Christian")) {
                    b.llCast.setVisibility(View.GONE);
                    b.llSubCast.setVisibility(View.GONE);
                    b.llGotra.setVisibility(View.GONE);
                }
            }
        });

        b.etMaritalStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String myMaritalS = b.etMaritalStatus.getText().toString().trim();

                if (myMaritalS.equalsIgnoreCase("Never Married")) {
                    b.llNoChild.setVisibility(View.GONE);
                }

                if (myMaritalS.equalsIgnoreCase("Divorce")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                }

                if (myMaritalS.equalsIgnoreCase("Widowed")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                }

                if (myMaritalS.equalsIgnoreCase("Awaiting Divorce")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                }

                if (myMaritalS.equalsIgnoreCase("Married")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean checkForm() {
//        name = b.etAddUserName.getText().toString().trim();
       gender = b.etGender.getText().toString().trim();
        description = b.etAddUserDescription.getText().toString().trim();
        Dob = b.mbDatePicker.getText().toString().trim();
        Marital_status = b.etMaritalStatus.getText().toString().trim();
        age = b.etAge.getText().toString().trim();
        child = b.etAddUserNoOfChild.getText().toString().trim();
        Height = b.etHeight.getText().toString().trim();
        GrewUpIn = b.etAddUserGrewUpIn.getText().toString().trim();
        bloodGroup = b.etBloodGroup.getText().toString().toString();
        Diet = b.etDiet.getText().toString().trim();
        Location = b.etAddUserLocation.getText().toString().trim();
        MotherTongue = b.tvMotherTongue.getText().toString().trim();
        HealthDetail = b.etHealth.getText().toString().trim();
        Religion = b.tvUserReligion.getText().toString().trim();
        cast = b.tvUserCommunity.getText().toString().trim();
        subCast = b.tvUserSubCommunity.getText().toString().trim();
        Gothram = b.tvUserGotra.getText().toString().trim();


        if (description.isEmpty()) {
            b.etAddUserDescription.setError("Please write about you");
            b.etAddUserDescription.setFocusableInTouchMode(true);
            b.etAddUserDescription.requestFocus();
            return false;
        } else {
            b.etAddUserDescription.setError(null);
        }


        if (Dob.isEmpty()) {
            b.mbDatePicker.setError("Please select Date of Birth");
            b.mbDatePicker.setFocusableInTouchMode(true);
            b.mbDatePicker.requestFocus();
            return false;
        } else {
            b.mbDatePicker.setError(null);
        }


//        if (age.equals("selected")) {
//            b.tvage.setError("please select age");
//            b.tvage.setFocusableInTouchMode(true);
//            b.tvage.requestFocus();
//            return false;
//        } else {
//            b.tvage.setError(null);
//        }
//
//        if (myMaritalS.equals("select")) {
//            b.tvmarital.setError("please select Marital Status");
//            b.tvmarital.setFocusableInTouchMode(true);
//            b.tvmarital.requestFocus();
//            return false;
//        } else {
//            b.tvmarital.setError(null);
//        }
//
//        if (Height.equals("select")) {
//            b.tvHeight.setError("please select Height");
//            b.tvHeight.setFocusableInTouchMode(true);
//            b.tvHeight.requestFocus();
//            return false;
//        } else {
//            b.tvHeight.setError(null);
//        }
//
//        if (bloodGroup.equals("select")) {
//            b.tvBlood.setError("please select blood group");
//            b.tvBlood.setFocusableInTouchMode(true);
//            b.tvBlood.requestFocus();
//            return false;
//        } else {
//            b.tvBlood.setError(null);
//        }


//        if (Diet.equals("select")) {
//            b.tvDiet.setError("please select Diet");
//            b.tvDiet.setFocusableInTouchMode(true);
//            b.tvDiet.requestFocus();
//            return false;
//        } else {
//            b.tvDiet.setError(null);
//        }


//        if (b.spUserHeight.getCount()==0){
//            Toast.makeText(getApplicationContext(),"spinner hasn't values",
//                    Toast.LENGTH_LONG).show();
//        }


//        if (GrewUpIn.isEmpty()) {
//            b.etAddUserGrewUpIn.setError("Please enter your Grew Location");
//            b.etAddUserGrewUpIn.setFocusableInTouchMode(true);
//            b.etAddUserGrewUpIn.requestFocus();
//            return false;
//        } else {
//            b.etAddUserGrewUpIn.setError(null);
//        }


//        if (Location.isEmpty()) {
//            b.etAddUserLocation.setError("Please enter your Grew Location");
//            b.etAddUserLocation.setFocusableInTouchMode(true);
//            b.etAddUserLocation.requestFocus();
//            return false;
//        } else {
//            b.etAddUserLocation.setError(null);
//        }


//        if (MotherTongue.isEmpty()) {
//            b.tvMotherTongue.setError("Please enter your Mother Tongue");
//            b.tvMotherTongue.setFocusableInTouchMode(true);
//            b.tvMotherTongue.requestFocus();
//            return false;
//        } else {
//            b.tvMotherTongue.setError(null);
//        }
//        if (HealthDetail.equals("select")) {
//            b.tvHealth.setError("please select Health Detail");
//            b.tvHealth.setFocusableInTouchMode(true);
//            b.tvHealth.requestFocus();
//            return false;
//        } else {
//            b.tvHealth.setError(null);
//        }
//
//        if (Religion.isEmpty()) {
//            b.tvUserReligion.setError("Please enter your Religion");
//            b.tvUserReligion.setFocusableInTouchMode(true);
//            b.tvUserReligion.requestFocus();
//            return false;
//        } else {
//            b.tvUserReligion.setError(null);
//        }

//        if (cast.isEmpty()) {
//            b.tvUserCommunity.setError("Please enter your Mother Tongue");
//            b.tvUserCommunity.setFocusableInTouchMode(true);
//            b.tvUserCommunity.requestFocus();
//            return false;
//        } else {
//            b.tvUserCommunity.setError(null);
//        }
//
//
//        if (subCast.isEmpty()) {
//            b.tvUserSubCommunity.setError("Please enter your Sub cast");
//            b.tvUserSubCommunity.setFocusableInTouchMode(true);
//            b.tvUserSubCommunity.requestFocus();
//            return false;
//        } else {
//            b.tvUserSubCommunity.setError(null);
//        }
//
//
//        if (Gothram.isEmpty()) {
//            b.tvUserGotra.setError("Please enter your Gotharm");
//            b.tvUserGotra.setFocusableInTouchMode(true);
//            b.tvUserGotra.requestFocus();
//            return false;
//        } else {
//            b.tvUserGotra.setError(null);
//        }
        return true;

    }

    private void submitForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("age", age);
        params.put("about_ourself", description);
        params.put("date_of_birth", Dob);
        params.put("marital_status", Marital_status);
        params.put("height", Height);
        params.put("blood_group", bloodGroup);
        params.put("mother_tounge", MotherTongue);
        params.put("health_info", HealthDetail);
        params.put("religion_name", Religion);
        params.put("caste_name", cast);
        params.put("sub_caste_name", subCast);
        params.put("gothra", Gothram);
        params.put("gender", gender);
        params.put("lifestyles", Diet);
        params.put("no_of_children", child);

        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Updateurl+memberId, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" update personal detail response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Gson gson = new Gson();
//                                SessionProfileDetailModel model = gson.fromJson(String.valueOf(response.getJSONObject("data")), SessionProfileDetailModel.class);
//                                sessionManager.CreateProfileSession(model);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                Intent intent=new Intent(context,HomeFragment.class);
//                                startActivity(intent);
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
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

    private void religionList() {
        b.tvUserReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                        communityData();

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void communityData() {
        String selectedCommunity = b.tvUserReligion.getText().toString().trim();
        String url = "http://192.168.1.36:9094/api/get/cast-name/by/religion_name/" + selectedCommunity;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cast");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String cast = jsonObject1.getString("cast_name");
//                                            Log.e("cast", cast);
                            communityList.add(cast);
                            Log.e("cast-list", String.valueOf(communityList));
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
                        Log.e("position", communityAdapter.getItem((int) id));
                        communityList.clear();
                        dialog.dismiss();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

            }
        });

    }

    private void HealthDetails() {
////        {
////            @Override
////            public boolean isEnabled(int position) {
////            if (position == 0) {
////                return false;
////            } else {
////                return true;
////            }
////        }
////        };
//        //Setting the ArrayAdapter data on the Spinner
//
//        b.spUserHealthDetail.setAdapter(healthAdapter);
////        b.spUserHealthDetail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////            @Override
////            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                // First item will be gray
////                if (parent.getItemAtPosition(position).equals("Select")) {
////                    onNothingSelected(parent);
////                    ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.gray_dark));
////                } else {
////                    ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.black));
////
////                }
////            }
////
////            @Override
////            public void onNothingSelected(AdapterView<?> parent) {
////            }
////        });

        final int[] checkedItem = {-1};
        b.etHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.height);

                // title of the alert dialog
                alertDialog.setTitle("Choose Health");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] healthList = getResources().getStringArray(R.array.Health);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(healthList, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etHealth.setText(healthList[which]);

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

    private void limitWord() {
        b.etAddUserDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                b.etAddUserDescription.length();

                String s = b.etAddUserDescription.getText().toString().trim();
                int num = s.length();
                b.tvCountWord.setText("" + (int) num);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void motherTongueSpinner() {
        // initialize array list
        motherTongueList = new ArrayList<>();
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


        b.tvMotherTongue.setOnClickListener(new View.OnClickListener() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, motherTongueList);
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
                        b.tvMotherTongue.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });


    }

    private void bloodGroup() {

        final int[] checkedItem = {-1};
        b.etBloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.blood);

                // title of the alert dialog
                alertDialog.setTitle("Choose BloodGroup");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] bloodGroup = getResources().getStringArray(R.array.BloodGroup);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(bloodGroup, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etBloodGroup.setText(bloodGroup[which]);

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


//        {
//            @Override
//            public boolean isEnabled(int position) {
//                if (position == 0) {
//                    return false;
//                } else {
//                    return true;
//                }
//            }
//        };
        //Setting the ArrayAdapter data on the Spinner

//        b.spUserBloodGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                // First item will be gray
//                if (parent.getItemAtPosition(position).equals("Select")) {
//                    onNothingSelected(parent);
//                    ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.gray_dark));
//                } else {
//                    ((TextView) view).setTextColor(ContextCompat.getColor(context, R.color.black));
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

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
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose MaritalStatus");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] maritalstatus = getResources().getStringArray(R.array.MaritalStatus);
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

    private void ageDropDown() {
        final int[] checkedItem = {-1};
        b.etAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_age);

                // title of the alert dialog
                alertDialog.setTitle("Choose age");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] age = getResources().getStringArray(R.array.AgeFrom);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(age, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etAge.setText(age[which]);

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