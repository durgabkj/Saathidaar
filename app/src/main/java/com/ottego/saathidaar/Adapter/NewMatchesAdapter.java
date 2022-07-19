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
import android.widget.Spinner;
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

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {

    Context context;
    List<NewMatchesModel> list;

    public NewMatchesAdapter(Context context, List<NewMatchesModel> list) {
        this.context = context;
        this.list = list;
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

        holder.tvNewMatchName.setText(item.first_name + " " + item.member_id);
        holder.tvNewMatchAge.setText(item.mage);
        holder.tvNewMatchHeight.setText(item.religion);
        holder.tvNewMatchCity.setText(item.maritalStatus);



        holder.llPhotoMyMatches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(), GalleryActivity.class);
                context.startActivity(intent);
            }
        });


        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sentRequest(context, item.member_id);
                holder.ivLike.setVisibility(View.GONE);
                 holder.llConnect.setVisibility(View.VISIBLE);

            }
        });

        holder.llShortList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.shortList(context, item.member_id);
                holder.llShortList.setVisibility(View.GONE);
                holder.llShortListRemove.setVisibility(View.VISIBLE);

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position", String.valueOf(position));
                MatchPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "match_pager_fragment");

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
        LinearLayout llMess, llShortListRemove, llShortList,llPhotoMyMatches;
        LinearLayout ivLike,llConnect;
Spinner SpMenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNewMatchAge = itemView.findViewById(R.id.tvNewMatchAge);
            tvNewMatchName = itemView.findViewById(R.id.tvNewMatchName);
            tvNewMatchHeight = itemView.findViewById(R.id.tvNewMatchHeight);
            tvNewMatchCity = itemView.findViewById(R.id.tvNewMatchCity);
            tvNewMatchWorkAs = itemView.findViewById(R.id.tvNewMatchWorkAs);
            ivLike = itemView.findViewById(R.id.ivLike);
            llPhotoMyMatches=itemView.findViewById(R.id.llPhotoMyMatches);
            ivUserMatch = itemView.findViewById(R.id.ivUserMatch);
            llShortListRemove = itemView.findViewById(R.id.llShortListRemove);
            llShortList = itemView.findViewById(R.id.llShortList);
            llConnect=itemView.findViewById(R.id.llConnect);

        }
    }
}
