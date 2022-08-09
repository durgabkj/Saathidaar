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
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;


public class RecentVisitorAdapter extends RecyclerView.Adapter<RecentVisitorAdapter.ViewHolder>{
    SessionManager sessionManager;
        Context context;
        List<NewMatchesModel> list;
    ApiListener clickListener;
        public RecentVisitorAdapter(Context context, List<NewMatchesModel> list, ApiListener clickListener) {
            this.context = context;
            this.list = list;
            this.clickListener=clickListener;

        }


        @NonNull
        @Override
        public RecentVisitorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentlyview, parent, false);
            return new RecentVisitorAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull RecentVisitorAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NewMatchesModel item = list.get(position);
            Log.e(" New Matches model", new Gson().toJson(item));

            sessionManager = new SessionManager(context);


            if(item.shortlist_status.equalsIgnoreCase("1") && item.shortlist_status!=null && !item.shortlist_status.isEmpty())
            {
                holder.llShortListRecent.setVisibility(View.VISIBLE);
                holder.llShortListRecentV.setVisibility(View.GONE);
            }

            if(item.request_status!=null && !item.request_status.isEmpty())
            {
                holder.llConnectedRecently.setVisibility(View.VISIBLE);
                holder.ivLikeRecentVisitors.setVisibility(View.GONE);
            }


            holder.tvNewMatchName.setText(item.first_name + " " + item.last_name);
            holder.tvNewMatchAge.setText(item.mage);
            holder.tvNewMatchHeight.setText(item.religion);
            holder.tvNewMatchCity.setText(item.maritalStatus);
            holder.tvNewMatchWorkAs.setText(item.income);
            holder.tvImageCountRecentView.setText(item.images_count);


            holder.llShortBlockRecentV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id,clickListener);
                    holder.llShortBlockRecentV.setVisibility(View.GONE);
                    holder.llBlockedRecentV.setVisibility(View.VISIBLE);
                    holder.ivLikeRecentVisitors.setVisibility(View.GONE);
                }
            });


            holder.llShortListRecentV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.shortList(context, item.member_id,clickListener);
                    holder.llShortListRecentV.setVisibility(View.GONE);
                    holder.llShortListRemove.setVisibility(View.VISIBLE);
                }
            });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("position", String.valueOf(position));
//                /*ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
//                        Pair.create(holder.ivUserMatch, "tnMemberImage"));
//                Intent intent = new Intent(view.getContext(), MatchesDetailsActivity.class);
//                intent.putExtra("data", new Gson().toJson(item));
//                view.getContext().startActivity(intent, options.toBundle());*/

                    MatchPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");

                }
            });

            holder.llPhotoRecentV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(), MemberGalleryActivity.class);
                    intent.putExtra("Member_id", item.member_id);
                    context.startActivity(intent);
                }
            });
