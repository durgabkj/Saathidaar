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
import android.location.Address;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hbisoft.pickit.PickiT;
import com.ottego.saathidaar.Model.CountryModel;
import com.ottego.saathidaar.Model.DataModelReligion;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.UserModel;
import com.ottego.saathidaar.databinding.ActivityProfileEditPersonalBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileEditPersonalActivity extends AppCompatActivity {
    // String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
// Permissions for accessing the storage
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";
    public String ReligionUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/religion-name";
    public String Updateurl = Utils.memberUrl + "app/basic-lifestyles/update/";
    SessionManager sessionManager;
    final Calendar myCalendar = Calendar.getInstance();
    public String countryUrl = Utils.location + "country";
    public String stateUrl = Utils.location + "state-name/by/country-name/";
    public String cityUrl = Utils.location + "city-name/by/state-name/";
    ActivityProfileEditPersonalBinding b;
    Context context;
    UserModel userModel;
    ArrayList<String> AgeList = new ArrayList<String>();
    ArrayAdapter<String> minAdapter;
    DataModelReligion data;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> stateList = new ArrayList<>();
    ArrayAdapter<String> stateAdapter;
    String countryName;
    ArrayList<String> motherTongueList;
    Dialog dialog;
    String country = "";
    String state = "";
    String city = "";
    String origin = "";
    String pinCode = "";

    String[] stringArray =new String[0];
    String[] stringArray1 =new String[0];
    String[] stringArray2 =new String[0];
    ArrayAdapter<String> countryAdapter;
    ArrayList<String> cityList = new ArrayList<>();
    ArrayAdapter<String> cityAdapter;
    String stateName;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int PICK_FILE_REQUEST = 1;
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
    String countryName1;
    String cityName;
    String hour;
    String minutes;
    String time;
    Address address;
    ProgressBar progressBar;
    String DOB;
    String timeStatus;
    DatePickerDialog datePickerDialog;
    String manglik = "";

    private String format = "";
    List<String> imagePathList = new ArrayList<>();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    PickiT pickiT;
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
        getCountry(countryUrl);
        getState();
        getCity();
        setDropDownData();


        countryName=b.etCountry.getText().toString().trim();
        stateList();
        Log.e("ngDigital",countryName);
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
    private void getCity() {
        b.etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stateName = b.etState.getText().toString().trim();
                cityList.clear();
                cityList(cityUrl);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void cityList(String cityUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                cityUrl + stateName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cities");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String city = jsonObject1.getString("city_name");
                            cityList.add(city);
                            //Log.e("city-list", String.valueOf(cityList));
                            stringArray2 = cityList.toArray(new String[cityList.size()]);
                        }
                    }
//                    cityAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, cityList);
//                    // set adapter
//                    cityAdapter.notifyDataSetChanged();
//                    b.etAddUserResidenceStatus.setAdapter(cityAdapter);
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
        b.etCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countryName = b.etCountry.getText().toString().trim();
                stateList.clear();
                stateList();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void stateList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                stateUrl + countryName, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("states");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state = jsonObject1.getString("state_name");
                            stateList.add(state);
                            // Log.e("state-list Professional", String.valueOf(state));
                            //   stringArray1 = new String[]{state};
                            stringArray1 = stateList.toArray(new String[stateList.size()]);
                        }
                    }
