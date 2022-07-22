package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.DashBoardFragment;
import com.ottego.saathidaar.GalleryPagerFragment;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ShowImageFragment;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

        Context context;
        List<ImageModel> list;
        public ImageAdapter(Context context, List<ImageModel> list) {
            this.context = context;
            this.list = list;
        }
        @NonNull
        @Override
        public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_gride, parent, false);
            return new ImageAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            ImageModel item = list.get(position);
//        Log.e(" New Matches model", new Gson().toJson(item));

            Glide.with(context)
                    .load(Utils.imageUrl + item.member_images)
                    .into(holder.ivUserImage);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.e("position", String.valueOf(position));
                    GalleryPagerFragment.newInstance(String.valueOf(position),"").show(((FragmentActivity) context).getSupportFragmentManager(), "gallery_pager_fragment");



                }
            });


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivUserImage;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                ivUserImage = itemView.findViewById(R.id.ivUserImage);

            }
        }
    }
