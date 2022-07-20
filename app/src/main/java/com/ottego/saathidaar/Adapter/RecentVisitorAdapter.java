package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.GalleryActivity;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;


public class RecentVisitorAdapter extends RecyclerView.Adapter<RecentVisitorAdapter.ViewHolder>{
        Context context;
        List<NewMatchesModel> list;

        public RecentVisitorAdapter(Context context, List<NewMatchesModel> list) {
            this.context = context;
            this.list = list;


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

            holder.tvNewMatchName.setText(item.first_name + " " + item.last_name);
            holder.tvNewMatchAge.setText(item.mage);
            holder.tvNewMatchHeight.setText(item.religion);
            holder.tvNewMatchCity.setText(item.maritalStatus);
            holder.tvNewMatchWorkAs.setText(item.income);


            holder.llShortBlockRecentV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id);
                    holder.llShortBlockRecentV.setVisibility(View.GONE);
                    holder.llBlockedRecentV.setVisibility(View.VISIBLE);
                    holder.ivLikeRecentVisitors.setEnabled(false);
                }
            });


            holder.llShortListRecentV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.blockMember(context, item.member_id);
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
                    Intent intent = new Intent(view.getContext(), GalleryActivity.class);
                    context.startActivity(intent);
                }
            });

        }





        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivUserMatch;
            TextView tvNewMatchName, tvNewMatchAge, tvNewMatchHeight, tvNewMatchCity, tvNewMatchWorkAs;
            LinearLayout llPhotoRecentV,llShortBlockRecentV,llBlockedRecentV,ivLikeRecentVisitors,llShortListRecentV,llShortListRemove;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
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



            }
        }
    }

