package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.UpgradeOnButtonActivity;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class SentInvitationAdapter extends RecyclerView.Adapter<SentInvitationAdapter.ViewHolder>{
        SessionManager sessionManager;
        private AdapterView.OnItemClickListener onItemClickListener;
        Context context;
        List<InboxModel> list;
        ApiListener clickListener;
        public SentInvitationAdapter(Context context, List<InboxModel> list,    ApiListener clickListener) {
            this.context = context;
            this.list = list;
            this.clickListener=clickListener;
        }

        @NonNull
        @Override
        public SentInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sent_item, parent, false);
            return new SentInvitationAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull SentInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            InboxModel item = list.get(position);
            Log.e(" Inbox model", new Gson().toJson(item));
            sessionManager=new SessionManager(context);
            holder.tvInvNewMatchName.setText(Utils.nullToBlank(item.first_name) + " " +  Utils.nullToBlank(item.last_name).charAt(0));
            holder.tvInvNewMatchAge.setText(Utils.nullToBlank(item.mage+" yrs"));
            holder.tvInvNewMatchHeight.setText(Utils.nullToBlank(item.religion));
            holder.tvInvNewMatchCity.setText(Utils.nullToBlank(item.maritalStatus));
            holder.tvInvNewMatchWorkAs.setText(Utils.nullToBlank(item.country));
            holder.tvImageCountSent.setText(Utils.nullToBlank(item.images_count));

//            holder.llAccept.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Utils.acceptRequest(context, item.member_id);
//                    holder.llAccept.setVisibility(View.GONE);
//                    holder.llAccepted.setVisibility(View.VISIBLE);
//                    holder.llDelete.setVisibility(View.GONE);
//                }
//            });
holder.llPrivateSentPhoto.setVisibility(View.GONE);

            holder.llDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.deleteRequest(context, item.member_id,clickListener);
                    holder.llDelete.setVisibility(View.GONE);
                    holder.llDeleted.setVisibility(View.VISIBLE);

                }
            });


            holder.llBlockSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id,clickListener);
                    holder.llBlockSent.setVisibility(View.GONE);
                    holder.llBlockedSent.setVisibility(View.VISIBLE);

                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position  sent Inbox", String.valueOf(position));
                    InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

                }
            });


            holder.llPhotoSent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(), MemberGalleryActivity.class);
                    intent.putExtra("Member_id", item.member_id);
                    context.startActivity(intent);
                }
            });

