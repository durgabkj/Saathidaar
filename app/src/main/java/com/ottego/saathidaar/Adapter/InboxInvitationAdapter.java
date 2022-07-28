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

public class InboxInvitationAdapter extends RecyclerView.Adapter<InboxInvitationAdapter.ViewHolder>{
    SessionManager sessionManager;
    Context context;
    List<InboxModel> list;
    ApiListener clickListener;
    public InboxInvitationAdapter(Context context, List<InboxModel> list,    ApiListener clickListener) {
        this.context = context;
        this.list = list;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public InboxInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invitation_inbox, parent, false);
        return new InboxInvitationAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull InboxInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InboxModel item = list.get(position);
        Log.e(" Inbox model", new Gson().toJson(item));
        sessionManager = new SessionManager(context);
        holder.tvInvNewMatchName.setText(item.first_name + " " + item.last_name);
        holder.tvInvNewMatchAge.setText(item.mage+" yrs");
        holder.tvInvNewMatchHeight.setText(item.religion);
        holder.tvInvNewMatchCity.setText(item.maritalStatus);
        holder.tvInvNewMatchWorkAs.setText(item.country);
        holder.tvImageCountInvitation.setText(item.images_count);



        holder.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.acceptRequest(context, item.member_id);
                holder.llAccept.setVisibility(View.GONE);
                holder.llAccepted.setVisibility(View.VISIBLE);
                holder.llDelete.setVisibility(View.GONE);
            }
        });


        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.deleteRequest(context, item.member_id);
                holder.llAccept.setVisibility(View.GONE);
                holder.llDelete.setVisibility(View.GONE);
                holder.llDeleted.setVisibility(View.VISIBLE);

            }
        });


        holder.llBlockInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.blockMember(context, item.member_id,clickListener);
                holder.llBlockInvitation.setVisibility(View.GONE);
                holder.llBlockedInvitation.setVisibility(View.VISIBLE);
                holder.llAcceptDelete.setVisibility(View.GONE);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position Inbox", String.valueOf(position));
                InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

            }
        });


        holder.llPhotoInvitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MemberGalleryActivity.class);
                intent.putExtra("Member_id", item.member_id);
                context.startActivity(intent);
            }
        });


        if (item.profile_photo != null && !item.profile_photo.isEmpty() && item.premium_status.equalsIgnoreCase(("1"))) {
            // For Premium member
            Glide.with(context).load(Utils.imageUrl + item.profile_photo)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new BlurTransformation(20, 8))
                    .into(holder.ivReceivedInvitation);
        }


        if (item.profile_photo != null && !item.profile_photo.isEmpty()) {
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .into(holder.ivReceivedInvitation);

        } else {
            if (sessionManager.getKeyGender().equalsIgnoreCase("male")) {
                holder.llNo_imageFemaleListReceivedInvitation.setVisibility(View.VISIBLE);
                holder.flNoImageMaleFemaleListReceivedInvitation.setVisibility(View.VISIBLE);
                holder.ivReceivedInvitation.setVisibility(View.GONE);
                Glide.with(context)
                        .load(R.drawable.ic_no_image__female_)
                        .into(holder.ivNoImageMaleFemaleReceivedInvitation);

            } else {
                holder.llNo_imageFemaleListReceivedInvitation.setVisibility(View.VISIBLE);
                holder.flNoImageMaleFemaleListReceivedInvitation.setVisibility(View.VISIBLE);
                holder.ivReceivedInvitation.setVisibility(View.GONE);

                Glide.with(context)
                        .load(R.drawable.ic_no_image__male_)
                        .into(holder.ivReceivedInvitation);

            }

        }

        if((item.request_message!= null || item.request_status_date!= null) && (!item.request_message.isEmpty() || !item.request_status_date.isEmpty()) )
        {
            holder.tvInvitationMessageInbox.setText(item.request_message);
           // holder.tvInvitationDateInbox.setText(item.request_status_date);

        }else {
            holder.llMsgDate.setVisibility(View.GONE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        FrameLayout flNoImageMaleFemaleListReceivedInvitation;
        ImageView ivNoImageMaleFemaleReceivedInvitation, ivReceivedInvitation;
        TextView tvInvNewMatchName,tvImageCountInvitation, tvInvNewMatchAge, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAs, tvInvitationMessageInbox, tvInvitationDateInbox;
        LinearLayout llNo_imageFemaleListReceivedInvitation, llAccept, llDelete, llAccepted, llDeleted, llPhotoInvitation, llBlockInvitation, llBlockedInvitation, llAcceptDelete,llMsgDate;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
            tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
            tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
            tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
            tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
            llAccepted = itemView.findViewById(R.id.llAccepted);
            llAccept = itemView.findViewById(R.id.llAccept);
            llDelete = itemView.findViewById(R.id.llDelete);
            llDeleted = itemView.findViewById(R.id.llDeleted);
            llPhotoInvitation = itemView.findViewById(R.id.llPhotoInvitation);
            llBlockInvitation = itemView.findViewById(R.id.llBlockInvitation);
            llBlockedInvitation = itemView.findViewById(R.id.llBlockedInvitation);
            llAcceptDelete = itemView.findViewById(R.id.llAcceptDelete);
            tvImageCountInvitation=itemView.findViewById(R.id.tvImageCountInvitation);

            tvInvitationMessageInbox = itemView.findViewById(R.id.tvInvitationMessageInbox);
            tvInvitationDateInbox = itemView.findViewById(R.id.tvInvitationDateInbox);
            llMsgDate=itemView.findViewById(R.id.llMsgDate);

            llNo_imageFemaleListReceivedInvitation = itemView.findViewById(R.id.llNo_imageFemaleListReceivedInvitation);
            flNoImageMaleFemaleListReceivedInvitation = itemView.findViewById(R.id.flNoImageMaleFemaleListReceivedInvitation);
            ivNoImageMaleFemaleReceivedInvitation = itemView.findViewById(R.id.ivNoImageMaleFemaleReceivedInvitation);
            ivReceivedInvitation = itemView.findViewById(R.id.ivReceivedInvitation);

        }
    }
}
