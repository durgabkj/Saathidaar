package com.ottego.saathidaar.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ottego.saathidaar.MatchDetailsFragment;
import com.ottego.saathidaar.MatchPagerFragment;

public class ViewPagerMatchDetailAdapter extends FragmentStateAdapter {


        public ViewPagerMatchDetailAdapter(@NonNull FragmentActivity fragmentActivity)
        {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0:
                    return  new MatchDetailsFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getItemCount() {return 21; }


    }
