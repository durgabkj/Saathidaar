package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
Context context;
SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        sessionManager = new SessionManager(context);

        start();
    }

    private void start() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()) {
                    startActivity(new Intent(context, NavigationActivity.class));
                } else {
                    startActivity(new Intent(context, LandingActivity.class));
                }

                finish();

//                Intent intent=new Intent(MainActivity.this,NavigationActivity.class);
//                startActivity(intent);
            }

        }, 2000);
    }
    }