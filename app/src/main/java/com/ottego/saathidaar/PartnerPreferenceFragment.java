package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.gson.Gson;
import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.Model.DataModelCountry;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.PartnerPreferenceModel;

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
    public String getPreference = Utils.memberUrl + "preference/get/";
    public String ReligionUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/religion-name";
    public String countryUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/country";
    public String castUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/all/cast";
    public String stateUrl = Utils.location + "multiple/state";
    public String cityUrl = Utils.location + "city-name/by/state-name/";
    public String updatePreference = Utils.memberUrl + "preference/update/";
    MultipleSelection tvMultipleCast, tvMultipleReligion, multi_SelectionProfessionArea, multi_SelectionCountry, multi_SelectionState, multi_SelectionMotherTongue, tvMultipleCity, multi_SelectionQualification, multi_SelectionWorkingWith;
    TextView etFromAgePartnerPreference, tvSearchButton, etToAgePartnerPreference, tvMultipleMaritalStatus, tvPartnerPreferencesBtn, etIncomePartnerPreference, etProfilePreference, etDietPreference, tvProfileCreated;
    EditText etfromHeightPartnerPreference, etToHeightPartnerPreference, etProfileSearch, etPreferenceCountry, etPreferenceState, etPreferenceCity;
    boolean[] selectedLanguage;
    SessionManager sessionManager;
    ChipGroup cpChild;
    RadioGroup rgManglikType;
    MaterialRadioButton mrbNoManglik, mrbOpenToAll, mrbOnlyManglik, mrbDontNoManglik;
    String id;
    String countryName;
    String cityName;
    MemberProfileModel data;
    Context context;
    //For MaritalStatus....
    boolean[] selectedMaritalStatus;
    boolean[] selectedDeit;
    ArrayList<Integer> MaritalStatusList = new ArrayList<>();
    ArrayList<Integer> DietList = new ArrayList<>();
    String[] MaritalStatusArray = {"Open to all", "Never Married", "Divorce", "Widowed", "Awaiting Divorce"};
    String[] DietStatusArray = {"Open to all", "Veg", "Non-Veg", "Jain", "Vegan", "Eggetarian", "Occasinonally Non-veg"};
    PartnerPreferenceModel model;
    DataModelCountry countryModel;
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
    int[] state_arr = new int[10];
    int myCounter = 0;
    int countryId;
    MultiSpinnerSearch multiSelectSpinnerWithSearch;

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
        rgManglikType = view.findViewById(R.id.rgManglikType);
        tvMultipleCity = view.findViewById(R.id.tvMultipleCity);
//        tvEditDietPreference = view.findViewById(R.id.etDietPreference);
        etProfilePreference = view.findViewById(R.id.etProfilePreference);
        etDietPreference = view.findViewById(R.id.etDietPreference);
        etIncomePartnerPreference = view.findViewById(R.id.etIncomePartnerPreference);
        mrbOpenToAll = view.findViewById(R.id.mrbOpenToAll);
        mrbOnlyManglik = view.findViewById(R.id.mrbOnlyManglik);
        mrbNoManglik = view.findViewById(R.id.mrbNoManglik);
        mrbDontNoManglik = view.findViewById(R.id.mrbDontNoManglik);
        etProfileSearch = view.findViewById(R.id.etProfileSearch);
        tvSearchButton = view.findViewById(R.id.tvSearchButton);


        etPreferenceCountry = view.findViewById(R.id.etPreferenceCountry);
        etPreferenceState = view.findViewById(R.id.etPreferenceState);
        etPreferenceCity = view.findViewById(R.id.etPreferenceCity);




        multi_SelectionWorkingWith();
        multipleSelectionMotherTongue();
        multipleSelectionQualification();
        multipleMaritalStatusSelectionCheckBox();
        multipleReligionSelectionCheckBox();
        multi_SelectionmultiProfessionArea();
        incomeSelection();
        multipleCastSelectionCheckBox();
        getCountryItems();


        Height();
        SpinnerAge();
        dietList();
        profileCreatedBy();
        listener();

        if (sessionManager.getUserGender().equalsIgnoreCase("male")) {
            etProfileSearch.setHint("E.g:-FSD001");
        } else {
            etProfileSearch.setHint("E.g:-MSD001");
        }
        getData();
