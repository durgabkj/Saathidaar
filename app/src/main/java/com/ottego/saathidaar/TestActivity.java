package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityRequestAcceptBinding;
import com.ottego.saathidaar.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {
ActivityTestBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        listener();
    }

    private void listener() {
        b.mtbProfileSentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}