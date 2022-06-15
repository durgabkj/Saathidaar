package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.ottego.saathidaar.databinding.ActivityFamilyProfileBinding;

public class FamilyProfileActivity extends AppCompatActivity {
    ActivityFamilyProfileBinding b;
    Context context;

    String fatherStatus = "";
    String motherStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityFamilyProfileBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = FamilyProfileActivity.this;
        fatherAndMotherStatus();
        FamilyType();
        FamilyAffluence();

    }

    private void FamilyType() {
        String[] FamilyTypeList = getResources().getStringArray(R.array.FamilyType);
        ArrayAdapter FamilyTypeAdapter = new ArrayAdapter(context, R.layout.dropdown_item, FamilyTypeList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFamilyType.setAdapter(FamilyTypeAdapter);
    }

    private void FamilyAffluence() {
        String[] FamilyAffluenceList = getResources().getStringArray(R.array.Affluence);
        ArrayAdapter FamilyAffluenceAdapter = new ArrayAdapter(context, R.layout.dropdown_item, FamilyAffluenceList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFamilyAffluence.setAdapter(FamilyAffluenceAdapter);

    }

    private void fatherAndMotherStatus() {
        String[] fatherAndMotherStatusList = getResources().getStringArray(R.array.FatherAndStatus);
        ArrayAdapter fatherAndMotherStatusAdapter = new ArrayAdapter(context, R.layout.dropdown_item, fatherAndMotherStatusList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFatherStatus.setAdapter(fatherAndMotherStatusAdapter);
        b.UserMotherStatus.setAdapter(fatherAndMotherStatusAdapter);



        b.UserMotherStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                motherStatus = b.UserMotherStatus.getSelectedItem().toString();
                Log.e("Selected-Status", motherStatus);

                if (motherStatus.equalsIgnoreCase("Retired"))
                {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }


                if (motherStatus.equalsIgnoreCase("Employed")) {
                    b.llMCompany.setVisibility(View.VISIBLE);
                    b.llMDesignation.setVisibility(View.VISIBLE);
                    b.llMNatureBusiness.setVisibility(View.GONE);
                }
                if (motherStatus.equalsIgnoreCase("Business")) {
                    b.llMNatureBusiness.setVisibility(View.VISIBLE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }

                if(motherStatus.equalsIgnoreCase("Not Employed")){
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }


                if(motherStatus.equalsIgnoreCase("Pass Away")){
                    b.llMNatureBusiness.setVisibility(View.GONE);
                    b.llMCompany.setVisibility(View.GONE);
                    b.llMDesignation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        b.UserFatherStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fatherStatus = b.UserFatherStatus.getSelectedItem().toString();
                Log.e("Selected-Status", fatherStatus);

                if (fatherStatus.equalsIgnoreCase("Retired"))
                {
                    b.llCompanyName.setVisibility(View.VISIBLE);
                    b.llDesignation.setVisibility(View.VISIBLE);
                    b.llBusiness.setVisibility(View.GONE);
                }


                if (fatherStatus.equalsIgnoreCase("Employed")) {
                    b.llCompanyName.setVisibility(View.VISIBLE);
                    b.llDesignation.setVisibility(View.VISIBLE);
                    b.llBusiness.setVisibility(View.GONE);
                }
                if (fatherStatus.equalsIgnoreCase("Business")) {
                    b.llBusiness.setVisibility(View.VISIBLE);
                    b.llCompanyName.setVisibility(View.GONE);
                    b.llDesignation.setVisibility(View.GONE);
                }

                if(fatherStatus.equalsIgnoreCase("Not Employed")){
                    b.llBusiness.setVisibility(View.GONE);
                    b.llCompanyName.setVisibility(View.GONE);
                    b.llDesignation.setVisibility(View.GONE);
                }


                if(fatherStatus.equalsIgnoreCase("Pass Away")){
                    b.llBusiness.setVisibility(View.GONE);
                    b.llCompanyName.setVisibility(View.GONE);
                    b.llDesignation.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}