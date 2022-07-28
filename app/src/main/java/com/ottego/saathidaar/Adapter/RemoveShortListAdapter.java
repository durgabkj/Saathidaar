package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class RemoveShortListAdapter extends RecyclerView.Adapter<RemoveShortListAdapter.ViewHolder> {

    SessionManager sessionManager;
    Context context;
    List<NewMatchesModel> list;
    ApiListener clickListener;

    public RemoveShortListAdapter(Context context, List<NewMatchesModel> list, ApiListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public RemoveShortListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_remove_shortlist, parent, false);
        return new RemoveShortListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RemoveShortListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewMatchesModel item = list.get(position);
        Log.e(" New Matches model", new Gson().toJson(item));
        sessionManager = new SessionManager(context);

        holder.tvNewMatchNameRs.setText(item.first_name + " " + item.last_name);
        holder.tvNewMatchAgeRs.setText(item.mage + " Yrs");
        holder.tvNewMatchHeightRs.setText(item.religion);
        holder.tvNewMatchCityRs.setText(item.maritalStatus);
        holder.tvImageCountRemoveShortList.setText(item.images_count);

        holder.ivLikeShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sentRequest(context, item.member_id);
                holder.ivLikeShortList.setVisibility(View.GONE);
                holder.llConnectShortList.setVisibility(View.VISIBLE);

            }
        });

        holder.llShortListRemove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShortList(context, item.member_id, clickListener);
                holder.llShortList1.setVisibility(View.VISIBLE);
                holder.llShortListRemove1.setVisibility(View.GONE);
            }
        });


        holder.llBlockShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.blockMember(context, item.member_id, clickListener);
                holder.llBlockShortList.setVisibility(View.GONE);
                holder.llBlockedShortList.setVisibility(View.VISIBLE);
                holder.ivLikeShortList.setVisibility(View.GONE);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position", String.valueOf(position));
                MatchPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");

            }
        });
        holder.llPhotoShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MemberGalleryActivity.class);
                intent.putExtra("Member_id", item.member_id);
                context.startActivity(intent);
            }
        });


//        if (item.profile_photo != null && !item.profile_photo.isEmpty() && item.premium_status.equalsIgnoreCase(("1"))) {
//            // For Premium member
//            Glide.with(context).load(Utils.imageUrl + item.profile_photo)
//                    .transition(DrawableTransitionOptions.withCrossFade())
//                    .placeholder(new ColorDrawable(Color.BLACK))
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .transform(new BlurTransformation(20, 8))
//                    .into(holder.ivRemoveShortList);
//        }


        if (item.profile_photo != null && !item.profile_photo.isEmpty()) {
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .into(holder.ivRemoveShortList);

        } else {
            if (sessionManager.getKeyGender().equalsIgnoreCase("male")) {
                holder.llNo_imageFemaleRemoveShortList.setVisibility(View.VISIBLE);
                holder.flNoImageMaleFemaleRemoveShortList.setVisibility(View.VISIBLE);
                holder.ivRemoveShortList.setVisibility(View.GONE);
                Glide.with(context)
                        .load(R.drawable.ic_no_image__female_)
                        .into(holder.ivNoImageMaleFemaleRemoveShortList);

            } else {
                holder.llNo_imageFemaleRemoveShortList.setVisibility(View.VISIBLE);
                holder.flNoImageMaleFemaleRemoveShortList.setVisibility(View.VISIBLE);
                holder.ivRemoveShortList.setVisibility(View.GONE);

                Glide.with(context)
                        .load(R.drawable.ic_no_image__male_)
                        .into(holder.ivRemoveShortList);
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRemoveShortList, ivNoImageMaleFemaleRemoveShortList;
        TextView tvNewMatchNameRs, tvNewMatchAgeRs, tvNewMatchHeightRs, tvNewMatchCityRs, tvNewMatchWorkAsRs, tvImageCountRemoveShortList;
        LinearLayout llMess, llShortListRemove1, llShortList1, llPhotoShortList, llNo_imageFemaleRemoveShortList;
        LinearLayout ivLikeShortList, llBlockShortList, llBlockedShortList, llConnectShortList;
        FrameLayout flNoImageMaleFemaleRemoveShortList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNewMatchAgeRs = itemView.findViewById(R.id.tvNewMatchAgeRs);
            tvNewMatchNameRs = itemView.findViewById(R.id.tvNewMatchNameRs);
            tvNewMatchHeightRs = itemView.findViewById(R.id.tvNewMatchHeightRs);
            tvNewMatchCityRs = itemView.findViewById(R.id.tvNewMatchCityRs);
            tvNewMatchWorkAsRs = itemView.findViewById(R.id.tvNewMatchWorkAsRs);
            ivLikeShortList = itemView.findViewById(R.id.ivLikeShortList);
            llPhotoShortList = itemView.findViewById(R.id.llPhotoShortList);
            llShortListRemove1 = itemView.findViewById(R.id.llShortListRemove1);
            llShortList1 = itemView.findViewById(R.id.llShortList1);
            llBlockedShortList = itemView.findViewById(R.id.llBlockedShortList);
            llBlockShortList = itemView.findViewById(R.id.llBlockShortList);
            llConnectShortList = itemView.findViewById(R.id.llConnectShortList);
            tvImageCountRemoveShortList = itemView.findViewById(R.id.tvImageCountRemoveShortList);
            ivRemoveShortList = itemView.findViewById(R.id.ivRemoveShortList);
            ivNoImageMaleFemaleRemoveShortList = itemView.findViewById(R.id.ivNoImageMaleFemaleRemoveShortList);
            llNo_imageFemaleRemoveShortList = itemView.findViewById(R.id.llNo_imageFemaleRemoveShortList);
            flNoImageMaleFemaleRemoveShortList = itemView.findViewById(R.id.flNoImageMaleFemaleRemoveShortList);

        }
    }
}
