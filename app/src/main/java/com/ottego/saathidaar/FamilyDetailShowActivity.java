package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityFamilyDetailShowBinding;
import com.ottego.saathidaar.databinding.ActivityPersonalInfoShowBinding;

public class FamilyDetailShowActivity extends AppCompatActivity {
    ActivityFamilyDetailShowBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b= ActivityFamilyDetailShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        listener();

    }

    private void listener() {
        b.FamilyDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}