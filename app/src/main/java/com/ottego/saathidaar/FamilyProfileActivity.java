package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.ActivityFamilyProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FamilyProfileActivity extends AppCompatActivity {
    ActivityFamilyProfileBinding b;
    Context context;
    public String url = Utils.memberUrl + "update/11";
    String fatherStatus = "";
    String motherStatus = "";
    String companyNameF = "";
    String designationF = "";
    String natureBusinessF = "";
    String companyNameM = "";
    String designationM = "";
    String natureBusinessM = "";
    String familyLocation = "";
    String nativePlace = "";
    String marriedBrother = "";
    String unMarriedBrother = "";
    String marriedSister = "";
    String unMarriedSister = "";
    String familyType = "";
    String familyAffluence = "";
    MemberProfileModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityFamilyProfileBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = FamilyProfileActivity.this;

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, MemberProfileModel.class);
        fatherAndMotherStatus();
        FamilyType();
        FamilyAffluence();
        listener();
        setData();
    }

    private void setData() {
        if (model != null) {
            b.etFStatus.setText(model.father_status);
            b.etFatherCompanyName.setText(model.father_company_name);
            b.etFatherDesignation.setText(model.father_designation);
            b.etFatherNatureBusiness.setText(model.father_business_name);

            b.etMStatus.setText(model.father_status);
            b.etMotherCompanyName.setText(model.mother_company_name);
            b.etMotherDesignation.setText(model.mother_designation);
            b.etMotherNatureBusiness.setText(model.mother_business_name);
            b.etFAffluence.setText(model.family_affluence);
            b.etAddUserFamilyLocation.setText(model.family_location);
            b.etFType.setText(model.family_type);
            b.etAddUserBrotherMarried.setText(model.married_male);
            b.etAddUserBrotherNotMarried.setText(model.unmarried_male);

            b.etAddUserSisterMarried.setText(model.married_female);
            b.etAddUserSisterNotMarried.setText(model.unmarried_female);
        }
    }

    private void FamilyType() {

        final int[] checkedItem = {-1};
        b.etFType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose an Item");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] FamilyTypeList = getResources().getStringArray(R.array.FamilyType);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(FamilyTypeList, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etFType.setText(FamilyTypeList[which]);

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

    private void FamilyAffluence() {

        final int[] checkedItem = {-1};
        b.etFAffluence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose an Item");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] FamilyAffluenceList = getResources().getStringArray(R.array.Affluence);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(FamilyAffluenceList, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etFAffluence.setText(FamilyAffluenceList[which]);

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

    private void fatherAndMotherStatus() {
        final int[] checkedItem = {-1};

        final int[] checkedItem1 = {-1};

        b.etFStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose an Item");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] fatherAndMotherStatusList = getResources().getStringArray(R.array.FatherAndStatus);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(fatherAndMotherStatusList  , checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etFStatus.setText(fatherAndMotherStatusList[which]);

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
        b.etMStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.ic_baseline_supervisor_account_24);

                // title of the alert dialog
                alertDialog.setTitle("Choose an Item");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] fatherAndMotherStatusList = getResources().getStringArray(R.array.FatherAndStatus);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(fatherAndMotherStatusList  , checkedItem1[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etMStatus.setText(fatherAndMotherStatusList[which]);

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
        b.btnFsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();

            }
        });

        b.etMStatus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                motherStatus = b.etMStatus.getText().toString();
                if (motherStatus.equalsIgnoreCase("Retired")) {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Employed")) {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Business")) {
                    b.llMNatureBusiness.setVisibility(View.VISIBLE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Not Employed")) {
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Pass Away")) {
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


b.etFStatus.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        fatherStatus = b.etFStatus.getText().toString();

        if (fatherStatus.equalsIgnoreCase("Retired")) {
            b.llCompanyName.setVisibility(View.VISIBLE);
            b.llDesignation.setVisibility(View.VISIBLE);
            b.llBusiness.setVisibility(View.GONE);
        }

        if (fatherStatus.equalsIgnoreCase("Employed")) {
            b.llCompanyName.setVisibility(View.VISIBLE);
            b.llDesignation.setVisibility(View.VISIBLE);
            b.llBusiness.setVisibility(View.GONE);
        }
        if (fatherStatus.equalsIgnoreCase("Business")) {
            b.llBusiness.setVisibility(View.VISIBLE);
            b.llCompanyName.setVisibility(View.GONE);
            b.llDesignation.setVisibility(View.GONE);
        }

        if (fatherStatus.equalsIgnoreCase("Not Employed")) {
            b.llBusiness.setVisibility(View.GONE);
            b.llCompanyName.setVisibility(View.GONE);
            b.llDesignation.setVisibility(View.GONE);
        }


        if (fatherStatus.equalsIgnoreCase("Pass Away")) {
            b.llBusiness.setVisibility(View.GONE);
            b.llCompanyName.setVisibility(View.GONE);
            b.llDesignation.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
});




}

