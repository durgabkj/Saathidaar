package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ottego.saathidaar.Adapter.ViewPageAdapter;
import com.ottego.saathidaar.databinding.ActivityNavigationBinding;

public class NavigationActivity extends AppCompatActivity {
    ActivityNavigationBinding b;

    TabLayout tabLayout;
    ViewPager viewPager;
    Context context;


    int[] tabIcons = {
            R.drawable.logo1,
            R.drawable.ic_couple,
            R.drawable.ic_inbox,
            R.drawable.ic_user,
            R.drawable.ic_diamond,
    };

    //MySathidarFragment sathidarFragment=new MySathidarFragment();
//    MatchesFragment matchesFragment=new MatchesFragment();
//   SearchFragment searchFragment=new SearchFragment();
//   InboxFragment inboxFragment=new InboxFragment();
//   ProfileFragment profileFragment=new ProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = NavigationActivity.this;
        tabLayout = findViewById(R.id.tlMatch);
        viewPager = findViewById(R.id.vpMatch);


        FragmentManager fragmentManager=getSupportFragmentManager();

        tabLayout.addTab(tabLayout.newTab().setText("Home"));
        tabLayout.addTab(tabLayout.newTab().setText("Matches"));
        tabLayout.addTab(tabLayout.newTab().setText("Inbox"));
        tabLayout.addTab(tabLayout.newTab().setText("Account"));
        tabLayout.addTab(tabLayout.newTab().setText("Premium"));
        setupTabIcons();


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

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


//       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,sathidarFragment).commit();
//
//       b.bnvSaathidar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//           @Override
//           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//               switch (item.getItemId())
//               {
//                   case R.id.mnuSathi:
//                       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,sathidarFragment).commit();
//                       return true;
//
//                   case R.id.mnuMatch:
//                       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,matchesFragment).commit();
//                       return true;
//
//                   case R.id.mnuSearch:
//                       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,searchFragment).commit();
//                       return true;
//
//                   case R.id.mnuInbox:
//                       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,inboxFragment).commit();
//                       return true;
//
//                   case R.id.mnuProfile:
//                       getSupportFragmentManager().beginTransaction().replace(R.id.fcvSathidaar,profileFragment).commit();
//                       return true;
//               }
//
//               return false;
//           }
//       });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
    }
}
