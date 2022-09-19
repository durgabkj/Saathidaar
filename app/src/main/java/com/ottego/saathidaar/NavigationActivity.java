package com.ottego.saathidaar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.databinding.ActivityNavigationBinding;

import java.util.Calendar;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding b;
    MyReceiver myReceiver = new MyReceiver();
    Context context;


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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcvSathidaar, HomeFragment.newInstance("", ""))
                .commit();

        b.tlMatch.setTabGravity(TabLayout.GRAVITY_FILL);
        b.tlMatch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcvSathidaar, getFragment(tab.getPosition()))
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }


        });

        b.tlMatch.getTabAt(0).select();


        setNotification();
    }

    public Fragment getFragment(int position) {
        Log.d("position", position + "");
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MatchesFragment();
            case 2:
                return new InboxFragment();
            case 3:
                return new AccountFragment();
            case 4:
                return new UpgradeFragment();
            default:
                return null;
        }
    }


    private void setNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 36);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        SharedPreferences setting = getSharedPreferences("PREFS", 0);
        int lastDay = setting.getInt("day", 0);

        if (lastDay != currentDay) {
            SharedPreferences.Editor editor = setting.edit();
            editor.putInt("day", currentDay);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            }
        }

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
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
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
