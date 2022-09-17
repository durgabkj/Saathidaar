package com.ottego.saathidaar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.Model.AdvertisementModel;
import com.ottego.saathidaar.Model.SliderModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementAdapter extends SliderViewAdapter<AdvertisementAdapter.SliderAdapterViewHolder> {

        // list for storing urls of images.
        private final List<AdvertisementModel> mSliderItems;

        // Constructor
        public AdvertisementAdapter(Context context, ArrayList<AdvertisementModel> sliderDataArrayList) {
            this.mSliderItems = sliderDataArrayList;
        }

        // We are inflating the slider_layout
        // inside on Create View Holder method.
        @Override
        public AdvertisementAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_slider, null);
            return new AdvertisementAdapter.SliderAdapterViewHolder(inflate);
        }

        // Inside on bind view holder we will
        // set data to item of Slider View.
        @Override
        public void onBindViewHolder(AdvertisementAdapter.SliderAdapterViewHolder viewHolder, final int position) {

            final AdvertisementModel advertisementModel = mSliderItems.get(position);

            // Glide is use to load image
            // from url in your imageview.
            Glide.with(viewHolder.itemView)
                    .load(Utils.imageUrl + advertisementModel.member_images)
                    .fitCenter()
                    .into(viewHolder.imageViewBackground);
        }

        // this method will return
        // the count of our list.
        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
            // Adapter class for initializing
            // the views of our slider view.
            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterViewHolder(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.myimage);
                this.itemView = itemView;
            }
        }
    }


