package com.ottego.saathidaar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Context context;
    ImageView ivSplash;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        sessionManager = new SessionManager(context);

        ivSplash=findViewById(R.id.ivSplash);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom);
        ivSplash.startAnimation(animation);
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

            }

        }, 3000);
    }
    }