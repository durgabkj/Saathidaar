package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityMoreBinding;
import com.ottego.saathidaar.databinding.ActivityPersonalInfoShowBinding;

public class PersonalInfoShowActivity extends AppCompatActivity {
ActivityPersonalInfoShowBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b= ActivityPersonalInfoShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        listener();

    }

    private void listener() {
        b.BasicLife.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}