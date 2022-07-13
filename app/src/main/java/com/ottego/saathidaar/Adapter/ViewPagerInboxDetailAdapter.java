package com.ottego.saathidaar.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ottego.saathidaar.InboxDetailFragment;
import com.ottego.saathidaar.MatchDetailsFragment;
import com.ottego.saathidaar.viewmodel.InboxViewModel;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

public class ViewPagerInboxDetailAdapter  extends FragmentStateAdapter{
        InboxViewModel viewModel;

        public ViewPagerInboxDetailAdapter(@NonNull FragmentActivity fragmentActivity, InboxViewModel viewModel) {
            super(fragmentActivity);
            this.viewModel = viewModel;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Log.e("member_id Inbox ", viewModel.list1.getValue().get(position).member_id);

            return InboxDetailFragment.newInstance(viewModel.list1.getValue().get(position).member_id, "");

        }

        @Override
        public int getItemCount() {
            return viewModel.list1.getValue().size();
        }


    }
