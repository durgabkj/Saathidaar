package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    String url = Utils.userUrl+"login";
    ActivityLoginBinding b;
    Context context;
    SessionManager sessionManager;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = LoginActivity.this;
        sessionManager = new SessionManager(context);
        listener();
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = LoginActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void listener() {
        b.mtbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if (checkForm()) {
                    submitForm();
                }
            }

            private boolean checkForm() {
                email = b.etLoginUserName.getText().toString().trim();
                password = b.etLoginPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    b.etLoginUserName.setError("Please enter Email");
                    b.etLoginUserName.setFocusableInTouchMode(true);
                    b.etLoginUserName.requestFocus();
                    return false;
                } else if (!Utils.isValidEmail(email)) {
                    b.etLoginUserName.setError("Invalid email");
                    b.etLoginUserName.setFocusableInTouchMode(true);
                    b.etLoginUserName.requestFocus();
                    return false;
                } else if (password.length() < 6) {
                    b.etLoginPassword.setError("Password must be at least 6 characters long");
                    b.etLoginPassword.setFocusableInTouchMode(true);
                    b.etLoginPassword.requestFocus();
                    return false;
                } else {
                    b.etLoginPassword.setError(null);
                }
                return true;
            }
        });


        b.mbLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LandingActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });
       b.tvForgetPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(context,ForgetActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
           }
       });

    }

    private void submitForm() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", email);
        params.put("password", password);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Gson gson = new Gson();
                                SessionModel model = gson.fromJson(String.valueOf(response), SessionModel.class);
                                sessionManager.createSession(model);
                                Intent intent = new Intent(context, NavigationActivity.class);
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Toast.makeText(context,"Try again......",Toast.LENGTH_LONG).show();
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }
}