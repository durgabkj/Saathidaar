package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.xml.sax.Parser;

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

        holder.llPrivatePhoto.setVisibility(View.GONE);
        Log.e(" New Matches model", new Gson().toJson(item));

        sessionManager = new SessionManager(context);
        if (item != null) {
            holder.tvInvNewMatchName.setText(item.first_name + " " + Utils.nullToBlank(item.last_name).charAt(0));

            if (item.mage.equalsIgnoreCase("null") && !item.mage.equalsIgnoreCase("")) {
                holder.tvInvNewMatchAge.setText("Age-Not Specified");
            } else {
                holder.tvInvNewMatchAge.setText(Utils.nullToBlank(item.mage) + "yrs");
            }

            holder.tvInvNewMatchHeight.setText(Utils.nullToBlank(item.religion));
            holder.tvInvNewMatchCity.setText(Utils.nullToBlank(item.maritalStatus));
            holder.tvInvNewMatchWorkAsAccept.setText(Utils.nullToBlank(item.city));
            holder.tvInvitationAccetMessage.setText(Utils.nullToBlank(item.request_message).trim());
            holder.tvImageCountAccept.setText(Utils.nullToBlank(item.images_count));
            holder.tvInvNewMatchState.setText(Utils.nullToBlank(item.state));

        }
        holder.llPrivatePhoto.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position Inbox", String.valueOf(position));
                InboxPagerFragment.newInstance(String.valueOf(position), String.valueOf(list.size())).show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

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
                Utils.deleteRequest(context, item.member_id, clickListener);
                holder.llDeleteAccet.setVisibility(View.GONE);
                holder.llDeletedAccept.setVisibility(View.VISIBLE);
            }
        });

        holder.llCancelAccet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.cancelRequest(context, item.member_id, clickListener);
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
            holder.llPremiumMsgAccept.setVisibility(View.GONE);
            holder.llPrivatePhoto.setVisibility(View.VISIBLE);
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


        holder.tvPremiumAcceptMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                context.startActivity(intent);
            }
        });


        //For free user...Apply Conditions
        if (item.my_premium_status.equalsIgnoreCase("0")) {
            holder.llDeleteAccet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Upgrade your profile to decline request", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });

            holder.llBlockAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Upgrade your profile to add this profile in Blocklist", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });
        }

        if (item.request_sent_from.equals("1")) {
            holder.llCancelAccet.setVisibility(View.VISIBLE);
        }else {
            holder.llDeleteAccet.setVisibility(View.VISIBLE);
        }


        if (item.first_name.equals("") && item.last_name.equalsIgnoreCase("")) {
            holder.ivAcceptDot.setVisibility(View.GONE);
        }

        if (item.height.equals("") || item.height.equalsIgnoreCase("null")) {
            holder.ivAcceptDot2.setVisibility(View.GONE);
        }

        if (item.maritalStatus.equals("")) {
            holder.ivAcceptDot2.setVisibility(View.GONE);
        }

        if (item.country.equals("")) {
            holder.ivAcceptDot2.setVisibility(View.GONE);
        }

        //Calling function

        holder.llCAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.my_premium_status.equalsIgnoreCase(item.phone_privacy)) {
                    String call = item.contact_number;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + call));
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Can't connect because user has decided to keep phone number private", Toast.LENGTH_LONG).show();
                }
            }
        });

//WhatsApp function

        holder.llWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.my_premium_status.equalsIgnoreCase(item.phone_privacy)) {
                    try {
                        String text = "Hello";// Replace with your message.


                        Log.e("contact",item.contact_number);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" +item.contact_number+ "&text=" + text));
                        context.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "It may be " +item.first_name+" don't have whatsApp", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Can't connect because user has decided to keep phone number private", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvNewMatchName,tvInvNewMatchState, tvPremiumAcceptMatch, tvInvNewMatchAge, tvPremiumDeleteMatch, tvInvitationAccetMessage, tvLevelPremiumAccept, tvImageCountAccept, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAsAccept, tvInvitationDate;
        LinearLayout llCAll, llWhatsApp,llCancelAccet, llItemAnimationAccept, llPhotoAccept, llPrivatePhoto, llMessageAccept, llBlockAccept, llBlockedAccept, llAcceptCallMsgDecline, llNo_imageFemaleListAccept, llDeleteAccet, llDeletedAccept, llPremiumMsgAccept;
        FrameLayout flNoImageMaleFemaleListAccept, flPremiumAccept;
        ImageView ivNoImageMaleFemaleAccept, ivProfileAcceptInvi, ivAcceptDot, ivAcceptDot1, ivAcceptDot2;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            llCancelAccet=itemView.findViewById(R.id.llCancelAccet);
            ivAcceptDot = itemView.findViewById(R.id.ivAcceptDot);
            ivAcceptDot1 = itemView.findViewById(R.id.ivAcceptDot1);
            ivAcceptDot2 = itemView.findViewById(R.id.ivAcceptDot2);
            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
            tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
            tvPremiumAcceptMatch = itemView.findViewById(R.id.tvPremiumAcceptMatch);
            tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
            tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
            tvInvNewMatchWorkAsAccept = itemView.findViewById(R.id.tvInvNewMatchWorkAsAccept);
            llCAll = itemView.findViewById(R.id.llCAll);
            llItemAnimationAccept = itemView.findViewById(R.id.llItemAnimationAccept);
            tvPremiumDeleteMatch = itemView.findViewById(R.id.tvPremiumDeleteMatch);
            tvInvitationAccetMessage = itemView.findViewById(R.id.tvInvitationAccetMessage);
            llPhotoAccept = itemView.findViewById(R.id.llPhotoAccept);
            llWhatsApp = itemView.findViewById(R.id.llWhatsApp);
            llBlockAccept = itemView.findViewById(R.id.llBlockAccept);
            llMessageAccept = itemView.findViewById(R.id.llMessageAccept);
            llBlockedAccept = itemView.findViewById(R.id.llBlockedAccept);
            llAcceptCallMsgDecline = itemView.findViewById(R.id.llAcceptCallMsgDecline);
            tvImageCountAccept = itemView.findViewById(R.id.tvImageCountAccept);
            tvInvNewMatchState = itemView.findViewById(R.id.tvInvNewMatchState);
            tvInvitationDate = itemView.findViewById(R.id.tvInvitationDate);
//            llNo_imageFemaleListAccept = itemView.findViewById(R.id.llNo_imageFemaleList);
//            flNoImageMaleFemaleListAccept = itemView.findViewById(R.id.flNoImageMaleFemaleList);
//            ivNoImageMaleFemaleAccept = itemView.findViewById(R.id.ivNoImageMaleFemaleMatch);
            ivProfileAcceptInvi = itemView.findViewById(R.id.ivProfileAcceptInvi);
            flPremiumAccept = itemView.findViewById(R.id.flPremiumAccept);
            llDeleteAccet = itemView.findViewById(R.id.llDeleteAccet);
            llDeletedAccept = itemView.findViewById(R.id.llDeletedAccept);
            llPremiumMsgAccept = itemView.findViewById(R.id.llPremiumMsgAccept);
            llPrivatePhoto = itemView.findViewById(R.id.llPrivateAcceptPhoto);
            tvLevelPremiumAccept = itemView.findViewById(R.id.tvLevelPremiumAccept);

        }
    }
}
