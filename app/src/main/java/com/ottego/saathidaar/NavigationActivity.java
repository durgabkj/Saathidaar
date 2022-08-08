package com.ottego.saathidaar;

import android.content.Context;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TabHost;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.ViewPageAdapter;
import com.ottego.saathidaar.databinding.ActivityNavigationBinding;

import java.io.IOException;
import java.util.Objects;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding b;
    MyReceiver myReceiver = new MyReceiver();
    TabLayout tabLayout;
    SwipeDisabledPager viewPager;
    Context context;
   MediaPlayer mp;
    int[] tabIcons = {
            R.drawable.logo1,
            R.drawable.ic_couple,
            R.drawable.ic_email,
            R.drawable.ic_user,
            R.drawable.ic_diamond,
    };
    private boolean doubleBackToExitPressedOnce;
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = NavigationActivity.this;
        tabLayout = findViewById(R.id.tlMatch);
        viewPager = findViewById(R.id.vpMatch);
        FragmentManager fragmentManager = getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Matches"));
        tabLayout.addTab(tabLayout.newTab().setText("Inbox"));
        tabLayout.addTab(tabLayout.newTab().setText("Account"));
        tabLayout.addTab(tabLayout.newTab().setText("Premium"));
        setupTabIcons();



//      tabLayout.setSmoothScrollingEnabled(false);

        viewPager.setPagingEnable(false);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPageAdapter adapter = new ViewPageAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                 viewPager.setCurrentItem(tab.getPosition());
                  mp = MediaPlayer.create(NavigationActivity.this, R.raw.app);
                  mp.start();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //  viewPager.setCurrentItem(tab.getPosition());

            }


        });

        Objects.requireNonNull(tabLayout.getTabAt(1)).select();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }


    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(myReceiver, filter);

        super.onStart();
    }


    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
    }


}
