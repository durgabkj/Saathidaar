package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.MemberGalleryPagerFragment;
import com.ottego.saathidaar.Model.ImageModel;
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

        if (!item.photo_status.equals("0")) {
            Glide.with(context)
                    .load(Utils.imageUrl + item.member_images)
                    .into(holder.ivUserImageMember);
        } else {
            holder.llGalleyBorder.setVisibility(View.GONE);
        }


//        if (item.photo_status.equals("0")) {
//            holder.tvImageApproveStatus.setVisibility(View.VISIBLE);
//        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (item.photo_status.equals("0")) {
                    Toast.makeText(context, "Photo Approval Pending", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("position", String.valueOf(position));
                    MemberGalleryPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "gallery_pager_fragment");

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return Math.min(list.size(), 2);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llGalleyBorder;
        ImageView ivUserImageMember;
        TextView tvImageApproveStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvImageApproveStatus = itemView.findViewById(R.id.tvImageMemberApproveStatus);
            ivUserImageMember = itemView.findViewById(R.id.ivUserImageMember);
            llGalleyBorder = itemView.findViewById(R.id.llGalleyBorder);
        }
    }
}
