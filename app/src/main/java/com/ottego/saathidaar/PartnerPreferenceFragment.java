package com.ottego.saathidaar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ottego.multipleselectionspinner.MultipleSelection;
import com.ottego.saathidaar.databinding.FragmentPartnerPreferenceBinding;

import java.util.ArrayList;
import java.util.List;


public class PartnerPreferenceFragment extends Fragment {
FragmentPartnerPreferenceBinding b;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    MultipleSelection multipleSelection;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartnerPreferenceFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static PartnerPreferenceFragment newInstance(String param1, String param2) {
        PartnerPreferenceFragment fragment = new PartnerPreferenceFragment();
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

        View view= inflater.inflate(R.layout.fragment_partner_preference, container, false);
multipleSelection=view.findViewById(R.id.multi_Selection);
       multipleSelection.setItems(getItems());
        multipleSelection.setOnItemSelectedListener(new MultipleSelection.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, boolean isSelected, int position) {
//                Toast.makeText(MainActivity.this, "On Item selected : " + isSelected, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSelectionCleared() {
//                Toast.makeText(MainActivity.this, "All items are unselected", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private List getItems() {
        ArrayList<String> alphabetsList = new ArrayList<>();
//        for (char i = 'A'; i <= 'Z'; i++)
//            alphabetsList.add(Character.toString(i));
        alphabetsList.add("hello");
        return alphabetsList;
    }

    }
