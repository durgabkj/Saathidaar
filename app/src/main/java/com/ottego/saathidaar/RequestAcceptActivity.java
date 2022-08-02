package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ottego.saathidaar.databinding.ActivityGalleryBinding;
import com.ottego.saathidaar.databinding.ActivityRequestAcceptBinding;

public class RequestAcceptActivity extends AppCompatActivity {
ActivityRequestAcceptBinding b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityRequestAcceptBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        listener();
    }

    private void listener() {
        b.mtbSentFragmentActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}