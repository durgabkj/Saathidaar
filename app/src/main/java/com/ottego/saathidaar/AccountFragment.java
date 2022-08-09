package com.ottego.saathidaar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;
import com.ottego.saathidaar.databinding.FragmentAccountBinding;

import java.util.List;


public class AccountFragment extends Fragment {

    FragmentAccountBinding b;
    AccountSettingFragment accountSettingFragment =new AccountSettingFragment();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        b = FragmentAccountBinding.inflate(getLayoutInflater());

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvAccount, accountSettingFragment)
                .commit();

        b.chipGroupAccount.check(b.chipGroupAccount.getChildAt(0).getId());
        listener();
        return b.getRoot();
    }

    private void listener() {
        b.chipGroupAccount.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                b.vpAccount.setCurrentItem(group.getCheckedChipId());
                Fragment fragment = null;
                switch (group.getCheckedChipId()) {
                    case R.id.chipAccountSetting: {
                        fragment = AccountSettingFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipEmailSetting: {
                        fragment = EmailAndSmsAlertFragment.newInstance("", "");
                        break;
                    }
                    case R.id.chipPrivacy: {
                        fragment = PrivacyOptionFragment.newInstance("", "");
                        break;
                    }

                    case R.id.chipHideUnHide: {
                        fragment = HideDeleteProfileFragment.newInstance("", "");
                        break;
                    }

                    default: {
                        fragment = AccountSettingFragment.newInstance("", "");
                        break;
                    }

                }
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcvAccount, fragment)
                        .commit();


            }

        });
    }


}