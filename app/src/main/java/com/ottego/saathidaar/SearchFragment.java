package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.Model.SearchModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment {
    MultipleSelection multi_SelectionMotherTongueSearch, multi_SelectionCountrySearch, tvMultipleReligionSearch, tvMultipleCastSearch, tvMultipleStateSearch, tvMultipleCitySearch;
    Context context;
    SearchModel model;
    TextView tvSearchMatchBtn;
    SessionManager sessionManager;
    String member_id;
    TextView etFromAgeSearch, etToAgeSearch, etfromHeightSearch, etToHeightSearch;
    // Initialize variables
    Spinner spMinSearch, spMaxSearch, spFromHeightSearch, spToHeightSearch;
    TextView tvMultipleMaritalStatusSearch;
    public String SearchUrl = Utils.memberUrl + "search/update/";
    public String getSearchDetailUrl = Utils.memberUrl + "search/get/";
    public String ReligionUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/religion-name";
    public String countryUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/country";
    public String castUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/all/cast";
    public String stateUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/state";
    public String cityUrl = "http://103.174.102.195:8080/saathidaar_backend/api/get/all/city";


    ArrayList<String> AgeListSearch = new ArrayList<>();
    ArrayList<String> minListSearch = new ArrayList<>();
    ArrayList<String> maxListSearch = new ArrayList<>();
    ArrayAdapter<String> minAdapterSearch, maxAdapterSearch;


    //For MaritalStatus....
    boolean[] selectedMaritalStatus;
    ArrayList<Integer> MaritalStatusList = new ArrayList<>();
    String[] MaritalStatusArray = {"Never Married", "Divorce", "Widowed", "Awaiting Divorce", "Annulled"};


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
    String city = "";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        context = getContext();
        sessionManager = new SessionManager(context);
        member_id = sessionManager.getMemberId();

        multi_SelectionMotherTongueSearch = view.findViewById(R.id.multi_SelectionMotherTongueSearch);
        multi_SelectionCountrySearch = view.findViewById(R.id.multi_SelectionCountrySearch);
        tvMultipleMaritalStatusSearch = view.findViewById(R.id.tvMultipleMaritalStatusSearch);
        tvMultipleReligionSearch = view.findViewById(R.id.tvMultipleReligionSearch);
        tvMultipleCastSearch = view.findViewById(R.id.tvMultipleCastSearch);
        tvMultipleCitySearch = view.findViewById(R.id.tvMultipleCitySearch);
        tvMultipleStateSearch = view.findViewById(R.id.tvMultipleStateSearch);
        tvSearchMatchBtn = view.findViewById(R.id.tvSearchMatchBtn);
        etFromAgeSearch = view.findViewById(R.id.etFromAgeSearch);
        etToAgeSearch = view.findViewById(R.id.etToAgeSearch);
        etfromHeightSearch = view.findViewById(R.id.etfromHeightSearch);
        etToHeightSearch = view.findViewById(R.id.etToHeightSearch);


        Height();
        SpinnerAge();
        multipleSelectionMotherTongue();
        multipleMaritalStatusSelectionCheckBox();
        multipleReligionSelectionCheckBox();
        multipleCastSelectionCheckBox();
        multipleCountrySelectionCheckBox();
        multipleStateSelectionCheckBox();
        multipleCityCheckBox();

        getData();

        listener();

        return view;
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getSearchDetailUrl + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response searched data ", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), SearchModel.class);
                if(model!=null)
                {
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
        tvMultipleMaritalStatusSearch.setText(model.search_marital_status);
        etFromAgeSearch.setText(model.search_from_age);
        etToAgeSearch.setText(model.search_to_age);
        tvMultipleCitySearch.setText(model.search_city);
        tvMultipleStateSearch.setText(model.search_state);
        tvMultipleCastSearch.setText(model.search_cast);
        etfromHeightSearch.setText(model.search_from_height);
        tvMultipleCitySearch.setText(model.search_city);
        etToHeightSearch.setText(model.search_to_height);
        multi_SelectionCountrySearch.setText(model.search_country);
        multi_SelectionMotherTongueSearch.setText(model.search_mother_tongue);

    }

    private void listener() {

        tvSearchMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                hideKeyboard(view);
            }
        });


