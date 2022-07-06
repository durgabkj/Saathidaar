package com.ottego.saathidaar.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ottego.saathidaar.MatchDetailsFragment;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

public class ViewPagerMatchDetailAdapter extends FragmentStateAdapter {
    NewMatchViewModel viewModel;

    public ViewPagerMatchDetailAdapter(@NonNull FragmentActivity fragmentActivity, NewMatchViewModel viewModel) {
        super(fragmentActivity);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return MatchDetailsFragment.newInstance(viewModel.list.getValue().get(position).member_id, "");

    }

    @Override
    public int getItemCount() {
        return viewModel.list.getValue().size();
    }


}
