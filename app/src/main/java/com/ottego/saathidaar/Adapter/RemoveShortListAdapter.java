package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class RemoveShortListAdapter extends RecyclerView.Adapter<RemoveShortListAdapter.ViewHolder>{
    Context context;
    List<NewMatchesModel> list;

    public RemoveShortListAdapter(Context context, List<NewMatchesModel> list) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public RemoveShortListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_remove_shortlist, parent, false);
        return new RemoveShortListAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RemoveShortListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NewMatchesModel item = list.get(position);
        Log.e(" New Matches model", new Gson().toJson(item));

        holder.tvNewMatchNameRs.setText(item.first_name + " " + item.last_name);
        holder.tvNewMatchAgeRs.setText(item.mage+"Yrs");
        holder.tvNewMatchHeightRs.setText(item.religion);
        holder.tvNewMatchCityRs.setText(item.maritalStatus);


        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.sentRequest(context, item.member_id);

            }
        });

        holder.llShortListRemove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.removeShortList(context, item.member_id);
                holder.llShortList1.setVisibility(View.VISIBLE);
                holder.llShortListRemove1.setVisibility(View.GONE);
            }
        });


    }
    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageListViewMess;
        TextView tvNewMatchNameRs, tvNewMatchAgeRs, tvNewMatchHeightRs, tvNewMatchCityRs, tvNewMatchWorkAsRs;
        LinearLayout llMess,llShortListRemove1,llShortList1;
        LinearLayout ivLike;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNewMatchAgeRs = itemView.findViewById(R.id.tvNewMatchAgeRs);
            tvNewMatchNameRs = itemView.findViewById(R.id.tvNewMatchNameRs);
            tvNewMatchHeightRs = itemView.findViewById(R.id.tvNewMatchHeightRs);
            tvNewMatchCityRs = itemView.findViewById(R.id.tvNewMatchCityRs);
            tvNewMatchWorkAsRs = itemView.findViewById(R.id.tvNewMatchWorkAsRs);
            ivLike = itemView.findViewById(R.id.ivLike);
            llShortListRemove1 = itemView.findViewById(R.id.llShortListRemove1);
            llShortList1 = itemView.findViewById(R.id.llShortList1);
        }
    }
}
