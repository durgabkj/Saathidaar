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
import com.ottego.saathidaar.GalleryActivity;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;

import java.util.List;

    public class BlockMemberAdapter extends RecyclerView.Adapter<BlockMemberAdapter.ViewHolder>{
        SessionManager sessionManager;
        Context context;
        List<NewMatchesModel> list;

        public BlockMemberAdapter(Context context, List<NewMatchesModel> list) {
            this.context = context;
            this.list = list;


        }


        @NonNull
        @Override
        public BlockMemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.un_block_layout, parent, false);
            return new BlockMemberAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NewMatchesModel item = list.get(position);
            Log.e(" New Matches model", new Gson().toJson(item));
            sessionManager=new SessionManager(context);

            holder.tvBlockName.setText(item.first_name + " " + item.last_name);
            holder.tvBlockAge.setText(item.mage+" yrs");
            holder.tvNewBlockHeight.setText(item.religion);
            holder.tvBlockCity.setText(item.maritalStatus);
            holder.tvBlockWorkAs.setText(item.income);


            holder.ivUnblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.UnblockMember(context, item.member_id);
                    holder.ivUnblock.setVisibility(View.GONE);
                    holder.llConnectBlock.setVisibility(View.VISIBLE);
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


            holder.llPhotoBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(), MemberGalleryActivity.class);
                    intent.putExtra("Member_id", item.member_id);
                    context.startActivity(intent);
                }
            });




            if (!(!item.profile_photo.isEmpty()) && !(item.profile_photo != null)) {
                Glide.with(context)
                        .load(Utils.imageUrl + item.profile_photo)
                        .into(holder.ivBlockProfileImage);

            } else {
                if (sessionManager.getKeyGender().equalsIgnoreCase("male")) {
                    holder.llNo_imageFemaleListBlock.setVisibility(View.VISIBLE);
                    holder.flNoImageMaleFemaleListBlock.setVisibility(View.VISIBLE);
                    holder.ivBlockProfileImage.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__female_)
                            .into(holder.ivNoImageMaleFemaleBlock);

                } else {
                    holder.llNo_imageFemaleListBlock.setVisibility(View.VISIBLE);
                    holder.flNoImageMaleFemaleListBlock.setVisibility(View.VISIBLE);
                    holder.ivBlockProfileImage.setVisibility(View.GONE);

                    Glide.with(context)
                            .load(R.drawable.ic_no_image__male_)
                            .into(holder.ivNoImageMaleFemaleBlock);

                }


            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView ivBlockProfileImage,ivNoImageMaleFemaleBlock;
            TextView tvBlockName, tvBlockAge, tvNewBlockHeight, tvBlockCity, tvBlockWorkAs;
            LinearLayout llPhotoBlock,ivUnblock,llConnectBlock,llNo_imageFemaleListBlock;

            FrameLayout flNoImageMaleFemaleListBlock;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvBlockAge = itemView.findViewById(R.id.tvBlockAge);
                tvBlockName = itemView.findViewById(R.id.tvNewBlockName);
                tvNewBlockHeight = itemView.findViewById(R.id.tvNewBlockHeight);
                tvBlockCity = itemView.findViewById(R.id.tvBlockCity);
                tvBlockWorkAs = itemView.findViewById(R.id.tvBlockWorkAs);
                llPhotoBlock = itemView.findViewById(R.id.llPhotoBlock);
                ivUnblock=itemView.findViewById(R.id.ivUnblock);
                llConnectBlock=itemView.findViewById(R.id.llConnectBlock);


                ivBlockProfileImage=itemView.findViewById(R.id.ivBlockProfileImage);
                ivNoImageMaleFemaleBlock=itemView.findViewById(R.id.ivNoImageMaleFemaleBlock);
                flNoImageMaleFemaleListBlock=itemView.findViewById(R.id.flNoImageMaleFemaleListBlock);
                llNo_imageFemaleListBlock=itemView.findViewById(R.id.llNo_imageFemaleListBlock);
            }
        }
    }