//        spToHeightSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String myToHeight = spToHeightSearch.getSelectedItem().toString();
//
//                if (myToHeight.equals("select")) {
//
//                } else {
//                    etToHeightSearch.setText(myToHeight);
//                }
//                //
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//        spFromHeightSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String myFromHeight = spFromHeightSearch.getSelectedItem().toString();
//
//                if (myFromHeight.equals("select")) {
//
//                } else {
//                    etfromHeightSearch.setText(myFromHeight);
//                }
//                //
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//
//
//        spMaxSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String temp1 = spMaxSearch.getSelectedItem().toString().trim();
//
//                if (temp1.equalsIgnoreCase("19 Years")) {
//                    etToAgeSearch.setText("");
//                } else {
//                    etToAgeSearch.setText(spMaxSearch.getSelectedItem().toString().trim());
//
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });

    }


    private void Height() {

        final int[] checkedItem = {-1};
        final int[] checkedItem1 = {-1};
        etfromHeightSearch.setOnClickListener(new View.OnClickListener() {
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
                alertDialog.setSingleChoiceItems(fromHeight, checkedItem1[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        etfromHeightSearch.setText(fromHeight[which]);

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
        etToHeightSearch.setOnClickListener(new View.OnClickListener() {
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
                        etToHeightSearch.setText(fromHeight[which]);

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

    private void SpinnerAge() {
        final int[] checkedItem = {-1};
        final int[] checkedItem1 = {-1};
        etFromAgeSearch.setOnClickListener(new View.OnClickListener() {
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
                        etFromAgeSearch.setText(age[which]);

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
        etToAgeSearch.setOnClickListener(new View.OnClickListener() {
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
                        etToAgeSearch.setText(age[which]);

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

    private void multipleSelectionMotherTongue() {
        multi_SelectionMotherTongueSearch.setItems(getMotherTongueItems());
        multi_SelectionMotherTongueSearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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

        Log.e("mother Tongue", String.valueOf(motherTongueList));
        return motherTongueList;
    }

    private void multipleMaritalStatusSelectionCheckBox() {
        // initialize selected language array
        selectedMaritalStatus = new boolean[MaritalStatusArray.length];

        tvMultipleMaritalStatusSearch.setOnClickListener(new View.OnClickListener() {
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
                        tvMultipleMaritalStatusSearch.setText(stringBuilder.toString());
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
                            tvMultipleMaritalStatusSearch.setText("");
                        }
                    }
                });
                // show dialog

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setTextColor(R.color.colorPrimary);
                Button buttonbackground = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setTextColor(R.color.colorPrimary);

            }
        });
    }

    private void multipleReligionSelectionCheckBox() {
        tvMultipleReligionSearch.setItems(getReligionItems());
        tvMultipleReligionSearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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


    private void multipleCastSelectionCheckBox() {
        tvMultipleCastSearch.setItems(getCastItems());
        tvMultipleCastSearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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


    private void multipleCountrySelectionCheckBox() {
        multi_SelectionCountrySearch.setItems(getCountryItems());
        multi_SelectionCountrySearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
             //   Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("country");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String country = jsonObject1.getString("country_name");
                            countryList.add(country);
                            //Log.e("country-list", String.valueOf(countryList));
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
        tvMultipleStateSearch.setItems(getStateItems());
        tvMultipleStateSearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
               // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("state");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String state = jsonObject1.getString("state_name");
                            stateList.add(state);
                           // Log.e("state-list", String.valueOf(stateList));
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
        tvMultipleCitySearch.setItems(getCityItems());
        tvMultipleCitySearch.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
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
               // Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("city");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String city = jsonObject1.getString("city_name");
                            cityList.add(city);
                           // Log.e("city-list", String.valueOf(cityList));
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

    private void submitForm() {
        fromAge = etFromAgeSearch.getText().toString().trim();
        toAge = etToAgeSearch.getText().toString().trim();
        fromHeight = etfromHeightSearch.getText().toString().trim();
        toHeight = etToHeightSearch.getText().toString().trim();
        maritalStatus = tvMultipleMaritalStatusSearch.getText().toString().trim();
        motherTongue = multi_SelectionMotherTongueSearch.getText().toString().trim();
        religion = tvMultipleReligionSearch.getText().toString().trim();
        cast = tvMultipleCastSearch.getText().toString().trim();
        country = multi_SelectionCountrySearch.getText().toString().trim();
        state = tvMultipleStateSearch.getText().toString().trim();
        city = tvMultipleCitySearch.getText().toString().trim();

        Map<String, String> params = new HashMap<String, String>();
        params.put("search_from_age", fromAge);
        params.put("search_to_age", toAge);
        params.put("search_from_height", fromHeight);
        params.put("search_to_height", toHeight);
        params.put("search_marital_status", maritalStatus);
        params.put("search_mother_tongue", motherTongue);
        params.put("search_cast", cast);

//        params.put("search_marital_status", country);
//        params.put("search_mother_tongue",state);
//        params.put("search_cast", city);

        params.put("member_id", sessionManager.getMemberId());
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SearchUrl + member_id, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (!code.equalsIgnoreCase("") && code != null) {
//                                    Gson gson = new Gson();
//                                    UserModel sessionModel = gson.fromJson(String.valueOf((response)), UserModel.class);
//                                   // sessionManager.createSUserDetails(sessionModel);
                                Toast.makeText(context, code, Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
                                Intent intent = new Intent(context, MoreActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(intent);
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