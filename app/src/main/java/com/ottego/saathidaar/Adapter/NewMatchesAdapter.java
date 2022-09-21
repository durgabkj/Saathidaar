package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ottego.saathidaar.ApiListener;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.MemberGalleryActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
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
        this.clickListener = clickListener;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   Log.e("position", String.valueOf(position));
                MatchPagerFragment.newInstance(String.valueOf(position), String.valueOf(list.size())).show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");
            }
        });



        holder.llViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MatchPagerFragment.newInstance(String.valueOf(position), String.valueOf(list.size())).show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");
            }
        });



        holder.tvNewMatchName.setText(Utils.nullToBlank(item.first_name) + " " + Utils.nullToBlank(item.last_name).charAt(0));

        if (item.mage.equalsIgnoreCase("null") && !item.mage.equalsIgnoreCase("")) {
            holder.tvNewMatchAge.setText("Age-Not Specified");
        } else {
            holder.tvNewMatchAge.setText(Utils.nullToBlank(item.mage));
        }

        holder.tvNewMatchHeight.setText(Utils.nullToBlank(item.religion));
        holder.tvNewMatchCity.setText(Utils.nullToBlank(item.maritalStatus));
        holder.tvNewMatchWorkAs.setText(Utils.nullToBlank(item.city));
        holder.tvNewMatchState.setText(Utils.nullToBlank(item.state));
        holder.tvImageCount.setText(item.images_count);

        holder.llPrivateMatchesPhoto.setVisibility(View.GONE);
//        holder.ivMatchDot.setVisibility(View.VISIBLE);
//        holder.ivMatchDot2.setVisibility(View.VISIBLE);
//        holder.ivMatchDot1.setVisibility(View.VISIBLE);

        if (item.photo_privacy.equalsIgnoreCase("1")) {
            holder.llPhotoMyMatches.setEnabled(true);
            // holder.flPremiumMatch.setVisibility(View.GONE);
            holder.llPremiumMsgMatches.setVisibility(View.GONE);
//            holder.tvLevelPremiumMatch.setVisibility(View.GONE);

            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    //  .transform(!item.my_premium_status.equals(item.premium_status)?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                    .into(holder.ivUserMatch);
        } else if (item.photo_privacy.equalsIgnoreCase("3")) {
            holder.llPhotoMyMatches.setEnabled(false);
            // holder.flPremiumMatch.setVisibility(View.VISIBLE);
            holder.llPremiumMsgMatches.setVisibility(View.GONE);
            holder.llPrivateMatchesPhoto.setVisibility(View.VISIBLE);
            // holder.tvLevelPremiumMatch.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .transform(new BlurTransformation(20, 8))
                    .into(holder.ivUserMatch);

        } else if (item.photo_privacy.equalsIgnoreCase(item.my_premium_status)) {
            // holder.flPremiumMatch.setVisibility(View.GONE);
            holder.llPremiumMsgMatches.setVisibility(View.GONE);
            holder.llPhotoMyMatches.setEnabled(true);
            //  holder.tvLevelPremiumMatch.setVisibility(View.GONE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .into(holder.ivUserMatch);
        } else {
            //  holder.flPremiumMatch.setVisibility(View.VISIBLE);
            holder.llPremiumMsgMatches.setVisibility(View.VISIBLE);
            holder.llPhotoMyMatches.setEnabled(false);
            //  holder.tvLevelPremiumMatch.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(Utils.imageUrl + item.profile_photo)
                    .placeholder(item.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                    .transform(new BlurTransformation(20, 8))
                    .into(holder.ivUserMatch);
        }


        if (item.shortlist_status.equalsIgnoreCase("1") && item.shortlist_status != null && !item.shortlist_status.isEmpty()) {
            holder.llShortListRemove.setVisibility(View.VISIBLE);
            holder.llShortList.setVisibility(View.GONE);
        }

//show request sent status
        if ((item.request_status != null) && (!item.request_status.isEmpty()) && (!item.request_status.equalsIgnoreCase("")) && (!item.request_status.equalsIgnoreCase("null"))) {
            holder.llConnect.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.GONE);
        } else {
            holder.llConnect.setVisibility(View.GONE);
            holder.ivLike.setVisibility(View.VISIBLE);
        }

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
                Utils.sentRequest(context, item.member_id, clickListener);
                holder.ivLike.setVisibility(View.GONE);
                holder.llConnect.setVisibility(View.VISIBLE);

            }
        });

        holder.llShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.shortList(context, item.member_id, clickListener);
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

        holder.tvPremiumContactMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UpgradeOnButtonActivity.class);
                context.startActivity(intent);
            }
        });

        if (item.premium_status.equalsIgnoreCase("1")) {
            holder.flPremiumMatch.setVisibility(View.VISIBLE);
            holder.tvLevelPremiumMatch.setVisibility(View.VISIBLE);
        }
// hide premium msg on the basis of image count
        if (item.images_count.equalsIgnoreCase("0")) {
            holder.llPremiumMsgMatches.setVisibility(View.GONE);
        }


        //For free user...Apply Conditions
        if(item.my_premium_status.equalsIgnoreCase("0"))
        {
//            holder.llShortList.setEnabled(false);
//            holder.llShortBlock.setEnabled(false);
//            holder.ivLike.setEnabled(false);

            holder.llShortList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Upgrade your profile to add this profile in Shortlist", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });

            holder.llShortBlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Upgrade your profile to add this profile in Blocklist", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });

            holder.ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Upgrade to premium if you want to connect", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(view.getContext(),UpgradeOnButtonActivity.class);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivUserMatch, ivNoImageMaleFemaleMatch, ivMatchDot, ivMatchDot1, ivMatchDot2;
        TextView tvNewMatchName, tvNewMatchAge, tvPremiumContactMatch, tvLevelPremiumMatch, tvNewMatchHeight, tvNewMatchCity,tvNewMatchState, tvNewMatchWorkAs, tvImageCount;
        LinearLayout llMess, llShortListRemove, llPrivateMatchesPhoto, llShortList, llPhotoMyMatches, llShortBlock, llBlocked, llItemAnimation;
        LinearLayout ivLike,llViewProfile, llConnect, llNo_imageFemaleList, llPremiumMsgMatches;
        FrameLayout flNoImageMaleFemaleList, flPremiumMatch;
        Spinner SpMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMatchDot = itemView.findViewById(R.id.ivMatchDot);
            ivMatchDot1 = itemView.findViewById(R.id.ivMatchDot1);
            ivMatchDot2 = itemView.findViewById(R.id.ivMatchDot2);
            llPrivateMatchesPhoto = itemView.findViewById(R.id.llPrivateMatchesPhoto);
            tvPremiumContactMatch = itemView.findViewById(R.id.tvPremiumContactMatch);
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
            llViewProfile = itemView.findViewById(R.id.llViewProfile);
           tvNewMatchState = itemView.findViewById(R.id.tvNewMatchState);
//            ivNoImageMaleFemaleMatch = itemView.findViewById(R.id.ivNoImageMaleFemaleMatch);
            tvImageCount = itemView.findViewById(R.id.tvImageCount);
            llItemAnimation = itemView.findViewById(R.id.llItemAnimation);
            flPremiumMatch = itemView.findViewById(R.id.flPremiumMatch);
            llPremiumMsgMatches = itemView.findViewById(R.id.llPremiumMsgMatches);
            tvLevelPremiumMatch = itemView.findViewById(R.id.tvLevelPremiumMatch);

        }
    }
}
