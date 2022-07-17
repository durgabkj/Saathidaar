package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.databinding.ActivityShowImageBinding;

public class ShowImageActivity extends AppCompatActivity {
    ActivityShowImageBinding b;
    Context context;
    ImageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, ImageModel.class);
context=ShowImageActivity.this;




        setData();
        listener();
    }

    private void listener() {

    }


    private void setData() {
        Glide.with(context)
                .load(Utils.imageUrl + model.member_images)
                .into(b.ivProfilePic);
    }

}