//
//            Glide.with(context)
//                    .load(Utils.imageUrl + item.profile_photo)
//                    .placeholder(sessionManager.getKeyGender().equalsIgnoreCase("male")?R.drawable.ic_no_image__female_:R.drawable.ic_no_image__male_)
//                    .transform(item.premium_status.equalsIgnoreCase("1")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
//                    .into(holder.ivRecentViewImage);



            if (item.photo_privacy.equalsIgnoreCase("1")) {
                holder.llPhotoRecentV.setEnabled(true);
             //   holder.flPremiumRecentView.setVisibility(View.GONE);
                holder.llPremiumMsgRecentlyView.setVisibility(View.GONE);
               // holder.tvLevelPremiumRecent.setVisibility(View.GONE);

                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        //  .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                        .into(holder.ivRecentViewImage);


            } else if (item.photo_privacy.equalsIgnoreCase("3")) {
                holder.llPhotoRecentV.setEnabled(false);
             //   holder.flPremiumRecentView.setVisibility(View.VISIBLE);
                holder.llPremiumMsgRecentlyView.setVisibility(View.VISIBLE);
              //  holder.tvLevelPremiumRecent.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(holder.ivRecentViewImage);

            } else if (item.photo_privacy.equalsIgnoreCase(item.my_premium_status)) {
              //  holder.flPremiumRecentView.setVisibility(View.GONE);
                holder.llPremiumMsgRecentlyView.setVisibility(View.GONE);
                holder.llPhotoRecentV.setEnabled(true);
              //  holder.tvLevelPremiumRecent.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .into(holder.ivRecentViewImage);
            } else {
                holder.llPhotoRecentV.setEnabled(false);
               // holder.flPremiumRecentView.setVisibility(View.VISIBLE);
                holder.llPremiumMsgRecentlyView.setVisibility(View.VISIBLE);
              //  holder.tvLevelPremiumRecent.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(holder.ivRecentViewImage);
            }


            holder.ivLikeRecentVisitors.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.sentRequest(context,item.member_id);
                    holder.ivLikeRecentVisitors.setVisibility(View.GONE);
                    holder.llConnectedRecently.setVisibility(View.VISIBLE);
                }
            });

            if (item.premium_status.equalsIgnoreCase("1")) {
                holder.flPremiumRecentView.setVisibility(View.VISIBLE);
                holder.tvLevelPremiumRecent.setVisibility(View.VISIBLE);

            }

        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivRecentViewImage,ivNoImageMaleFemaleRecentView;
            TextView tvNewMatchName, tvNewMatchAge,tvLevelPremiumRecent, tvNewMatchHeight, tvNewMatchCity, tvNewMatchWorkAs,tvImageCountRecentView;
            LinearLayout llPhotoRecentV,llShortBlockRecentV,llConnectShortList,llShortListRecent,llBlockedRecentV,llPremiumMsgRecentlyView,ivLikeRecentVisitors,llShortListRecentV,llShortListRemove,llNo_imageFemaleListRecentView,llConnectedRecently;
            FrameLayout flNoImageMaleFemaleListRecentView,flPremiumRecentView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                llConnectShortList=itemView.findViewById(R.id.llConnectShortList);
                llShortListRecent=itemView.findViewById(R.id.llShortListRecent);
                tvNewMatchAge = itemView.findViewById(R.id.tvRecentViewAgeRs);
                tvNewMatchName = itemView.findViewById(R.id.ivRecentViewNameRs);
                tvNewMatchHeight = itemView.findViewById(R.id.tvRecentViewHeightRs);
                tvNewMatchCity = itemView.findViewById(R.id.tvRecentViewCityRs);
                tvNewMatchWorkAs = itemView.findViewById(R.id.tvRecentViewWorkAsRs);
                llPhotoRecentV = itemView.findViewById(R.id.llPhotoRecentV);
                llShortBlockRecentV=itemView.findViewById(R.id.llShortBlockRecentV);
                llBlockedRecentV=itemView.findViewById(R.id.llBlockedRecentV);
                ivLikeRecentVisitors=itemView.findViewById(R.id.ivLikeRecentVisitors);
                llShortListRecentV=itemView.findViewById(R.id.llShortListRecentV);
                llShortListRemove=itemView.findViewById(R.id.llShortListRemove);
                llConnectedRecently=itemView.findViewById(R.id.llConnectedRecently);
                flNoImageMaleFemaleListRecentView=itemView.findViewById(R.id.flNoImageMaleFemaleListRecentView);
                ivRecentViewImage=itemView.findViewById(R.id.ivRecentViewImage);
                ivNoImageMaleFemaleRecentView=itemView.findViewById(R.id.ivNoImageMaleFemaleRecentView);
                llNo_imageFemaleListRecentView=itemView.findViewById(R.id.llNo_imageFemaleListRecentView);
                flPremiumRecentView=itemView.findViewById(R.id.flPremiumRecentView);
                tvImageCountRecentView=itemView.findViewById(R.id.tvImageCountRecentView);
                llPremiumMsgRecentlyView=itemView.findViewById(R.id.llPremiumMsgRecentlyView);
                tvLevelPremiumRecent=itemView.findViewById(R.id.tvLevelPremiumRecent);
            }
        }
    }

