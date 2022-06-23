package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.NewMatchesFragment;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class UpgradeAdapter  extends RecyclerView.Adapter<UpgradeAdapter.ViewHolder>{
    Context context;
    List<UpgradeModel> list;

    public UpgradeAdapter(Context context, List<UpgradeModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public UpgradeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upgrade_page, parent, false);
        return new UpgradeAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UpgradeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        UpgradeModel item = list.get(position);
        Log.e(" New Matches model", new Gson().toJson(item));

        holder.tvCardName.setText(item.plan_name);
        holder.tvMonths.setText(item.plan_validity);
        holder.tvPriceUpgrade.setText(item.plan_price);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardName,tvPriceUpgrade,tvMonths;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvPriceUpgrade = itemView.findViewById(R.id.tvPriceUpgrade);
            tvMonths = itemView.findViewById(R.id.tvMonths);


        }
    }
}
