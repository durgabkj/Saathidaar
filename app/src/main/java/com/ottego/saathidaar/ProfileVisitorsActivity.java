package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityProfileVisitorsBinding;
import com.ottego.saathidaar.databinding.ActivityRequestAcceptBinding;

public class ProfileVisitorsActivity extends AppCompatActivity {
ActivityProfileVisitorsBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfileVisitorsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        listener();
    }

    private void listener() {
        b.mtbProfileVisitorsActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}