//                    stateAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, stateList);
//                    // set adapter
//                    stateAdapter.notifyDataSetChanged();
//
//                    b.etAddUserStateOfResidence.setAdapter(stateAdapter);
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

    private void setData() {
        if (model != null) {
            b.etAddUserDescription.setText(model.about_ourself);
            b.mbDatePicker.setText(model.date_of_birth);
            b.etAge.setText(model.age);
            b.etMaritalStatus.setText(model.marital_status);
            b.etHeight.setText(model.height);
            b.etBloodGroup.setText(model.blood_group);
            b.etDiet.setText(model.lifestyles);
            b.etGender.setText(new StringBuilder().append(model.gender.substring(0, 1).toUpperCase()).append(model.gender.substring(1)).toString());
            b.tvMotherTongue.setText(model.mother_tounge);
            b.tvUserSubCommunity.setText(model.subcaste);
            b.tvUserCommunity.setText(model.caste_name);
            b.etAddUserNoOfChild.setText(model.no_of_children);
            b.tvUserReligion.setText(model.religion_name);


            b.etCountry.setText(model.country_name);
            b.etState.setText(model.state);
            b.etCity.setText(model.city);
            b.etAddUserCorigin.setText(model.ethnic_corigin);
            b.etAddUserZipPinCode.setText(model.pincode);

           // b.tvUserOtherHealthDetails.setText(model.health_info);

            b.etHealth.setText(model.health_info);
            b.tvUserGotra.setText(model.gothra);
            b.tvUserSubCommunity.setText(model.sub_caste_name);


            if (model.profile_photo != null && !model.profile_photo.isEmpty()) {
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .into(b.profilePicEdit);
            } else {
                if (sessionManager.getUserGender().equalsIgnoreCase("male")) {
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__male_)
                            .into(b.profilePicEdit);

                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__female_)
                            .into(b.profilePicEdit);

                }
            }


            b.acvCountry.setText(model.country_of_birth);
            b.etHoroscopeBirthCity.setText(model.city_of_birth);
            b.acvHour.setText(model.hours);
            b.acvMinutes.setText(model.minutes);
            b.actvampm.setText(model.time);
            b.actvapprox.setText(model.time_status);
            b.etHoroscopeBirthDOB.setText(model.date_of_birth);

            if (model.manglik != null && model.manglik.equalsIgnoreCase("Yes")) {
                b.radioButton1.setChecked(true);
            } else if (model.manglik != null && model.manglik.equalsIgnoreCase("No")) {
                b.radioButton2.setChecked(true);
            } else if (model.manglik != null && model.manglik.equalsIgnoreCase("Don't Know")) {
                b.radioButton3.setChecked(true);
            }
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
    public void successDialog()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View layout_dialog= LayoutInflater.from(context).inflate(R.layout.alert_sucess_dialog,null);
        builder.setView(layout_dialog);

        TextView btnokSuccess =layout_dialog.findViewById(R.id.btnokSuccess);
        // show dialog

        AlertDialog dialog=builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getWindow().setGravity(Gravity.CENTER);

        btnokSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent=new Intent(context,PersonalInfoShowActivity.class);
                startActivity(intent);

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
                alertDialog.setIcon(R.drawable.ic_baseline_people_24);

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
        b.acvCountry.setOnClickListener(new View.OnClickListener() {
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
                        b.acvCountry.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);

                if (minAdultAge.before(myCalendar)) {
                    Toast.makeText(context, "Age should be 18 or above", Toast.LENGTH_LONG).show();
                } else {
                    updateLabel();
                }

            }

        };
        b.etHoroscopeBirthDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = new DatePickerDialog(context, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);

                datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);

            }
        });
        b.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_1:
                        manglik = "yes";
                        break;
                    case R.id.radio_button_2:
                        manglik = "no";
                        break;
                    case R.id.radio_button_3:
                        manglik = "Don't Know";
                }
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
                        b.acvCountry.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

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
        b.etState.setOnClickListener(new View.OnClickListener() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, stateList);
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
                        b.etState.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });
        b.etCity.setOnClickListener(new View.OnClickListener() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, cityList);
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
                        b.etCity.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });
        b.etHealth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String health = b.etHealth.getText().toString().trim();
                if (health.equalsIgnoreCase("Other")) {
                    b.tvUserOtherHealthDetails.setVisibility(View.VISIBLE);
                    // b.etHealth.setText(b.tvUserOtherHealthDetails.getText().toString().trim());
                } else {
                    b.tvUserOtherHealthDetails.setVisibility(View.GONE);
                }
                //
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
                fmt.setCalendar(userAge);
                String dateFormatted = fmt.format(userAge.getTime());

                if (minAdultAge.before(userAge)) {
                    Toast.makeText(context, "Age should be 18 or above", Toast.LENGTH_LONG).show();
                } else {
                    b.mbDatePicker.setText(dateFormatted);
                }
            }
        };
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

                if (myMaritalS.equalsIgnoreCase("Divorced")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                    b.tvmarital.setText(" ");
                }

                if (myMaritalS.equalsIgnoreCase("Widowed")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                    b.tvmarital.setText("");
                }

                if (myMaritalS.equalsIgnoreCase("Awaiting Divorce")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                    b.tvmarital.setText("");
                }

                if (myMaritalS.equalsIgnoreCase("Married")) {
                    b.llNoChild.setVisibility(View.VISIBLE);
                    b.tvmarital.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd/mm/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        b.etHoroscopeBirthDOB.setText(dateFormat.format(myCalendar.getTime()));

    }
    private void setDropDownData() {

        final int[] checkedItem1 = {-1};
        b.acvHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_watch_later_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose Hour");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] Hour = getResources().getStringArray(R.array.Hour);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(Hour, checkedItem1[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem1[0] = which;

                        // now also update the TextView which previews the selected item
                        b.acvHour.setText(Hour[which]);

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


        final int[] checkedItem = {-1};
        b.acvMinutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_watch_later_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose Minutes");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] minutes = getResources().getStringArray(R.array.minutes);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(minutes, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.acvMinutes.setText(minutes[which]);

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

        final int[] checkedItem2 = {-1};
        b.actvampm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_watch_later_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose A.M/P.M");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] ampm = getResources().getStringArray(R.array.ampm);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(ampm, checkedItem2[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem2[0] = which;

                        // now also update the TextView which previews the selected item
                        b.actvampm.setText(ampm[which]);

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


        final int[] checkedItem3 = {-1};
        b.actvapprox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_watch_later_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose Timing");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] approx = getResources().getStringArray(R.array.aprox);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(approx, checkedItem3[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem3[0] = which;

                        // now also update the TextView which previews the selected item
                        b.actvapprox.setText(approx[which]);

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
    private boolean checkForm() {
//        name = b.etAddUserName.getText().toString().trim();

        description = b.etAddUserDescription.getText().toString().trim();
        Dob = b.mbDatePicker.getText().toString().trim();
        Marital_status = b.etMaritalStatus.getText().toString().trim();
        age = b.etAge.getText().toString().trim();
        child = b.etAddUserNoOfChild.getText().toString().trim();
        Height = b.etHeight.getText().toString().trim();
        GrewUpIn = b.etAddUserGrewUpIn.getText().toString().trim();
        bloodGroup = b.etBloodGroup.getText().toString().toString();
        Diet = b.etDiet.getText().toString().trim();
        gender = b.etGender.getText().toString().trim().toLowerCase();
        Location = b.etAddUserLocation.getText().toString().trim();
        MotherTongue = b.tvMotherTongue.getText().toString().trim();

        countryName1 = b.acvCountry.getText().toString().trim();
        cityName = b.etHoroscopeBirthCity.getText().toString().trim();
        DOB = b.etHoroscopeBirthDOB.getText().toString().trim();
        hour = b.acvHour.getText().toString().trim();
        minutes = b.acvMinutes.getText().toString().trim();
        time = b.actvampm.getText().toString().trim();
        timeStatus = b.actvapprox.getText().toString().trim();

        Religion = b.tvUserReligion.getText().toString().trim();
        cast = b.tvUserCommunity.getText().toString().trim();
        subCast = b.tvUserSubCommunity.getText().toString().trim();
        Gothram = b.tvUserGotra.getText().toString().trim();

        country = b.etCountry.getText().toString().trim();
        state = b.etState.getText().toString().trim();
        city = b.etCity.getText().toString().trim();
        origin = b.etAddUserCorigin.getText().toString().trim();
        pinCode = b.etAddUserZipPinCode.getText().toString().trim();

        if(b.etHealth.getText().toString().trim().equalsIgnoreCase("other"))
        {
            HealthDetail=b.tvUserOtherHealthDetails.getText().toString().trim();
            if (HealthDetail.isEmpty()) {
                b.tvUserOtherHealthDetails.setError("Write Health Issue");
                b.tvUserOtherHealthDetails.setFocusableInTouchMode(true);
                b.tvUserOtherHealthDetails.requestFocus();
                return false;
            } else {
                b.tvUserOtherHealthDetails.setError(null);
            }
        }else
        {
            HealthDetail = b.etHealth.getText().toString().trim();
        }


        if (description.isEmpty()) {
            b.etAddUserDescription.setError("Please write about you");
            b.etAddUserDescription.setFocusableInTouchMode(true);
            b.etAddUserDescription.requestFocus();
            return false;
        } else {
            b.etAddUserDescription.setError(null);
        }


        if (Dob.isEmpty()) {
            b.mbDatePicker.setError("Please Select Date of Birth");
            b.mbDatePicker.setFocusableInTouchMode(true);
            b.mbDatePicker.requestFocus();
            return false;
        } else {
            b.mbDatePicker.setError(null);
        }

        if (countryName1.isEmpty()) {
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
        params.put("pincode", pinCode);
        params.put("city_name", city);
        params.put("state_name", state);
        params.put("ethnic_corigin", origin);
        params.put("country_name", country);

        params.put("country_of_birth", countryName1);
        params.put("city_of_birth", cityName);
        params.put("date_of_birth", DOB);
        params.put("time", time);
        params.put("time_status", timeStatus);
        params.put("hours", hour);
        params.put("minutes", minutes);
        params.put("manglik", manglik);

        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Updateurl+memberId, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" update personal detail response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equals("1")) {
                                Gson gson = new Gson();
                                sessionManager.createSessionDescription(description);
                                sessionManager.createSessionGender(gender);
                                successDialog();
                               // Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                // sessionManager.createSessionLogin(userId);
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

    private void HealthDetails() {
        final int[] checkedItem = {-1};
        b.etHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.healthcare);

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