//        getStateItems();
        return view;
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void Height() {

        final int[] checkedItem = {-1};
        final int[] checkedItem1 = {-1};
        etfromHeightPartnerPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.height);

                // title of the alert dialog
                alertDialog.setTitle("Choose your Height");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] fromHeight = getResources().getStringArray(R.array.Height);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(fromHeight, checkedItem1[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        etfromHeightPartnerPreference.setText(fromHeight[which]);

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
        etToHeightPartnerPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.height);

                // title of the alert dialog
                alertDialog.setTitle("Choose your Height");

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
                        etToHeightPartnerPreference.setText(fromHeight[which]);

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

    private void incomeSelection() {
        final int[] checkedItem = {-1};
        etIncomePartnerPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.money);

                // title of the alert dialog
                alertDialog.setTitle("Choose Income");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] income = getResources().getStringArray(R.array.Income);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(income, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        etIncomePartnerPreference.setText(income[which]);

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

    private void profileCreatedBy() {
        final int[] checkedItem = {-1};
        etProfilePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose profile Created By");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] ProfileCreatedGroup = getResources().getStringArray(R.array.ProfileCreated);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(ProfileCreatedGroup, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        etProfilePreference.setText(ProfileCreatedGroup[which]);

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

    private void dietList() {
        // initialize selected language array
        selectedDeit = new boolean[DietStatusArray.length];

        etDietPreference.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                // Initialize alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set title
                builder.setTitle("Choose Life Style");
                // set the icon for the alert dialog
                builder.setIcon(R.drawable.bibimbap);
                // set dialog non cancelable
                builder.setCancelable(false);

                builder.setMultiChoiceItems(DietStatusArray, selectedDeit, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        // check condition
                        if (b) {
                            // when checkbox selected
                            // Add position  in lang list
                            DietList.add(i);
                            // Sort array list
                            Collections.sort(DietList);
                        } else {
                            // when checkbox unselected
                            // Remove position from langList
                            DietList.remove(Integer.valueOf(i));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Initialize string builder
                        StringBuilder stringBuilder = new StringBuilder();
                        // use for loop
                        for (int j = 0; j < DietList.size(); j++) {
                            // concat array value
                            stringBuilder.append(DietStatusArray[DietList.get(j)]);
                            // check condition
                            if (j != DietList.size() - 1) {
                                // When j value  not equal
                                // to lang list size - 1
                                // add comma
                                stringBuilder.append(", ");
                            }
                        }
                        // set text on textView
                        etDietPreference.setText(stringBuilder.toString());
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
                        for (int j = 0; j < selectedDeit.length; j++) {
                            // remove all selection
                            selectedDeit[j] = false;
                            // clear language list
                            DietList.clear();
                            // clear text view value
                            etDietPreference.setText("");
                        }
                    }
                });
                // show dialog
                // builder.show();


                // create the builder
                builder.create();

                // create the alert dialog with the
                // alert dialog builder instance
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setBackgroundColor(Color.BLACK);
//                Button buttonbackground3 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                buttonbackground3.setBackgroundColor(Color.BLACK);
            }
        });
    }

    private void listener() {
        tvPartnerPreferencesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFormPreference()) {
                    submitFormPreference();
                    hideKeyboard(v);
                }

            }
        });


//        multi_SelectionCountry.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(multi_SelectionCountry.getText().toString().trim().contains("Other"))
//                {
//                    etPreferenceCountry.setVisibility(View.VISIBLE);
//                }else
//                {
//                    etPreferenceCountry.setVisibility(View.GONE);
//                }
//            }
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


//        multi_SelectionState.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(multi_SelectionState.getText().toString().trim().contains("Other"))
//                {
//                    etPreferenceState.setVisibility(View.VISIBLE);
//                }else
//                {
//                    etPreferenceState.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });

