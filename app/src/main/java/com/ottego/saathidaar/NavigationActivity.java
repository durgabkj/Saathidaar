package com.ottego.saathidaar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.ViewPageAdapter;
import com.ottego.saathidaar.databinding.ActivityNavigationBinding;

import java.util.Locale;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding b;

    TabLayout tabLayout;
    SwipeDisabledPager viewPager;
    Context context;
    int[] tabIcons = {
            R.drawable.logo1,
            R.drawable.ic_couple,
            R.drawable.ic_inbox,
            R.drawable.ic_user,
            R.drawable.ic_diamond,
    };


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
        tabLayout.getTabAt(2).getOrCreateBadge().setNumber(10);
        ViewPageAdapter adapter = new ViewPageAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //  viewPager.setCurrentItem(tab.getPosition());
            }
        });

        tabLayout.getTabAt(1).select();

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }
}
