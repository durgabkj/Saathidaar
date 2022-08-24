package com.ottego.saathidaar.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ottego.saathidaar.MatchDetailsFragment;
import com.ottego.saathidaar.viewmodel.MatchViewModel;

public class ViewPagerMatchDetailAdapter extends FragmentStateAdapter {
    MatchViewModel viewModel;
    public ViewPagerMatchDetailAdapter(@NonNull FragmentActivity fragmentActivity, MatchViewModel viewModel) {
        super(fragmentActivity);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("member_id", viewModel.list.getValue().get(position).member_id);

        return MatchDetailsFragment.newInstance(viewModel.list.getValue().get(position).member_id, "");
    }

    @Override
    public int getItemCount() {
        return viewModel.list.getValue().size();
    }


}
