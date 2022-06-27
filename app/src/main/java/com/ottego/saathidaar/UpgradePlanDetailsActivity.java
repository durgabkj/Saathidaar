package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.databinding.ActivityUpgradePlanDetailsBinding;

public class UpgradePlanDetailsActivity extends AppCompatActivity {
ActivityUpgradePlanDetailsBinding b;
UpgradeModel model;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityUpgradePlanDetailsBinding.inflate(getLayoutInflater());
context=UpgradePlanDetailsActivity.this;
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, UpgradeModel.class);

        setData();

        setContentView(b.getRoot());
    }

    private void setData() {
        b.tvMembershipPlan.setText(model.plan_price);
        b.tvMembershipPlan1.setText(model.plan_price);
    }
}