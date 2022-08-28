package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.GalleryPagerFragment;
import com.ottego.saathidaar.MemberGalleryPagerFragment;
import com.ottego.saathidaar.Model.GalleryModel;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class MemberImageGalleryAdapter extends RecyclerView.Adapter<MemberImageGalleryAdapter.ViewHolder> {
    Context context;
    List<ImageModel> list;
    public MemberImageGalleryAdapter(Context context, List<ImageModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MemberImageGalleryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_gride, parent, false);
        return new MemberImageGalleryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MemberImageGalleryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ImageModel item = list.get(position);
//        Log.e(" New Matches model", new Gson().toJson(item));

        Glide.with(context)
                .load(Utils.imageUrl + item.member_images)
                .into(holder.ivUserImageMember);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("position", String.valueOf(position));
                MemberGalleryPagerFragment.newInstance(String.valueOf(position),"").show(((FragmentActivity) context).getSupportFragmentManager(), "gallery_pager_fragment");



            }
        });


    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 2);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

ImageView ivUserImageMember;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivUserImageMember=itemView.findViewById(R.id.ivUserImageMember);
        }
    }
}
