package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ottego.saathidaar.databinding.ActivityMatchesDetailsBinding;

public class MatchesDetailsActivity extends AppCompatActivity {
    Animation animation;
    ActivityMatchesDetailsBinding b;

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMatchesDetailsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

context=MatchesDetailsActivity.this;
        animation = AnimationUtils.loadAnimation(context, R.anim.move);
      //  b.llDetailCad.startAnimation(animation);
    }
}