//        tvMultipleCity.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if(tvMultipleCity.getText().toString().trim().contains("Other"))
//                {
//                    etPreferenceCity.setVisibility(View.VISIBLE);
//                }else
//                {
//                    etPreferenceCity.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
        etProfileSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etProfileSearch.getText().toString().trim().equalsIgnoreCase("")) {
                    tvSearchButton.setVisibility(View.GONE);
                    tvPartnerPreferencesBtn.setVisibility(View.VISIBLE);
                } else {
                    tvSearchButton.setVisibility(View.VISIBLE);
                    tvPartnerPreferencesBtn.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // search by id
        tvSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    hideKeyboard(view);
                    getSearchProfileData();
                }
            }
        });

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
    }

    private boolean checkFormPreference() {
        fromAge = etFromAgePartnerPreference.getText().toString().trim();
        toAge = etToAgePartnerPreference.getText().toString().trim();
        fromHeight = etfromHeightPartnerPreference.getText().toString().trim();
        toHeight = etToHeightPartnerPreference.getText().toString().trim();
        maritalStatus = tvMultipleMaritalStatus.getText().toString().trim();
        motherTongue = multi_SelectionMotherTongue.getText().toString().trim();
        religion = tvMultipleReligion.getText().toString().trim();
        cast = tvMultipleCast.getText().toString().trim();

        qualification = multi_SelectionQualification.getText().toString().trim();
        workingWith = multi_SelectionWorkingWith.getText().toString().trim();
        professionalArea = multi_SelectionProfessionArea.getText().toString().trim();
        createdBy = etProfilePreference.getText().toString().trim();
        String diet1 = etDietPreference.getText().toString().trim();
        diet = diet1.replace(",", "");

        income = etIncomePartnerPreference.getText().toString().toString();
        country = multi_SelectionCountry.getText().toString().trim();
        state = multi_SelectionState.getText().toString().trim();
        city = tvMultipleCity.getText().toString().toString();


        return true;
    }

    private boolean checkForm() {
        id = etProfileSearch.getText().toString().trim();
        if (id.isEmpty()) {
            etProfileSearch.setError("Please enter user id");
            etProfileSearch.setFocusableInTouchMode(true);
            etProfileSearch.requestFocus();
            return false;
        } else {
            etProfileSearch.setError(null);
        }
        return true;
    }

    public void getSearchProfileData() {
        String ProfileSearch = "http://103.174.102.195:8080/saathidaar_backend/api/member/get-details-by-member-id/";
        etProfileSearch.setText(" ");
        //final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Data Loading...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                ProfileSearch + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                // progressDialog.dismiss();
                Log.e("Search  response", String.valueOf(response));
                Gson gson = new Gson();

                if (response != null) {
                    data = gson.fromJson(String.valueOf(response), MemberProfileModel.class);
                    ProfileSearchFragment.newInstance((data.member_id), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Profile_search_fragment");
                } else {
                    Toast.makeText(context, "Profile Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layout_dialog = LayoutInflater.from(context).inflate(R.layout.alert_sucess_dialog, null);
        builder.setView(layout_dialog);

        TextView btnokSuccess = layout_dialog.findViewById(R.id.btnokSuccess);
        // show dialog


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getWindow().setGravity(Gravity.CENTER);

        btnokSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(context, MyMatchesShowActivity.class);
                startActivity(intent);

            }
        });
    }

    private void submitFormPreference() {
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

        params.put("member_id", sessionManager.getMemberId());
        Log.e("params", "Preferences" + String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, updatePreference + sessionManager.getMemberId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (!code.equalsIgnoreCase("") && code != null) {
//
                                successDialog();
                                //    Toast.makeText(context, code, Toast.LENGTH_SHORT).show();
                                // startActivity(requireActivity().getIntent());
                                // requireActivity().finish();
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
                getPreference + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  Log.e("response", String.valueOf((response)));
                if (response != null) {
                    Log.e("preference", String.valueOf(response));
                    Gson gson = new Gson();
                    model = gson.fromJson(String.valueOf(response), PartnerPreferenceModel.class);
                    setData();
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
        tvMultipleMaritalStatus.setText(Utils.nullToBlank(model.partner_marital_status));
        etFromAgePartnerPreference.setText(Utils.nullToBlank(model.partner_from_age));
        etToAgePartnerPreference.setText(Utils.nullToBlank(model.partner_to_age));
        multi_SelectionState.setText(model.partner_state);
        etfromHeightPartnerPreference.setText(Utils.nullToBlank(model.partner_from_height));
        tvMultipleCity.setText(model.partner_city);
        etToHeightPartnerPreference.setText(Utils.nullToBlank(model.partner_to_height));
        multi_SelectionCountry.setText(model.partner_country);
        multi_SelectionMotherTongue.setText(Utils.nullToBlank(model.partner_mother_tongue));
        etProfilePreference.setText(Utils.nullToBlank(model.partner_profile_created));
        multi_SelectionProfessionArea.setText(Utils.nullToBlank(model.partner_professional_area));
        etIncomePartnerPreference.setText(Utils.nullToBlank(model.partner_annual_income));
        etDietPreference.setText(Utils.nullToBlank(model.partner_lifestyles));
        multi_SelectionWorkingWith.setText(Utils.nullToBlank(model.partner_working_with));
        multi_SelectionQualification.setText(Utils.nullToBlank(model.partner_qualification));
        tvMultipleCast.setText(model.partner_cast);
        tvMultipleReligion.setText(model.partner_religions);


        if (model.partner_manglik_all != null && model.partner_manglik_all.equalsIgnoreCase("All")) {
            mrbOpenToAll.setChecked(true);
        } else if (model.partner_manglik_all != null && model.partner_manglik_all.equalsIgnoreCase("Yes")) {
            mrbOnlyManglik.setChecked(true);
        } else if (model.partner_manglik_all != null && model.partner_manglik_all.equalsIgnoreCase("No")) {
            mrbNoManglik.setChecked(true);
        } else if (model.partner_manglik_all != null && model.partner_manglik_all.equalsIgnoreCase("Don't Know")) {
            mrbDontNoManglik.setChecked(true);
        }
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
                // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("cast");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("cast_name");
                            castList.add(religion);
                            // Log.e("cast-list", String.valueOf(castList));
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
                //  Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("religion");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("religion_name");
                            religionList.add(religion);
                            // Log.e("Religion-list", String.valueOf(religionList));
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

        ProfessionAreaList.add("Open to all");
        ProfessionAreaList.add("Accounting,Banking & Finance");
        ProfessionAreaList.add("Administration & HR");
        ProfessionAreaList.add("Agriculture");
        ProfessionAreaList.add("Airlines & Aviation");
        ProfessionAreaList.add("Architecture & Design");
        ProfessionAreaList.add("Artists,Animators & Web Designers");
        ProfessionAreaList.add("BPO,KPO, & Customer Support");
        ProfessionAreaList.add("Beauty,Fashion & Jewellery Designers");
        ProfessionAreaList.add("Civil Service / Law Enforcement");
        ProfessionAreaList.add("Corporate Professionals");
        ProfessionAreaList.add("Defence");
        ProfessionAreaList.add("Engineering");
        ProfessionAreaList.add("Education & Training");
        ProfessionAreaList.add("IT & Software Engineering");
        ProfessionAreaList.add("Hotel & Hospitality");
        ProfessionAreaList.add("Legal");
        ProfessionAreaList.add("Medical & Healthcare");
        ProfessionAreaList.add("Merchant Navy");
        ProfessionAreaList.add("Non Working");
        ProfessionAreaList.add("Sale & Marketing");
        ProfessionAreaList.add("Science");

        ;
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
        workingWithItemsList.add("Open to all");
        workingWithItemsList.add("Private Company");
        workingWithItemsList.add("Government / Public Sector");
        workingWithItemsList.add("Defense / Civil Services");
        workingWithItemsList.add("Business / Self Employed");
        workingWithItemsList.add("Not Working");
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
        qualificationList.add("Open to all");
        qualificationList.add("Master");
        qualificationList.add("Bachelor / undergraduate");
        qualificationList.add("Associate / Diploma");
        qualificationList.add("High School and below");

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
            @SuppressLint("ResourceAsColor")
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
                // builder.show();


                // create the builder
                builder.create();

                // create the alert dialog with the
                // alert dialog builder instance
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setBackgroundColor(Color.BLACK);
//                Button buttonbackground3 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//                buttonbackground3.setBackgroundColor(Color.BLACK);
            }
        });
    }

    private void SpinnerAge() {
        final int[] checkedItem = {-1};
        final int[] checkedItem1 = {-1};
        etFromAgePartnerPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_age);

                // title of the alert dialog
                alertDialog.setTitle("Choose Age");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                // the function setSingleChoiceItems is the function which builds
                String[] age = getResources().getStringArray(R.array.AgeFrom);
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(age, checkedItem1[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem1[0] = which;

                        // now also update the TextView which previews the selected item
                        etFromAgePartnerPreference.setText(age[which]);

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
        etToAgePartnerPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_age);

                // title of the alert dialog
                alertDialog.setTitle("Choose Age");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                // the function setSingleChoiceItems is the function which builds
                String[] age = getResources().getStringArray(R.array.AgeFrom);
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(age, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        etToAgePartnerPreference.setText(age[which]);

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


    // dropDown With Search
    private void getCountryItems() {
        ArrayList<String> countryList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                countryUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response country", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        countryModel = gson.fromJson(String.valueOf(response), DataModelCountry.class);
                        JSONArray jsonArray = response.getJSONArray("country");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("country_name");
                            countryList.add(country);
                            // Log.e("country-list", String.valueOf(countryList));
                            multi_SelectionCountry.setItems(countryList);
                            multi_SelectionCountry.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(View view, boolean isSelected, int position) {
                                    //  Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();
                                    countryId = countryModel.country.get(position).country_id;
                                    Log.e("countryId", String.valueOf(countryId));
                                    state_arr[myCounter] = countryId;

                                    Log.e("selected items", new Gson().toJson(multi_SelectionCountry.getSelectedItems()));

                                    StringBuilder buffer = new StringBuilder();
                                    for (int i = position; i < countryList.size(); i++) {
                                        if (i > 0) {
                                            buffer.append(',');
                                        }
                                        buffer.append(countryModel.country.get(i).country_id);
                                    }
                                    System.out.println(buffer);
                                    Log.e("selected items", String.valueOf(buffer));


                                  //  getStateItems();
                                    getCityItems();
                                }

                                @Override
                                public void onSelectionCleared() {
                                    Toast.makeText(getContext(), "All items are unselected", Toast.LENGTH_SHORT).show();
                                }
                            });
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

    // dropDown With Search
    private void getStateItems() {
        countryName = multi_SelectionCountry.getText().toString().trim();
        ArrayList<String> stateList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://192.168.1.36:8088/api/get/multiples/state?country_ids=1", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String code = jsonObject.getString("result");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray array = new JSONArray(response);
                        array.getJSONArray(Integer.parseInt("state"));
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject1 = array.getJSONObject(i);
                                    String state = jsonObject1.getString("state_name");
                                    stateList.add(state);

                                    multi_SelectionState.setItems(stateList);
                                    multi_SelectionState.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(View view, boolean isSelected, int position) {
                                            getCityItems();
                                        }

                                        @Override
                                        public void onSelectionCleared() {
                                            Toast.makeText(getContext(), "All items are unselected", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }else {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },   new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(context, "Sorry, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<String, String>();
                params.put("country_ids", "1");
                Log.e("params", String.valueOf(params));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(stringRequest);
    }

    // dropDown With Search
    private void getCityItems() {
        ArrayList<String> cityList = new ArrayList<>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "http://192.168.1.36:8088/api/get/multiples/state?country_ids=1", null, new Response.Listener<JSONObject>() {
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
                            cityList.add(city);

                            tvMultipleCity.setItems(cityList);
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

    }



}
