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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.gson.Gson;
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class AcceptInvitationAdapter extends RecyclerView.Adapter<AcceptInvitationAdapter.ViewHolder> {

    SessionManager sessionManager;
    Context context;
    List<InboxModel> list;
    ApiListener clickListener;

    public AcceptInvitationAdapter(Context context, List<InboxModel> list, ApiListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AcceptInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_accepted, parent, false);
        return new AcceptInvitationAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InboxModel item = list.get(position);
        Log.e(" New Matches model", new Gson().toJson(item));

        sessionManager = new SessionManager(context);
if(item!=null)
{
    holder.tvInvNewMatchName.setText(item.first_name + " " + Utils.nullToBlank(item.last_name).charAt(0));
    holder.tvInvNewMatchAge.setText(Utils.nullToBlank(item.mage));
    holder.tvInvNewMatchHeight.setText(Utils.nullToBlank(item.religion));
    holder.tvInvNewMatchCity.setText(Utils.nullToBlank(item.maritalStatus));
    holder.tvInvNewMatchWorkAsAccept.setText(Utils.nullToBlank(item.country));
    holder.tvInvitationAccetMessage.setText(Utils.nullToBlank(item.request_message));
    holder.tvImageCountAccept.setText(Utils.nullToBlank(item.images_count));

}


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position Inbox", String.valueOf(position));
                InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

            }
        });

        holder.llPhotoAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MemberGalleryActivity.class);
                intent.putExtra("Member_id", item.member_id);
                context.startActivity(intent);
            }
        });


        holder.llDeleteAccet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.deleteRequest(context, item.member_id,clickListener);
                holder.llDeleteAccet.setVisibility(View.GONE);
                holder.llDeletedAccept.setVisibility(View.VISIBLE);
            }
        });


        holder.llBlockAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.blockMember(context, item.member_id, clickListener);
                holder.llBlockAccept.setVisibility(View.GONE);
                holder.llBlockedAccept.setVisibility(View.VISIBLE);
                holder.llAcceptCallMsgDecline.setVisibility(View.GONE);
            }
        });


//        Glide.with(context)
//                .load(Utils.imageUrl + item.profile_photo)
//                .placeholder(sessionManager.getKeyGender().equalsIgnoreCase("male")?R.drawable.ic_no_image__female_:R.drawable.ic_no_image__male_)
//                .transform(item.premium_status.equalsIgnoreCase("1")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
//                .into(holder.ivProfileAcceptInvi);

if(item!=null){
    if (item.photo_privacy.equalsIgnoreCase("1")) {
        holder.llPhotoAccept.setEnabled(true);
        //  holder.flPremiumAccept.setVisibility(View.GONE);
        holder.llPremiumMsgAccept.setVisibility(View.GONE);
        //  holder.tvLevelPremiumAccept.setVisibility(View.GONE);

        Glide.with(context)
                .load(Utils.imageUrl + item.profile_photo)
                .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                //  .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                .into(holder.ivProfileAcceptInvi);

    } else if (item.photo_privacy.equalsIgnoreCase("3")) {
        holder.llPhotoAccept.setEnabled(false);
        //   holder.flPremiumAccept.setVisibility(View.VISIBLE);
        holder.llPremiumMsgAccept.setVisibility(View.VISIBLE);
        // holder.tvLevelPremiumAccept.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(Utils.imageUrl + item.profile_photo)
                .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                .transform(new BlurTransformation(20, 8))
                .into(holder.ivProfileAcceptInvi);
    } else if (item.photo_privacy.equalsIgnoreCase(item.my_premium_status)) {
        //  holder.flPremiumAccept.setVisibility(View.GONE);
        holder.llPremiumMsgAccept.setVisibility(View.GONE);
        holder.llPhotoAccept.setEnabled(true);
        //  holder.tvLevelPremiumAccept.setVisibility(View.GONE);
        Glide.with(context)
                .load(Utils.imageUrl + item.profile_photo)
                .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                .into(holder.ivProfileAcceptInvi);
    } else {
        holder.llPhotoAccept.setEnabled(false);
        //  holder.flPremiumAccept.setVisibility(View.VISIBLE);
        holder.llPremiumMsgAccept.setVisibility(View.VISIBLE);
        // holder.tvLevelPremiumAccept.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(Utils.imageUrl + item.profile_photo)
                .transform(new BlurTransformation(20, 8))
                .into(holder.ivProfileAcceptInvi);
    }

    if (item.premium_status.equalsIgnoreCase("1")) {
        holder.flPremiumAccept.setVisibility(View.VISIBLE);
        holder.tvLevelPremiumAccept.setVisibility(View.VISIBLE);

    }


    if (item.request_message != null && !item.request_message.isEmpty()) {
        holder.tvInvitationAccetMessage.setText(item.request_message);
        // holder.tvInvitationDateInbox.setText(item.request_status_date);

    } else {
        holder.llMessageAccept.setVisibility(View.GONE);
    }

}

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvNewMatchName, tvInvNewMatchAge, tvInvitationAccetMessage, tvLevelPremiumAccept, tvImageCountAccept, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAsAccept, tvInvitationDate;
        LinearLayout llCAll, llWhatsApp, llPhotoAccept, llMessageAccept, llBlockAccept, llBlockedAccept, llAcceptCallMsgDecline, llNo_imageFemaleListAccept, llDeleteAccet, llDeletedAccept, llPremiumMsgAccept;
        FrameLayout flNoImageMaleFemaleListAccept, flPremiumAccept;
        ImageView ivNoImageMaleFemaleAccept, ivProfileAcceptInvi;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
            tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
            tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
            tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
            tvInvNewMatchWorkAsAccept = itemView.findViewById(R.id.tvInvNewMatchWorkAsAccept);
            llCAll = itemView.findViewById(R.id.llCAll);
            tvInvitationAccetMessage = itemView.findViewById(R.id.tvInvitationAccetMessage);
            llPhotoAccept = itemView.findViewById(R.id.llPhotoAccept);
            llWhatsApp = itemView.findViewById(R.id.llWhatsApp);
            llBlockAccept = itemView.findViewById(R.id.llBlockAccept);
            llMessageAccept = itemView.findViewById(R.id.llMessageAccept);
            llBlockedAccept = itemView.findViewById(R.id.llBlockedAccept);
            llAcceptCallMsgDecline = itemView.findViewById(R.id.llAcceptCallMsgDecline);
            tvImageCountAccept = itemView.findViewById(R.id.tvImageCountAccept);
           // tvInvitationAccetMessage = itemView.findViewById(R.id.tvInvitationAccetMessage);
            tvInvitationDate = itemView.findViewById(R.id.tvInvitationDate);
            llNo_imageFemaleListAccept = itemView.findViewById(R.id.llNo_imageFemaleList);
            flNoImageMaleFemaleListAccept = itemView.findViewById(R.id.flNoImageMaleFemaleList);
            ivNoImageMaleFemaleAccept = itemView.findViewById(R.id.ivNoImageMaleFemaleMatch);
            ivProfileAcceptInvi = itemView.findViewById(R.id.ivProfileAcceptInvi);
            flPremiumAccept = itemView.findViewById(R.id.flPremiumAccept);
            llDeleteAccet = itemView.findViewById(R.id.llDeleteAccet);
            llDeletedAccept = itemView.findViewById(R.id.llDeletedAccept);
            llPremiumMsgAccept = itemView.findViewById(R.id.llPremiumMsgAccept);
            tvLevelPremiumAccept = itemView.findViewById(R.id.tvLevelPremiumAccept);


        }
    }
}
