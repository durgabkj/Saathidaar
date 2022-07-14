package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelSmsAlert;
import com.ottego.saathidaar.Model.PartnerPreferenceModel;
import com.ottego.saathidaar.databinding.FragmentEmailAndSmsAlertBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class EmailAndSmsAlertFragment extends Fragment {
Context context;
SessionManager sessionManager;
DataModelSmsAlert model;
 FragmentEmailAndSmsAlertBinding b;
 String premium;
    String today;
    String recent;
    public String smsAlertUrl=Utils.alert+"update/email-sms";
    public  String getSmsAlertUrl=Utils.alert+"get/email-sms/";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmailAndSmsAlertFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EmailAndSmsAlertFragment newInstance(String param1, String param2) {
        EmailAndSmsAlertFragment fragment = new EmailAndSmsAlertFragment();
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
       b=FragmentEmailAndSmsAlertBinding.inflate(getLayoutInflater());
       context=getContext();
       sessionManager=new SessionManager(context);
       getData();
       listener();
       return b.getRoot();
    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getSmsAlertUrl+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response1", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelSmsAlert.class);

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

        if(model!=null)
        {
            if (model.data.get(0).premium_match_mail!=null && model.data.get(0).premium_match_mail.equalsIgnoreCase("Daily")){
                b.rbPremiumDaily.setChecked(true);
            }else if (model.data.get(0).premium_match_mail!=null && model.data.get(0).premium_match_mail.equalsIgnoreCase("Weekly")){
                b.rbPremiumWeekly.setChecked(true);
            }else if (model.data.get(0).premium_match_mail!=null && model.data.get(0).premium_match_mail.equalsIgnoreCase("Monthly")) {
                b.rbPremiumMonthly.setChecked(true);
            }else if (model.data.get(0).premium_match_mail!=null && model.data.get(0).premium_match_mail.equalsIgnoreCase("Unsubscribe")) {
                b.rbPremiumUnsubscribe.setChecked(true);
            }

            if (model.data.get(0).recent_visitors_email!=null && model.data.get(0).recent_visitors_email.equalsIgnoreCase("Daily")){
                b.rbVisitorsDaily.setChecked(true);
            }else if (model.data.get(0).recent_visitors_email!=null && model.data.get(0).recent_visitors_email.equalsIgnoreCase("Weekly")){
                b.rbVisitorsWeekly.setChecked(true);
            }else if (model.data.get(0).recent_visitors_email!=null && model.data.get(0).recent_visitors_email.equalsIgnoreCase("Monthly")) {
                b.rbVisitorsMonthly.setChecked(true);
            }else if (model.data.get(0).recent_visitors_email!=null && model.data.get(0).recent_visitors_email.equalsIgnoreCase("Unsubscribe")) {
                b.rbVisitorsUnsubscribe.setChecked(true);
            }



            if (model.data.get(0).today_match_email!=null && model.data.get(0).today_match_email.equalsIgnoreCase("Daily")){
                b.rbTodayMatchDaily.setChecked(true);
            }else if (model.data.get(0).today_match_email!=null && model.data.get(0).today_match_email.equalsIgnoreCase("Weekly")){
                b.rbTodayMatchWeekly.setChecked(true);
            }else if (model.data.get(0).today_match_email!=null && model.data.get(0).today_match_email.equalsIgnoreCase("Monthly")) {
                b.rbTodayMatchMonthly.setChecked(true);
            }else if (model.data.get(0).today_match_email!=null && model.data.get(0).today_match_email.equalsIgnoreCase("Unsubscribe")) {
                b.rbTodayMatchUnsubscribe.setChecked(true);
            }

        }



    }

    private void listener() {
        b.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rbPremium = b.radioGroup1.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                premium = rbPremium.getText().toString();
            }
        });

        b.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rbRecent = b.radioGroup2.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                recent = rbRecent.getText().toString();
            }
        });

        b.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                RadioButton rbToday = b.radioGroup3.findViewById(id);
//                OR
//                RadioButton rb=(RadioButton) findViewById(checkedId);

                today = rbToday.getText().toString();
            }
        });


        b.tvSaveAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    submitForm();

            }
        });
    }

    private void submitForm() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("member_id",new SessionManager(context).getMemberId());
        params.put("premium_match_mail", premium);
        params.put("recent_visitors_email", recent);
        params.put("today_match_email", today);
        Log.e(" Alert params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, smsAlertUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf((response)));
                        try {
                            if (response!=null) {
                                Toast.makeText(context, "Email-SMS Alert Updated", Toast.LENGTH_SHORT).show();

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