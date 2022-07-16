package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

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
