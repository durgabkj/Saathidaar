package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;

import com.ottego.saathidaar.Adapter.NewMatchesAdapter;
import com.ottego.saathidaar.Adapter.UpgradeAdapter;
import com.ottego.saathidaar.Model.DataModelUpgrade;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.databinding.ActivityUpgradeBinding;

import java.util.List;

public class UpgradeActivity extends AppCompatActivity {
ActivityUpgradeBinding b;

Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    b=ActivityUpgradeBinding.inflate(getLayoutInflater());
    context=UpgradeActivity.this;
   setContentView(b.getRoot());


    }
}