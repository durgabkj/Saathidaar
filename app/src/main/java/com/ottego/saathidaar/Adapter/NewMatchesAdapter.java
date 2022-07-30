package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.UpgradeOnButtonActivity;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {
    SessionManager sessionManager;
    Context context;
    List<NewMatchesModel> list;
    ApiListener clickListener;

    public NewMatchesAdapter(Context context, List<NewMatchesModel> list, ApiListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_my_matches, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewMatchesModel item = list.get(position);
//        Log.e(" New Matches model", new Gson().toJson(item));
        sessionManager = new SessionManager(context);
        holder.tvNewMatchName.setText(item.first_name + " " + item.last_name);
        holder.tvNewMatchAge.setText(item.mage);
        holder.tvNewMatchHeight.setText(item.religion);
        holder.tvNewMatchCity.setText(item.maritalStatus);
        holder.tvImageCount.setText(item.images_count);

        Glide.with(context)
                .load(Utils.imageUrl + item.profile_photo)
                .placeholder(sessionManager.getKeyGender().equalsIgnoreCase("male")?R.drawable.ic_no_image__female_:R.drawable.ic_no_image__male_)
                .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                .into(holder.ivUserMatch);




        holder.llPhotoMyMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MemberGalleryActivity.class);
                intent.putExtra("Member_id", item.member_id);
                context.startActivity(intent);
            }
        });


        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sentRequest(context, item.member_id);
                holder.ivLike.setVisibility(View.GONE);
                holder.llConnect.setVisibility(View.VISIBLE);

            }
        });

        holder.llShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.shortList(context, item.member_id);
                holder.llShortList.setVisibility(View.GONE);
                holder.llShortListRemove.setVisibility(View.VISIBLE);

            }
        });


        holder.llShortBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.move);
                holder.llItemAnimation.startAnimation(animation);
                Utils.blockMember(context, item.member_id, clickListener);
                holder.llShortBlock.setVisibility(View.GONE);
                holder.llBlocked.setVisibility(View.VISIBLE);
                holder.llShortList.setEnabled(false);
                holder.ivLike.setEnabled(false);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position", String.valueOf(position));
                MatchPagerFragment.newInstance(String.valueOf(position), String.valueOf(position)).show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");


            }
        });


        holder.tvPremiumContactMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                context.startActivity(intent);
            }
        });

        if (item.premium_status.equalsIgnoreCase("1")) {
            holder.flPremiumMatch.setVisibility(View.VISIBLE);
            holder.llPremiumMsgMatches.setVisibility(View.VISIBLE);
            holder.tvLevelPremiumMatch.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserMatch, ivNoImageMaleFemaleMatch;
        TextView tvNewMatchName, tvNewMatchAge,tvPremiumContactMatch, tvLevelPremiumMatch,tvNewMatchHeight, tvNewMatchCity, tvNewMatchWorkAs, tvImageCount;
        LinearLayout llMess, llShortListRemove, llShortList, llPhotoMyMatches, llShortBlock, llBlocked, llItemAnimation;
        LinearLayout ivLike, llConnect, llNo_imageFemaleList,llPremiumMsgMatches;
        FrameLayout flNoImageMaleFemaleList, flPremiumMatch;
        Spinner SpMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPremiumContactMatch=itemView.findViewById(R.id.tvPremiumContactMatch);
            tvNewMatchAge = itemView.findViewById(R.id.tvNewMatchAge);
            tvNewMatchName = itemView.findViewById(R.id.tvNewMatchName);
            tvNewMatchHeight = itemView.findViewById(R.id.tvNewMatchHeight);
            tvNewMatchCity = itemView.findViewById(R.id.tvNewMatchCity);
            tvNewMatchWorkAs = itemView.findViewById(R.id.tvNewMatchWorkAs);
            ivLike = itemView.findViewById(R.id.ivLike);
            llPhotoMyMatches = itemView.findViewById(R.id.llPhotoMyMatches);
            ivUserMatch = itemView.findViewById(R.id.ivUserMatch);
            llShortListRemove = itemView.findViewById(R.id.llShortListRemove);
            llShortList = itemView.findViewById(R.id.llShortList);
            llConnect = itemView.findViewById(R.id.llConnect);
            llShortBlock = itemView.findViewById(R.id.llShortBlock);
            llBlocked = itemView.findViewById(R.id.llBlocked);
            llNo_imageFemaleList = itemView.findViewById(R.id.llNo_imageFemaleList);
            flNoImageMaleFemaleList = itemView.findViewById(R.id.flNoImageMaleFemaleList);
            ivNoImageMaleFemaleMatch = itemView.findViewById(R.id.ivNoImageMaleFemaleMatch);
            tvImageCount = itemView.findViewById(R.id.tvImageCount);
            llItemAnimation = itemView.findViewById(R.id.llItemAnimation);
            flPremiumMatch = itemView.findViewById(R.id.flPremiumMatch);
            llPremiumMsgMatches=itemView.findViewById(R.id.llPremiumMsgMatches);
            tvLevelPremiumMatch=itemView.findViewById(R.id.tvLevelPremiumMatch);

        }
    }
}
