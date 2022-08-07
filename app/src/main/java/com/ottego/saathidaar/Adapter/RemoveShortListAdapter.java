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

import jp.wasabeef.glide.transformations.BlurTransformation;

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


        if(item.request_status!=null && !item.request_status.isEmpty())
        {
            holder.llConnectShortList.setVisibility(View.VISIBLE);
            holder.ivLikeShortList.setVisibility(View.GONE);
        }


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


        if (item.photo_privacy.equalsIgnoreCase("1")) {
            holder.llPhotoShortList.setEnabled(true);
         //   holder.flPremiumRemove_shortList.setVisibility(View.GONE);
            holder.llPremiumMsgRemoveShortlist.setVisibility(View.GONE);
           // holder.tvLevelPremiumRemoveShortList.setVisibility(View.GONE);

            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .centerCrop()
                    //  .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                    .into(holder.ivRemoveShortList);

        } else if (item.photo_privacy.equalsIgnoreCase("3")) {
            holder.llPhotoShortList.setEnabled(false);
          //  holder.flPremiumRemove_shortList.setVisibility(View.VISIBLE);
            holder.llPremiumMsgRemoveShortlist.setVisibility(View.VISIBLE);
           // holder.tvLevelPremiumRemoveShortList.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .centerCrop()
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .transform(new BlurTransformation(20, 8))
                    .into(holder.ivRemoveShortList);
        } else if (item.photo_privacy.equalsIgnoreCase(item.my_premium_status)) {
           // holder.flPremiumRemove_shortList.setVisibility(View.GONE);
            holder.llPhotoShortList.setEnabled(true);
            holder.llPremiumMsgRemoveShortlist.setVisibility(View.GONE);
          //  holder.tvLevelPremiumRemoveShortList.setVisibility(View.GONE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .centerCrop()
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .into(holder.ivRemoveShortList);
        } else {
            holder.llPhotoShortList.setEnabled(false);
         //   holder.flPremiumRemove_shortList.setVisibility(View.VISIBLE);
            holder.llPremiumMsgRemoveShortlist.setVisibility(View.VISIBLE);
          //  holder.tvLevelPremiumRemoveShortList.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .transform(new BlurTransformation(20, 8))
                    .into(holder.ivRemoveShortList);
        }


        if(item.premium_status.equalsIgnoreCase("1"))
        {
            holder.flPremiumRemove_shortList.setVisibility(View.VISIBLE);
            holder.tvLevelPremiumRemoveShortList.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivRemoveShortList, ivNoImageMaleFemaleRemoveShortList;
        TextView tvNewMatchNameRs,tvLevelPremiumRemoveShortList, tvNewMatchAgeRs, tvNewMatchHeightRs, tvNewMatchCityRs, tvNewMatchWorkAsRs, tvImageCountRemoveShortList;
        LinearLayout llMess, llShortListRemove1, llShortList1, llPhotoShortList, llNo_imageFemaleRemoveShortList;
        LinearLayout ivLikeShortList, llBlockShortList, llPremiumMsgRemoveShortlist,llBlockedShortList, llConnectShortList;
        FrameLayout flNoImageMaleFemaleRemoveShortList,flPremiumRemove_shortList;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvLevelPremiumRemoveShortList = itemView.findViewById(R.id.tvLevelPremiumRemoveShortList);
            flPremiumRemove_shortList = itemView.findViewById(R.id.flPremiumRemove_shortList);
            llPremiumMsgRemoveShortlist=itemView.findViewById(R.id.llPremiumMsgRemoveShortlist);
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
