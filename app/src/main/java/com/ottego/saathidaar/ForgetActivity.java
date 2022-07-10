package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.databinding.ActivityForgetBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetActivity extends AppCompatActivity {
    ActivityForgetBinding b;
    SessionManager sessionManager;
    Context context;
    public String receiveOtp = Utils.memberUrl + "forgot/password/otp";
    public String updatepass = Utils.memberUrl + "forgot/password/update";
    String OtpVerifyUrl = Utils.memberUrl + "verify/otp/";
    String phone;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityForgetBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = ForgetActivity.this;
        sessionManager = new SessionManager(context);
        b.etForgetPhone.setText(sessionManager.getPhone1());

        listener();

    }

    private void listener() {
        b.btnForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkForm()) {
                    submit();
                }
            }
        });


        b.btnForgetPassVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkFormotp()) {
                    verify();
                }

            }
        });
    }

    private void verify() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, OtpVerifyUrl + otp + "/" + phone, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf(response));

                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                update();
                                Toast.makeText(context, "your password has been Updated on registered Email", Toast.LENGTH_SHORT).show();
//                                startActivity(intent);
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                            Log.e("Error Response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }
    private void update() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, OtpVerifyUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.e("Error Response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }


    private boolean checkFormotp() {
        otp = b.etOTP.getText().toString().trim();
        if (otp.length() < 4) {
            b.etOTP.setError("Password must be at least 4 Digit long");
            b.etOTP.setFocusableInTouchMode(true);
            b.etOTP.requestFocus();
            return false;
        } else {
            b.etOTP.setError(null);
        }
        return true;
    }


    private boolean checkForm() {

        phone = b.etForgetPhone.getText().toString().trim();

        if (phone.isEmpty()) {
            b.etForgetPhone.setError("Please enter mobile number");
            b.etForgetPhone.setFocusableInTouchMode(true);
            b.etForgetPhone.requestFocus();
            return false;
        } else if (!Utils.isValidMobile(phone)) {
            b.etForgetPhone.setError("Invalid mobile number");
            b.etForgetPhone.setFocusableInTouchMode(true);
            b.etForgetPhone.requestFocus();
            return false;
        } else {
            b.etForgetPhone.setError(null);
        }
        return true;
    }

    private void submit() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, receiveOtp, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                b.etForgetPhone.setVisibility(View.GONE);
                                b.btnForgetPassResend.setVisibility(View.GONE);
                                b.btnForgetPass.setVisibility(View.GONE);

                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();

                                b.etOTP.setVisibility(View.VISIBLE);
                                b.btnForgetPassVerify.setVisibility(View.VISIBLE);
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Toast.makeText(context, "Try again......", Toast.LENGTH_LONG).show();
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }


}