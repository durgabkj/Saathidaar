package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelPrivacyOption;
import com.ottego.saathidaar.databinding.FragmentPrivacyOptionBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PrivacyOptionFragment extends Fragment {
    SessionManager sessionManager;
    Context context;
    FragmentPrivacyOptionBinding b;
    String radioText;
    String radioText1;
    String radioText2;
    DataModelPrivacyOption model;
    String radioText3;
    String radioText4;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String phonePrivacy = Utils.privacy + "phone";
    public String emailPrivacy = Utils.privacy + "email";
    public String photoPrivacy = Utils.privacy + "photo";
    public String dobPrivacy = Utils.privacy + "dob";
    public String incomePrivacy = Utils.privacy + "annual-income";

    public String getPrivacy = "http://103.150.186.33:8080/saathidaar_backend/api/privacy/get/all/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PrivacyOptionFragment() {
        // Required empty public constructor
    }


    public static PrivacyOptionFragment newInstance(String param1, String param2) {
        PrivacyOptionFragment fragment = new PrivacyOptionFragment();
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
        b = FragmentPrivacyOptionBinding.inflate(inflater,container,false);
        context = getContext();
        sessionManager = new SessionManager(context);
        listener();
        getData();
        return b.getRoot();
    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getPrivacy + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelPrivacyOption.class);
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

        if (model.data.size() > 0)
        {

            if (model.data.get(0).phone != null && model.data.get(0).phone.equalsIgnoreCase("Visible to all Member")) {
                b.radioButtonPhoneShowOnlyPMember.setChecked(true);
            } else if (model.data.get(0).phone != null && model.data.get(0).phone.equalsIgnoreCase("Visible to all Premium Members")) {
                b.radioButtonPhoneShowOnlyPMemberLike.setChecked(true);
            } else if (model.data.get(0).phone != null && model.data.get(0).phone.equalsIgnoreCase("Keep this private")) {
                b.radioButtonPhoneShowNoOne.setChecked(true);
            }

            if (model.data.get(0).email != null && model.data.get(0).email.equalsIgnoreCase("Visible to all Member")) {
                b.radioButtonPhoneShowAllPremiumMembers.setChecked(true);
            } else if (model.data.get(0).email != null && model.data.get(0).email.equalsIgnoreCase("Visible to all Premium Members")) {
                b.radioButtonPhoneShowPremiumMembersYouWishToConnect.setChecked(true);
            } else if (model.data.get(0).email != null && model.data.get(0).email.equalsIgnoreCase("Keep this private")) {
                b.radioButtonHideEmailAddress.setChecked(true);
            }


            if (model.data.get(0).photo != null && model.data.get(0).photo.equalsIgnoreCase("Visible to all Member")) {
                b.radioButtonPhotoShowAllMembers.setChecked(true);
            } else if (model.data.get(0).photo != null && model.data.get(0).photo.equalsIgnoreCase("Visible to all Premium Members")) {
                b.radioButtonPhotoShowPremiumMembersAndMemberILike.setChecked(true);
            } else if (model.data.get(0).photo != null && model.data.get(0).photo.equalsIgnoreCase("Keep this private")) {
                b.radioButtonVisiblePhotoOnlyMember.setChecked(true);
            }



            if (model.data.get(0).dob != null && model.data.get(0).dob.equalsIgnoreCase("Visible to all Member")) {
                b.radioButtonDOBFullDOB.setChecked(true);
            } else if (model.data.get(0).dob != null && model.data.get(0).dob.equalsIgnoreCase("Visible to all Premium Members")) {
                b.radioButtonDOBPremium.setChecked(true);
            } else if (model.data.get(0).dob != null && model.data.get(0).dob.equalsIgnoreCase("Keep this private")) {
                b.radioButtonDOBMonthAndYear.setChecked(true);
            }


            if (model.data.get(0).annual_income != null && model.data.get(0).annual_income.equalsIgnoreCase("Visible to all Member")) {
                b.radioButtonDOBFullIncome.setChecked(true);
            } else if (model.data.get(0).annual_income != null && model.data.get(0).annual_income.equalsIgnoreCase("Visible to all Premium Members")) {
                b.radioButtonIncomePremium.setChecked(true);
            } else if (model.data.get(0).annual_income != null && model.data.get(0).annual_income.equalsIgnoreCase("Keep this private")) {
                b.radioButtonIncomePrivate.setChecked(true);
            }
        }



    }

    private void listener() {
        b.tvEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.llshowPhonePrivacy.setVisibility(View.GONE);
                b.llHidePhone.setVisibility(View.VISIBLE);
                b.tvEditDOB.setVisibility(View.INVISIBLE);
                b.tvEditEmail.setVisibility(View.INVISIBLE);
                b.tvEditIncome.setVisibility(View.INVISIBLE);
                b.tvEditPhoto.setVisibility(View.INVISIBLE);
            }
        });


        b.tvCancelPhoneHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llHidePhone.setVisibility(View.GONE);
                b.llshowPhonePrivacy.setVisibility(View.VISIBLE);
                b.tvEditDOB.setVisibility(View.VISIBLE);
                b.tvEditEmail.setVisibility(View.VISIBLE);
                b.tvEditIncome.setVisibility(View.VISIBLE);
                b.tvEditPhoto.setVisibility(View.VISIBLE);
            }
        });
        b.tvEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llshowEmailPrivacy.setVisibility(View.GONE);
                b.llHideEmail.setVisibility(View.VISIBLE);

                b.tvEditPhone.setVisibility(View.INVISIBLE);
                b.tvEditDOB.setVisibility(View.INVISIBLE);
                b.tvEditIncome.setVisibility(View.INVISIBLE);
                b.tvEditPhoto.setVisibility(View.INVISIBLE);
            }
        });

        b.tvCancelEmailHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llHideEmail.setVisibility(View.GONE);
                b.llshowEmailPrivacy.setVisibility(View.VISIBLE);

                b.tvEditDOB.setVisibility(View.VISIBLE);
                b.tvEditPhone.setVisibility(View.VISIBLE);
                b.tvEditIncome.setVisibility(View.VISIBLE);
                b.tvEditPhoto.setVisibility(View.VISIBLE);
            }
        });


        b.tvEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llshowPhotoPrivacy.setVisibility(View.GONE);
                b.llHidePhoto.setVisibility(View.VISIBLE);

                b.tvEditEmail.setVisibility(View.INVISIBLE);
                b.tvEditDOB.setVisibility(View.INVISIBLE);
                b.tvEditIncome.setVisibility(View.INVISIBLE);
                b.tvEditPhoto.setVisibility(View.INVISIBLE);
            }
        });

        b.tvCancelPhotoHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llHidePhoto.setVisibility(View.GONE);
                b.llshowPhotoPrivacy.setVisibility(View.VISIBLE);
                b.tvEditPhoto.setVisibility(View.VISIBLE);

                b.tvEditDOB.setVisibility(View.VISIBLE);
                b.tvEditPhone.setVisibility(View.VISIBLE);
                b.tvEditIncome.setVisibility(View.VISIBLE);
                b.tvEditEmail.setVisibility(View.VISIBLE);
            }
        });


        b.tvEditDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llshowDOBPrivacy.setVisibility(View.GONE);
                b.llHideDOB.setVisibility(View.VISIBLE);

                b.tvEditEmail.setVisibility(View.INVISIBLE);
                b.tvEditPhone.setVisibility(View.INVISIBLE);
                b.tvEditIncome.setVisibility(View.INVISIBLE);
                b.tvEditPhoto.setVisibility(View.INVISIBLE);
            }
        });

        b.tvCancelDobHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llHideDOB.setVisibility(View.GONE);
                b.llshowDOBPrivacy.setVisibility(View.VISIBLE);

                b.tvEditPhoto.setVisibility(View.VISIBLE);
                b.tvEditPhone.setVisibility(View.VISIBLE);
                b.tvEditIncome.setVisibility(View.VISIBLE);
                b.tvEditEmail.setVisibility(View.VISIBLE);
            }
        });


        b.tvEditIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llshowIncomePrivacy.setVisibility(View.GONE);
                b.llHideIncome.setVisibility(View.VISIBLE);
                b.tvEditEmail.setVisibility(View.INVISIBLE);
                b.tvEditDOB.setVisibility(View.INVISIBLE);
                b.tvEditPhone.setVisibility(View.INVISIBLE);
                b.tvEditPhoto.setVisibility(View.INVISIBLE);
            }
        });

        b.tvCancelIncomeHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.llHideIncome.setVisibility(View.GONE);
                b.llshowIncomePrivacy.setVisibility(View.VISIBLE);

                b.tvEditDOB.setVisibility(View.VISIBLE);
                b.tvEditPhone.setVisibility(View.VISIBLE);
                b.tvEditPhoto.setVisibility(View.VISIBLE);
                b.tvEditEmail.setVisibility(View.VISIBLE);


            }
        });

        b.radioGroupPhone.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = b.radioGroupPhone.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                radioText = rb.getText().toString();
                b.tvPhoneShowText.setText(radioText);
            }
        });


        b.radioGroupEmail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = b.radioGroupEmail.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                radioText1 = rb.getText().toString();
                b.tvEmailShowText.setText(radioText1);
            }
        });


        b.radioGroupPhoto.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = b.radioGroupPhoto.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                radioText2 = rb.getText().toString();
                b.tvPhotoShowText.setText(radioText2);
            }
        });


        b.radioGroupDOB.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = b.radioGroupDOB.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                radioText3 = rb.getText().toString();
                b.tvDOBShowText.setText(radioText3);
            }
        });


        b.radioGroupIncome.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rb = b.radioGroupIncome.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                radioText4 = rb.getText().toString();
                b.tvIncomeShowText.setText(radioText4);
            }
        });

        b.tvSavePhoneHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPhoneForm();
            }
        });

        b.tvSavePhoneHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitEmailForm();
            }
        });


        b.tvSavePhotoHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitPhotoForm();
            }
        });


        b.tvSaveDobHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitDobForm();
            }
        });


        b.tvSaveIncomeHideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitIncomeForm();
            }
        });


    }

    private void submitIncomeForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("valAnnual_income", radioText4);
        params.put("member_id", new SessionManager(context).getMemberId());
        Log.e("params  Income privacy ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, incomePrivacy, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" IncomePrivacy response", String.valueOf((response)));

                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void submitDobForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("valDob", radioText3);
        params.put("member_id", new SessionManager(context).getMemberId());
        Log.e("params  dob privacy ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, dobPrivacy, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" dobPrivacy response", String.valueOf((response)));

                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Gson gson = new Gson();
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void submitPhotoForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("valPhoto", radioText2);
        params.put("member_id", new SessionManager(context).getMemberId());
        Log.e("params  Photo privacy ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, photoPrivacy, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" PhotoPrivacy response", String.valueOf((response)));

                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void submitEmailForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("valEmail", radioText1);
        params.put("member_id", new SessionManager(context).getMemberId());
        Log.e("params  Email privacy ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, emailPrivacy, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" emailPrivacy response", String.valueOf((response)));

                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    private void submitPhoneForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("valPhone", radioText);
        params.put("member_id", new SessionManager(context).getMemberId());
        Log.e("params  Phone privacy ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, phonePrivacy, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" phonePrivacy response", String.valueOf((response)));

                        try {
                            Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
