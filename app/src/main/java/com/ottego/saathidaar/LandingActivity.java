package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.UserModel;
import com.ottego.saathidaar.databinding.ActivityLandingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LandingActivity extends AppCompatActivity {
    public static ViewPager viewPager;
    ActivityLandingBinding binding;
    ViewFlipper flipper;
    private Button click;
    Context context;
    String registerurl = Utils.userUrl + "register";
    String firstName = "";
    String lastName = "";
    String email = "";
    String gender = "";
    String phone = "";
    String password = "";
    String profilecreatedby = "";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
            context = LandingActivity.this;

            listener();

            String[] items = new String[]{"Profile created By","Self", "Sibling", "Parents", "Relatives", "Friends", "Saathidaar.com", "Franchies"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            binding.spinner1.setAdapter(adapter);

            int imgarray[] = {R.drawable.landing1, R.drawable.landing2, R.drawable.landing3};
            flipper = (ViewFlipper) findViewById(R.id.flipper);

            for (int i = 0; i < imgarray.length; i++)
                showimage(imgarray[i]);
        }

        public void showimage(int img) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(img);
            flipper.addView(imageView);
            flipper.setAutoStart(true);
            flipper.setFlipInterval(5000);

           // flipper.setInAnimation(this, android.R.anim.slide_out_right);
            flipper.setInAnimation(this, android.R.anim.slide_in_left);
        }

        private void listener() {
            binding.btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkForm()) {
                        submitForm();
                        hideKeyboard();
                    }
                }
            });

            binding.tvCondition.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://192.168.1.38:9094/account/term-condition"));
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        intent.setData(Uri.parse("http://192.168.1.38:9094/account/term-condition"));
                    }
                }
            });


            binding.tvHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,LandingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            binding.tvLandingMemberLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            binding.tvLandingMemberSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,LandingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });


            binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.female:
                            gender = "female";

                        case R.id.male:
                            gender = "male";
                    }
                }
            });

            binding.btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,LoginActivity.class);
                    startActivity(intent);
                }
            });

        }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = LandingActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

        private boolean checkForm() {
            firstName = binding.txtfirstname.getText().toString().trim();
            lastName = binding.txtlastname.getText().toString().trim();
            phone = binding.txtmobile.getText().toString().trim();
            email = binding.txtemail.getText().toString().trim();
       //     password = binding.txtpassword.getText().toString().trim();
            profilecreatedby = binding.spinner1.getSelectedItem().toString();


            Log.e("data", firstName + lastName + phone + email + password + profilecreatedby);


            if (gender.isEmpty()) {
                Toast.makeText(context, "Please select gender", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (firstName.isEmpty()) {
                binding.txtfirstname.setError("Please enter your first name");
                binding.txtfirstname.setFocusableInTouchMode(true);
                binding.txtfirstname.requestFocus();
                return false;
            } else {
                binding.txtfirstname.setError(null);
            }

            if (lastName.isEmpty()) {
                binding.txtlastname.setError("Please enter your last name");
                binding.txtlastname.setFocusableInTouchMode(true);
                binding.txtlastname.requestFocus();
                return false;
            } else {
                binding.txtlastname.setError(null);
            }

            if (phone.isEmpty()) {
                binding.txtmobile.setError("Please enter mobile number");
                binding.txtmobile.setFocusableInTouchMode(true);
                binding.txtmobile.requestFocus();
                return false;
            } else if (!Utils.isValidMobile(phone)) {
                binding.txtmobile.setError("Invalid mobile no.");
                binding.txtmobile.setFocusableInTouchMode(true);
                binding.txtmobile.requestFocus();
                return false;
            } else {
                binding.txtmobile.setError(null);
            }

            if (email.isEmpty()) {
                binding.txtemail.setError("Please email id");
                binding.txtemail.setFocusableInTouchMode(true);
                binding.txtemail.requestFocus();
                return false;
            } else if (!Utils.isValidEmail(email)) {
                binding.txtemail.setError("Invalid email.");
                binding.txtemail.setFocusableInTouchMode(true);
                binding.txtemail.requestFocus();
                return false;
            } else {
                binding.txtemail.setError(null);
            }

//            if (password.isEmpty()) {
//                binding.txtpassword.setError("Please enter your password");
//                binding.txtpassword.setFocusableInTouchMode(true);
//                binding.txtpassword.requestFocus();
//                return false;
//            } else if (password.length() < 6) {
//                binding.txtpassword.setError("password must be at least 6 character long");
//                binding.txtpassword.setFocusableInTouchMode(true);
//                binding.txtpassword.requestFocus();
//                return false;
//            } else {
//                binding.txtpassword.setError(null);
//            }

            if (binding.spinner1.getSelectedItem().toString().trim().contains("Profile created By")) {
                Toast.makeText(context, " please select one ", Toast.LENGTH_SHORT).show();
            }

            return true;
        }

   //
        private void submitForm() {
            Map<String, String> params = new HashMap<String, String>();
            params.put("firstName", firstName);
            params.put("lastName", lastName);
            params.put("email", email);
            params.put("gender", gender);
            params.put("phone", phone);
            params.put("role", Utils.role_user);
           // params.put("password", password);
            params.put("profilecreatedby", profilecreatedby);
            Log.e("params", String.valueOf(params));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, registerurl, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("response", String.valueOf((response)));
                            try {
                                String code = response.getString("result");
                                if (code.equalsIgnoreCase("1")) {
//                                    Gson gson = new Gson();
//                                    UserModel sessionModel = gson.fromJson(String.valueOf((response)), UserModel.class);
//                                   // sessionManager.createSUserDetails(sessionModel);
                                     // Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
                                    Intent intent = new Intent(context, OtpVerificationActivity.class);
                                    intent.putExtra("mobile", phone);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
