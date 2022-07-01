package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.MatchesDetailsActivity;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.UpgradePlanDetailsActivity;
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
        Log.e(" New Matches model", new Gson().toJson(item));

        holder.tvNewMatchName.setText(item.first_name + " " + item.last_name);
        holder.tvNewMatchAge.setText(item.mage);
        holder.tvNewMatchHeight.setText(item.religion);
        holder.tvNewMatchCity.setText(item.maritalStatus);


        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sentRequest(context, item.member_id);

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

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),
                        Pair.create(holder.ivUserMatch, "tnMemberImage"));
                Intent intent = new Intent(view.getContext(), MatchesDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(item));
                view.getContext().startActivity(intent, options.toBundle());

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
        LinearLayout llMess,llShortListRemove,llShortList;
        LinearLayout ivLike;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNewMatchAge = itemView.findViewById(R.id.tvNewMatchAge);
            tvNewMatchName = itemView.findViewById(R.id.tvNewMatchName);
            tvNewMatchHeight = itemView.findViewById(R.id.tvNewMatchHeight);
            tvNewMatchCity = itemView.findViewById(R.id.tvNewMatchCity);
            tvNewMatchWorkAs = itemView.findViewById(R.id.tvNewMatchWorkAs);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivUserMatch = itemView.findViewById(R.id.ivUserMatch);
            llShortListRemove = itemView.findViewById(R.id.llShortListRemove);
            llShortList = itemView.findViewById(R.id.llShortList);
        }
    }
}
