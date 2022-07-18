package com.ottego.saathidaar.Adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ottego.saathidaar.Fragment.FamilyInfoFragment;
import com.ottego.saathidaar.Fragment.PersonalInfoFragment;
import com.ottego.saathidaar.Fragment.ProfessionalInfoFragment;

import java.util.ArrayList;
import java.util.List;

public class ProfileViewPager extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public ProfileViewPager(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("position", position + "");
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public void addFragment(Fragment fragment ,String title){
        fragmentList.add(fragment);
        titleList.add(title);
    }

}