//            Glide.with(context)
//                    .load(Utils.imageUrl + item.profile_photo)
//                    .placeholder(sessionManager.getKeyGender().equalsIgnoreCase("male")?R.drawable.ic_no_image__female_:R.drawable.ic_no_image__male_)
//                    .transform(item.premium_status.equalsIgnoreCase("1")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
//                    .into(holder.ivSentInvitation);


            if (item.photo_privacy.equalsIgnoreCase("1")) {
                holder.llPhotoSent.setEnabled(true);
                //  holder.flPremiumSent.setVisibility(View.GONE);
                holder.llPremiumMsgSent.setVisibility(View.GONE);
                //  holder.tvLevelPremiumSent.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        //  .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                        .into(holder.ivSentInvitation);

            }
            else if (item.photo_privacy.equalsIgnoreCase("3") && (item.premium_status.equalsIgnoreCase("0"))) {
                holder.llPhotoSent.setEnabled(false);
                // holder.flPremiumMatch.setVisibility(View.VISIBLE);
                holder.llPremiumMsgSent.setVisibility(View.GONE);
                holder.llPrivateSentPhoto.setVisibility(View.VISIBLE);
                // holder.tvLevelPremiumMatch.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(holder.ivSentInvitation);




            } else if (item.photo_privacy.equalsIgnoreCase("3")) {
                holder.llPhotoSent.setEnabled(false);
                // holder.flPremiumSent.setVisibility(View.VISIBLE);
                holder.llPremiumMsgSent.setVisibility(View.VISIBLE);
                //   holder.tvLevelPremiumSent.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(holder.ivSentInvitation);

            } else if (item.photo_privacy.equalsIgnoreCase(item.my_premium_status)) {
                //  holder.flPremiumSent.setVisibility(View.GONE);
                holder.llPremiumMsgSent.setVisibility(View.GONE);
                holder.llPhotoSent.setEnabled(true);
                // holder.tvLevelPremiumSent.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .into(holder.ivSentInvitation);
            } else {
                holder.llPhotoSent.setEnabled(false);
                // holder.flPremiumSent.setVisibility(View.VISIBLE);
                holder.llPremiumMsgSent.setVisibility(View.VISIBLE);
                // holder.tvLevelPremiumSent.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .transform(new BlurTransformation(20, 8))
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .into(holder.ivSentInvitation);

            }


            if((item.request_message!= null  && !item.request_message.isEmpty())) {
                holder.tvInvitationMessageSent.setText(item.request_message);
                // holder.tvInvitationDateInbox.setText(item.request_status_date);

            }else {
                holder.llMsgDateSent.setVisibility(View.GONE);
            }

            if ((item.premium_status.equalsIgnoreCase("1"))) {
                holder.tvLevelPremiumSent.setVisibility(View.VISIBLE);
                holder.flPremiumSent.setVisibility(View.VISIBLE);
            }


            holder.tvPremiumSentMatch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });

        }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvNewMatchName, tvPremiumSentMatch, tvInvNewMatchAge, tvInvNewMatchHeight, tvLevelPremiumSent, tvInvNewMatchCity, tvInvNewMatchWorkAs, tvInvitationMessageSent, tvImageCountSent;
        LinearLayout llAccept, llDelete, llAccepted, llPrivateSentPhoto, llDeleted, llPremiumMsgSent, llPhotoSent, llBlockSent, llBlockedSent, llNo_imageFemaleSentInvitation, llMsgDateSent;
        ImageView ivNoImageMaleFemaleSentInvitation, ivSentInvitation;
        FrameLayout flNoImageMaleFemaleSentInvitation, flPremiumSent;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            llPrivateSentPhoto = itemView.findViewById(R.id.llPrivateSentPhoto);
            tvLevelPremiumSent = itemView.findViewById(R.id.tvLevelPremiumSent);
            flPremiumSent = itemView.findViewById(R.id.flPremiumSent);
            llPremiumMsgSent = itemView.findViewById(R.id.llPremiumMsgSent);
            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
                tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
                tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
                tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
                tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
                llAccepted = itemView.findViewById(R.id.llAccepted);
                llAccept = itemView.findViewById(R.id.llAccept);
            llDelete = itemView.findViewById(R.id.llDelete);
            tvPremiumSentMatch = itemView.findViewById(R.id.tvPremiumSentMatch);
            llDeleted = itemView.findViewById(R.id.llDeleted);
                llPhotoSent=itemView.findViewById(R.id.llPhotoSent);
                llBlockSent=itemView.findViewById(R.id.llBlockSent);
                llBlockedSent=itemView.findViewById(R.id.llBlockedSent);
                llMsgDateSent=itemView.findViewById(R.id.llMsgDateSent);
                tvInvitationMessageSent=itemView.findViewById(R.id.tvInvitationMessageSent);
                llNo_imageFemaleSentInvitation = itemView.findViewById(R.id.llNo_imageFemaleSentInvitation);
                ivNoImageMaleFemaleSentInvitation=itemView.findViewById(R.id.ivNoImageMaleFemaleSentInvitation);
                ivSentInvitation=itemView.findViewById(R.id.ivSentInvitation);
                flNoImageMaleFemaleSentInvitation=itemView.findViewById(R.id.flNoImageMaleFemaleSentInvitation);
                tvImageCountSent=itemView.findViewById(R.id.tvImageCountSent);
            }
        }
    }

