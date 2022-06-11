package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.ottego.saathidaar.databinding.ActivityFamilyProfileBinding;

public class FamilyProfileActivity extends AppCompatActivity {
ActivityFamilyProfileBinding b;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityFamilyProfileBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context=FamilyProfileActivity.this;
        fatherAndMotherStatus();
        FamilyType();
        FamilyAffluence();

    }

    private void FamilyType() {
        String[]  FamilyTypeList = getResources().getStringArray(R.array.FamilyType);
        ArrayAdapter  FamilyTypeAdapter = new ArrayAdapter(context, R.layout.dropdown_item, FamilyTypeList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFamilyType.setAdapter(FamilyTypeAdapter);
    }

    private void FamilyAffluence() {
        String[]  FamilyAffluenceList = getResources().getStringArray(R.array.Affluence);
        ArrayAdapter  FamilyAffluenceAdapter = new ArrayAdapter(context, R.layout.dropdown_item, FamilyAffluenceList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFamilyAffluence.setAdapter(FamilyAffluenceAdapter);

    }

    private void fatherAndMotherStatus() {
        String[]  fatherAndMotherStatusList = getResources().getStringArray(R.array.FatherAndStatus);
        ArrayAdapter  fatherAndMotherStatusAdapter = new ArrayAdapter(context, R.layout.dropdown_item, fatherAndMotherStatusList);
        //Setting the ArrayAdapter data on the Spinner
        b.UserFatherStatus.setAdapter(fatherAndMotherStatusAdapter);
        b.UserMotherStatus.setAdapter(fatherAndMotherStatusAdapter);
    }
}