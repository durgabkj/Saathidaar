package com.ottego.saathidaar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ottego.saathidaar.databinding.FragmentAccountSettingBinding;


public class AccountSettingFragment extends Fragment {

    FragmentAccountSettingBinding b;
    String email;
    String password;
    String oldPass;
    String newPass;
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

        listener();
        changeEmail();
        changePassword();
        return b.getRoot();
    }

    private void listener() {
        b.tvEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditEmail.setVisibility(View.GONE);
                b.tvEmailShow.setVisibility(View.GONE);
                b.llSave.setVisibility(View.VISIBLE);
                b.etEmailChange.setVisibility(View.VISIBLE);

            }
        });

        b.tvEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditPassword.setVisibility(View.GONE);
                b.tvPasswordShow.setVisibility(View.GONE);
                b.llSavePassword.setVisibility(View.VISIBLE);
                b.etOldPassword.setVisibility(View.VISIBLE);
                b.etNewPassword.setVisibility(View.VISIBLE);

            }
        });

        b.tvCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditPassword.setVisibility(View.VISIBLE);
                b.tvPasswordShow.setVisibility(View.VISIBLE);
                b.llSavePassword.setVisibility(View.GONE);
                b.etOldPassword.setVisibility(View.GONE);
                b.etNewPassword.setVisibility(View.GONE);
            }
        });


        b.tvCancelPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvEditEmail.setVisibility(View.VISIBLE);
                b.tvEmailShow.setVisibility(View.VISIBLE);
                b.llSave.setVisibility(View.GONE);
                b.etEmailChange.setVisibility(View.GONE);
            }
        });



        b.etEmailChange.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                b.llPasswordEmail.setVisibility(View.VISIBLE);
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

    private void changeEmail() {
        b.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkEmail()) {
                    submitForm();
                }

            }
        });


    }



    private boolean checkEmail() {
        email = b.etEmailChange.getText().toString().trim();
        password = b.etEnterPassword.getText().toString().trim();

        if (email.isEmpty()) {
            b.etEmailChange.setError("Email mandatory");
            b.etEmailChange.setFocusableInTouchMode(true);
            b.etEmailChange.requestFocus();
            return false;
        } else if (!Utils.isValidEmail(email)) {
            b.etEmailChange.setError("Invalid email.");
            b.etEmailChange.setFocusableInTouchMode(true);
            b.etEmailChange.requestFocus();
            return false;
        } else {
            b.etEmailChange.setError(null);
        }


        if (password.isEmpty()) {
            b.etEnterPassword.setError("password mandatory");
            b.etEnterPassword.setFocusableInTouchMode(true);
            b.etEnterPassword.requestFocus();
            return false;
        } else if (password.length() < 6) {
            b.etEnterPassword.setError("password must be at least 6 character");
            b.etEnterPassword.setFocusableInTouchMode(true);
            b.etEnterPassword.requestFocus();
            return false;
        } else {
            b.etEnterPassword.setError(null);
        }

        return true;
    }


    private void submitForm() {

    }


}