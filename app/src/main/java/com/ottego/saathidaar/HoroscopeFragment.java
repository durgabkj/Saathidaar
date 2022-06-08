package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ottego.saathidaar.databinding.FragmentHoroscopeBinding;
import com.ottego.saathidaar.databinding.FragmentPartnerPreferenceBinding;


public class HoroscopeFragment extends Fragment {

    FragmentHoroscopeBinding b;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HoroscopeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HoroscopeFragment newInstance(String param1, String param2) {
        HoroscopeFragment fragment = new HoroscopeFragment();
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
        b = FragmentHoroscopeBinding.inflate(getLayoutInflater());

        String[] hour = getResources().getStringArray(R.array.Hour);
        ArrayAdapter aa = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, hour);
        //Setting the ArrayAdapter data on the Spinner
        b.acvHour.setAdapter(aa);


        String[] minutes = getResources().getStringArray(R.array.minutes);
        ArrayAdapter min = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, minutes);
        //Setting the ArrayAdapter data on the Spinner
        b.acvMinutes.setAdapter(min);

        String[] am = getResources().getStringArray(R.array.ampm);
        ArrayAdapter ampm = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, am);
        //Setting the ArrayAdapter data on the Spinner
        b.actvampm.setAdapter(ampm);

        String[] aprox = getResources().getStringArray(R.array.aprox);
        ArrayAdapter apr = new ArrayAdapter(requireActivity(), R.layout.dropdown_item, aprox);
        //Setting the ArrayAdapter data on the Spinner
        b.actvapprox.setAdapter(apr);


        return b.getRoot();
    }
}