package com.ottego.saathidaar.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ottego.saathidaar.FamilyInfoFragment;
import com.ottego.saathidaar.PersonalInfoFragment;
import com.ottego.saathidaar.ProfessionalInfoFragment;

public class ProfileViewPager extends FragmentPagerAdapter {
    Context mContext;
    int TotalTabs;

    public ProfileViewPager(Context context, FragmentManager fragmentManager, int totalTabs) {
        super(fragmentManager);
        mContext = context;
        TotalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("position", position + "");
        switch (position) {
            case 0:
                return new PersonalInfoFragment();
            case 1:
                return new FamilyInfoFragment();

            case 2:
                return new ProfessionalInfoFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TotalTabs;
    }

}