//    private boolean checkForm() {
//
//        if (fatherStatus.equalsIgnoreCase("select")) {
//            b.tvfs.setError("please select Status");
//            b.tvfs.setFocusableInTouchMode(true);
//            b.tvfs.requestFocus();
//            return false;
//        } else {
//            b.tvfs.setError(null);
//        }
//
//        if (motherStatus.equalsIgnoreCase("select")) {
//            b.tvms.setError("please select Status");
//            b.tvms.setFocusableInTouchMode(true);
//            b.tvms.requestFocus();
//            return false;
//        } else {
//            b.tvms.setError(null);
//        }
//
//
//
//        if (familyLocation.isEmpty()) {
//            b.etAddUserFamilyLocation.setError("Please write about you");
//            b.etAddUserFamilyLocation.setFocusableInTouchMode(true);
//            b.etAddUserFamilyLocation.requestFocus();
//            return false;
//        } else {
//            b.etAddUserFamilyLocation.setError(null);
//        }
//
//
//
//        if (nativePlace.isEmpty()) {
//            b.etNativePalace.setError("Please write about you");
//            b.etNativePalace.setFocusableInTouchMode(true);
//            b.etNativePalace.requestFocus();
//            return false;
//        } else {
//            b.etNativePalace.setError(null);
//        }
//
//        if (unMarriedBrother.isEmpty() ||  marriedBrother.isEmpty()) {
//            b.tvSibling.setError("Please write about you");
//            b.tvSibling.setFocusableInTouchMode(true);
//            b.tvSibling.requestFocus();
//            return true;
//        } else {
//            b.tvSibling.setError(null);
//        }
//
////        if (marriedBrother.isEmpty()) {
////            b.etAddUserBrotherMarried.setError("Please write about you");
////            b.etAddUserBrotherMarried.setFocusableInTouchMode(true);
////            b.etAddUserBrotherMarried.requestFocus();
////            return false;
////        } else {
////            b.etAddUserBrotherMarried.setError(null);
////        }
//
//        if (marriedSister.isEmpty() || unMarriedSister.isEmpty()) {
//            b.tvSibling.setError("Please enter no.of sister");
//            b.tvSibling.setFocusableInTouchMode(true);
//            b.tvSibling.requestFocus();
//            return false;
//        } else {
//            b.tvSibling.setError(null);
//        }
//
////        if (unMarriedSister.isEmpty()) {
////            b.etAddUserSisterMarried.setError("Please write about you");
////            b.etAddUserSisterMarried.setFocusableInTouchMode(true);
////            b.etAddUserSisterMarried.requestFocus();
////            return false;
////        } else {
////            b.etAddUserSisterMarried.setError(null);
////        }
//
//
//
//        if (familyType.equalsIgnoreCase("select")) {
//            b.tvft.setError("please select family type");
//            b.tvft.setFocusableInTouchMode(true);
//            b.tvft.requestFocus();
//            return false;
//        } else {
//            b.tvft.setError(null);
//        }
//
//        if (familyAffluence.equalsIgnoreCase("select")) {
//            b.tvfa.setError("please select family Affluence");
//            b.tvfa.setFocusableInTouchMode(true);
//            b.tvfa.requestFocus();
//            return false;
//        } else {
//            b.tvfa.setError(null);
//        }
//        return true;
//
//    }

    private void submitForm() {
        fatherStatus = b.etFStatus.getText().toString().trim();
        motherStatus = b.etMStatus.getText().toString().trim();
        companyNameF = b.etFatherCompanyName.getText().toString().trim();
        designationF = b.etFatherDesignation.getText().toString().trim();
        natureBusinessF = b.etFatherNatureBusiness.getText().toString().trim();
        companyNameM = b.etMotherCompanyName.getText().toString().trim();
        designationM = b.etMotherDesignation.getText().toString().trim();
        natureBusinessM = b.etMotherNatureBusiness.getText().toString().trim();
        familyLocation = b.etAddUserFamilyLocation.getText().toString().trim();
        nativePlace = b.etNativePalace.getText().toString().trim();
        marriedBrother = b.etAddUserBrotherMarried.getText().toString().trim();
        unMarriedBrother = b.etAddUserBrotherNotMarried.getText().toString().trim();
        marriedSister = b.etAddUserSisterMarried.getText().toString().trim();
        unMarriedSister = b.etAddUserSisterNotMarried.getText().toString().trim();
        familyType = b.etFType.getText().toString().trim();
        familyAffluence = b.etFAffluence.getText().toString().trim();


        Map<String, String> params = new HashMap<String, String>();
        params.put("father_status", fatherStatus);
        params.put("mother_status", motherStatus);
        params.put("father_company_name", companyNameF);
        params.put("father_designation", designationF);
        params.put("father_business_name", natureBusinessF);
        params.put("mother_company_name", companyNameM);
        params.put("mother_designation", designationM);
        params.put("mother_business_name", natureBusinessM);
        params.put("family_location", familyLocation);
        params.put("native_place", nativePlace);
        params.put("family_type", familyType);
        params.put("family_values", "");
        params.put("family_affluence", familyAffluence);
        params.put("married_male", marriedBrother);
        params.put("unmarried_male", unMarriedBrother);
        params.put("married_female", marriedSister);
        params.put("unmarried_female", unMarriedSister);

        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
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


