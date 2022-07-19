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

import java.util.List;

    public class BlockMemberAdapter extends RecyclerView.Adapter<BlockMemberAdapter.ViewHolder>{
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
        public void onBindViewHolder(@NonNull BlockMemberAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NewMatchesModel item = list.get(position);
            Log.e(" New Matches model", new Gson().toJson(item));

            holder.tvBlockName.setText(item.first_name + " " + item.last_name);
            holder.tvBlockAge.setText(item.mage);
            holder.tvNewBlockHeight.setText(item.religion);
            holder.tvBlockCity.setText(item.maritalStatus);
            holder.tvBlockWorkAs.setText(item.income);




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
            TextView tvBlockName, tvBlockAge, tvNewBlockHeight, tvBlockCity, tvBlockWorkAs;
            LinearLayout llPhotoBlock;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvBlockAge = itemView.findViewById(R.id.tvBlockAge);
                tvBlockName = itemView.findViewById(R.id.tvNewBlockName);
                tvNewBlockHeight = itemView.findViewById(R.id.tvNewBlockHeight);
                tvBlockCity = itemView.findViewById(R.id.tvBlockCity);
                tvBlockWorkAs = itemView.findViewById(R.id.tvBlockWorkAs);
                llPhotoBlock = itemView.findViewById(R.id.llPhotoBlock);

            }
        }
    }
