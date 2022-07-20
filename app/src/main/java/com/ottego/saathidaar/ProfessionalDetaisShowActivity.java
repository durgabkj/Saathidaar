package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityPersonalInfoShowBinding;
import com.ottego.saathidaar.databinding.ActivityProfessionalDetaisShowBinding;

public class ProfessionalDetaisShowActivity extends AppCompatActivity {

    ActivityProfessionalDetaisShowBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b= ActivityProfessionalDetaisShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        listener();

    }

    private void listener() {
        b.ProfessionalDetails.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

}