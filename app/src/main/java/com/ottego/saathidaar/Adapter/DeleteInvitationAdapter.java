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
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;
    public class DeleteInvitationAdapter extends RecyclerView.Adapter<DeleteInvitationAdapter.ViewHolder>{
        SessionManager sessionManager;
        Context context;
        List<InboxModel> list;

        public DeleteInvitationAdapter(Context context, List<InboxModel> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public DeleteInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_delete_invitation, parent, false);
            return new DeleteInvitationAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull DeleteInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            InboxModel item = list.get(position);
            Log.e(" Inbox model", new Gson().toJson(item));

sessionManager=new SessionManager(context);
            holder.tvInvDeleteName.setText(item.first_name + " " + item.last_name);
            holder.tvInvDeleteAge.setText(item.mage);
            holder.tvInvDeleteHeight.setText(item.religion);
            holder.tvInvDeleteCity.setText(item.maritalStatus);
            holder.tvInvDeleteWorkAs.setText(item.country);

            holder.llBlockDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id);
                    holder.llBlockDelete.setVisibility(View.GONE);
                    holder.llBlocked.setVisibility(View.VISIBLE);
                    holder.llDelete1.setVisibility(View.GONE);
                }
            });


            holder.llDelete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.deleteRequest(context, item.member_id);
                    //  holder.llAccept.setVisibility(View.GONE);
                    holder.llDelete1.setVisibility(View.GONE);
                    holder.llDeletedInvitation.setVisibility(View.VISIBLE);

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position Inbox", String.valueOf(position));
                    InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

                }
            });

            holder.llPhotoDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), MemberGalleryActivity.class);
                    intent.putExtra("Member_id", item.member_id);
                    context.startActivity(intent);
                }
            });


            if (!(!item.profile_photo.isEmpty()) && !(item.profile_photo != null)) {
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .into(holder.ivDeleteInvitation);

            } else {
                if (sessionManager.getKeyGender().equalsIgnoreCase("male")) {
                    holder.llNo_imageFemaleListDeleteInvi.setVisibility(View.VISIBLE);
                    holder.flNoImageMaleFemaleListDeleteInvi.setVisibility(View.VISIBLE);
                    holder.ivDeleteInvitation.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__female_)
                            .into(holder.ivNoImageMaleFemaleDeleteInvi);

                } else {
                    holder.llNo_imageFemaleListDeleteInvi.setVisibility(View.VISIBLE);
                    holder.flNoImageMaleFemaleListDeleteInvi.setVisibility(View.VISIBLE);
                    holder.ivDeleteInvitation.setVisibility(View.GONE);

                    Glide.with(context)
                            .load(R.drawable.ic_no_image__male_)
                            .into(holder.ivDeleteInvitation);

                }


            }


        }


        @Override
        public int getItemCount() {
            return list.size();
        }



        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInvDeleteWorkAs, tvInvDeleteCity, tvInvDeleteHeight, tvInvDeleteName, tvInvDeleteAge;
            LinearLayout llBlocked,llBlockDelete,llDeletedInvitation,llDelete1,llPhotoDelete,llNo_imageFemaleListDeleteInvi;
            ImageView ivDeleteInvitation,ivNoImageMaleFemaleDeleteInvi;
            FrameLayout flNoImageMaleFemaleListDeleteInvi;

            public ViewHolder(@NonNull View itemView) {

                super(itemView);
                tvInvDeleteAge = itemView.findViewById(R.id.tvInvDeleteAge);
                tvInvDeleteName = itemView.findViewById(R.id.tvInvDeleteName);
                tvInvDeleteHeight = itemView.findViewById(R.id.tvInvDeleteHeight);
                tvInvDeleteCity = itemView.findViewById(R.id.tvInvDeleteCity);
                tvInvDeleteWorkAs = itemView.findViewById(R.id.tvInvDeleteWorkAs);
                llBlocked = itemView.findViewById(R.id.llBlocked);
                llBlockDelete = itemView.findViewById(R.id.llBlockDelete);
                llDelete1 = itemView.findViewById(R.id.llDelete1);
                llDeletedInvitation = itemView.findViewById(R.id.llDeletedInvitation);
                llPhotoDelete=itemView.findViewById(R.id.llPhotoDelete);

                llNo_imageFemaleListDeleteInvi = itemView.findViewById(R.id.llNo_imageFemaleListDeleteInvi);
                ivDeleteInvitation = itemView.findViewById(R.id.ivDeleteInvitation);
                ivNoImageMaleFemaleDeleteInvi = itemView.findViewById(R.id.ivNoImageMaleFemaleDeleteInvi);
                flNoImageMaleFemaleListDeleteInvi=itemView.findViewById(R.id.flNoImageMaleFemaleListDeleteInvi);

            }
        }
    }


