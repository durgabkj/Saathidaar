package com.ottego.saathidaar.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.ottego.saathidaar.MemberGalleryShowFragment;
import com.ottego.saathidaar.ShowImageFragment;
import com.ottego.saathidaar.viewmodel.GalleryViewModel;

public class ViewPagerMemberGalleryAdapter extends FragmentStateAdapter {
    GalleryViewModel viewModel;
    public ViewPagerMemberGalleryAdapter(@NonNull FragmentActivity fragmentActivity, GalleryViewModel viewModel) {
        super(fragmentActivity);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("image_id", viewModel.list.getValue().get(position).image_id);

        return  MemberGalleryShowFragment.newInstance(viewModel.list.getValue().get(position).member_images, viewModel._list.getValue().get(position).image_id,viewModel._list.getValue().get(position).photo_status);
        // ShowImageFragment.newInstance(viewModel.list.getValue().get(position).member_images, viewModel._list.getValue().get(position).image_id);
    }

    @Override
    public int getItemCount() {
        return viewModel.list.getValue().size();
    }
}
