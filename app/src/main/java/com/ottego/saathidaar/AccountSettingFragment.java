package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.databinding.FragmentAccountSettingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class AccountSettingFragment extends Fragment {

    FragmentAccountSettingBinding b;
    Context context;
    String email;
    SessionManager sessionManager;
    String password;
    String oldPass;
    String newPass;
    public  String changePass=Utils.userUrl+"changepwd";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountSettingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AccountSettingFragment newInstance(String param1, String param2) {
        AccountSettingFragment fragment = new AccountSettingFragment();
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
        b = FragmentAccountSettingBinding.inflate(getLayoutInflater());
        context=getContext();
        sessionManager=new SessionManager(context);
        listener();
        //changeEmail();
        changePassword();
        b.tvEmailShow.setText(sessionManager.getEmail());
        return b.getRoot();
    }

    private void listener() {

        b.llCallingPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String call="7030600035";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call));
                startActivity(intent);

            }
        });
        b.llWhatsAppPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = "9835635191";
                //NOTE : please use with country code first 2digits without plus signed
                try {
                    String mobile = "7781027704";
                    String msg = "Its Working";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg)));
                }catch (Exception e){
                    //whatsapp app not install
                }
            }
        });

        b.llEmailPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@saathidaar.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Subject:-");
//                    i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });





        b.tvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditPassword.setVisibility(View.GONE);
                b.tvPasswordShow.setVisibility(View.GONE);
                b.llSavePasswordbutton.setVisibility(View.VISIBLE);
                b.etOldPassword.setVisibility(View.VISIBLE);
                b.etNewPassword.setVisibility(View.VISIBLE);

            }
        });

        b.tvCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditPassword.setVisibility(View.VISIBLE);
                b.tvPasswordShow.setVisibility(View.VISIBLE);
                b.llSavePasswordbutton.setVisibility(View.GONE);
                b.etOldPassword.setVisibility(View.GONE);
                b.etNewPassword.setVisibility(View.GONE);
                b.etOldPassword.setText("");
                b.etNewPassword.setText("");
            }
        });

    }

    private void changePassword() {
        b.tvSavePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPassword()) {
                    submitFormPass();
                }
            }
        });
    }

    private void submitFormPass() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("username",sessionManager.getEmail());
        params.put("oldPassword", oldPass);
        params.put("newPassword", newPass);
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, changePass, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {

                                b.etOldPassword.setText("");
                                b.etNewPassword.setText("");
                                b.tvPasswordShow.setVisibility(View.VISIBLE);
                                b.tvEditPassword.setVisibility(View.VISIBLE);
                                b.llSavePasswordbutton.setVisibility(View.GONE);
                                b.etOldPassword.setVisibility(View.GONE);
                                b.etNewPassword.setVisibility(View.GONE);
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
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
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }

    private boolean checkPassword() {
        oldPass = b.etOldPassword.getText().toString().trim();
        newPass = b.etNewPassword.getText().toString().trim();


        if (oldPass.isEmpty()) {
            b.etOldPassword.setError("Old password mandatory");
            b.etOldPassword.setFocusableInTouchMode(true);
            b.etOldPassword.requestFocus();
            return false;
        } else if (oldPass.length() < 6) {
            b.etOldPassword.setError("password must be at least 6 character");
            b.etOldPassword.setFocusableInTouchMode(true);
            b.etOldPassword.requestFocus();
            return false;
        } else {
            b.etOldPassword.setError(null);
        }


        if (newPass.isEmpty()) {
            b.etNewPassword.setError("Enter new password");
            b.etNewPassword.setFocusableInTouchMode(true);
            b.etNewPassword.requestFocus();
            return false;
        } else if (newPass.length() < 6) {
            b.etNewPassword.setError("password must be at least 6 character");
            b.etNewPassword.setFocusableInTouchMode(true);
            b.etNewPassword.requestFocus();
            return false;
        } else {
            b.etNewPassword.setError(null);
        }

        return true;
    }


//
//    private void buttonProgress() {
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void run() {
//                b.btnSave.setText("Done");
//                b.progressBar.setVisibility(View.VISIBLE);
//                b.btnSave.setTextColor((R.color.Green));
//                requireActivity().finish();
//
//            }
//        },4000);
//    }
}