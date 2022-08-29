package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.databinding.ActivityOtpVerificationBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends AppCompatActivity {
    ActivityOtpVerificationBinding binding;
    ImageButton user_profile_photo;
    Context context;
    String phone;
    String otpSentUrl = Utils.memberUrl + "send-sms";
    String otp = "";
    String OtpVerifyUrl = Utils.memberUrl + "verify/otp/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        context = OtpVerificationActivity.this;
        setContentView(binding.getRoot());
        phone = getIntent().getStringExtra("mobile");
        EditText mEdit;
        mEdit = (EditText) findViewById(R.id.edtOtpCode);
        otp = mEdit.getText().toString().trim();

        binding.tvmobileNo.setText(phone);
       // receiveOtp();
        listener();
    }

    private void receiveOtp() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone_number", phone);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, otpSentUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" otp receive response", String.valueOf(response));

                        try {
                            String code = response.getString("result");
                            if (code.equalsIgnoreCase("1")) {

                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                             //   Intent intent = new Intent(context, LoginActivity.class);
                              //  startActivity(intent);
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

    private void listener() {
        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    submitFormOtp();
                }
            }
        });


        binding.txtresentotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveOtp();
            }
        });


    }

    private boolean checkForm() {
        otp = binding.edtOtpCode.getText().toString().trim();
        if (otp.isEmpty() || otp.length() < 4) {
            binding.edtOtpCode.setError("Please enter OTP number");
            binding.edtOtpCode.setFocusableInTouchMode(true);
            binding.edtOtpCode.requestFocus();
            return false;
        } else {
            binding.edtOtpCode.setError(null);
        }
        return true;
    }
    private void submitFormOtp() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, OtpVerifyUrl + otp + "/" + phone, new JSONObject(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" otp verify response", String.valueOf(response));

                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
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

}


