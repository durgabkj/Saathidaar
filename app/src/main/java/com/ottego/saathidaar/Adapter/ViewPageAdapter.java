package com.ottego.saathidaar.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ottego.saathidaar.InboxFragment;
import com.ottego.saathidaar.MatchesFragment;
import com.ottego.saathidaar.HomeFragment;
import com.ottego.saathidaar.AccountFragment;
import com.ottego.saathidaar.UpgradeFragment;


public class ViewPageAdapter extends FragmentPagerAdapter {

    Context mContext;
    int TotalTabs;

    public ViewPageAdapter(Context context, FragmentManager fragmentManager, int totalTabs) {
        super(fragmentManager);
        mContext = context;
        TotalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
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

    @Override
    public int getCount() {
        return TotalTabs;
    }

}
