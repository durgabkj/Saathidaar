package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.UpgradePlanDetailsActivity;

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

        holder.tvCardName.setText(item.plan_name);
        holder.tvMonths.setText(item.plan_validity);
        holder.tvPriceUpgrade.setText(item.plan_price);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(),
                        Pair.create(holder.tvPriceUpgrade, "tnMembershipPlan"));
                Intent intent = new Intent(v.getContext(), UpgradePlanDetailsActivity.class);
                intent.putExtra("data", new Gson().toJson(item));
               v.getContext().startActivity(intent, options.toBundle());
            }
        });

        holder.btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());
                View layout_dialog= LayoutInflater.from(view.getContext()).inflate(R.layout.detail_layout,null);
                builder.setView(layout_dialog);
                AppCompatTextView tvName =layout_dialog.findViewById(R.id.tvName);
                AppCompatTextView tvPeriod =layout_dialog.findViewById(R.id.tvPeriod);
                AppCompatTextView tvPrice =layout_dialog.findViewById(R.id.tvPrice);
                AppCompatTextView tvPriceTotal =layout_dialog.findViewById(R.id.tvPriceTotal);
                AppCompatImageView ivClear =layout_dialog.findViewById(R.id.ivClearPlan);
                AppCompatButton btnProceed =layout_dialog.findViewById(R.id.btnProceed);


                tvName.setText(item.plan_name);
                tvPeriod.setText(item.plan_validity);
                tvPrice.setText(item.plan_price);
                tvPriceTotal.setText(item.plan_price);



                AlertDialog dialog=builder.create();
                dialog.getWindow().setLayout(400,200);
                dialog.show();

                Window window = dialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                dialog.getWindow().setGravity(Gravity.CENTER);


                ivClear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });



                btnProceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) v.getContext(),
                                Pair.create(holder.tvPriceUpgrade, "tnMembershipPlan"));
                        Intent intent = new Intent(v.getContext(), UpgradePlanDetailsActivity.class);
                        intent.putExtra("data", new Gson().toJson(item));
                        v.getContext().startActivity(intent, options.toBundle());
                    }
                });

            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardName,tvPriceUpgrade,tvMonths,tvName,tvPeriod,tvPrice,tvTotal,tvPriceTotal;
TextView btnDashboard;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvPriceUpgrade = itemView.findViewById(R.id.tvPriceUpgrade);
            tvMonths = itemView.findViewById(R.id.tvMonths);
            btnDashboard=itemView.findViewById(R.id.btnDashboard);


        }
    }
}
