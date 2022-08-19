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
import androidx.cardview.widget.CardView;
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

        holder.tvPlanFeature.setText(item.features.get(0).features_name);
        holder.tvPlanFeature1.setText(item.features.get(1).features_name);
        holder.tvPlanFeature2.setText(item.features.get(2).features_name);
        holder.tvPlanFeature3.setText(item.features.get(3).features_name);
        holder.tvPlanFeature4.setText(item.features.get(4).features_name);
        holder.tvPlanFeature5.setText(item.features.get(5).features_name);
        holder.tvPlanFeature6.setText(item.features.get(6).features_name);
        holder.tvPlanFeature7.setText(item.features.get(7).features_name);


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

        if (item.features.get(0).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature.setVisibility(View.VISIBLE);
        }

        if (item.features.get(1).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature1.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature1.setVisibility(View.VISIBLE);
        }

        if (item.features.get(2).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature2.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature2.setVisibility(View.VISIBLE);
        }

        if (item.features.get(3).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature3.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature3.setVisibility(View.VISIBLE);
        }

        if (item.features.get(4).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature4.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature4.setVisibility(View.VISIBLE);
        }

        if (item.features.get(5).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature5.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature5.setVisibility(View.VISIBLE);
        }

        if (item.features.get(6).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature6.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature6.setVisibility(View.VISIBLE);
        }

        if (item.features.get(7).features_valid.equalsIgnoreCase("1"))
        {
            holder.cvCheckFeature7.setVisibility(View.VISIBLE);
        }else
        {
            holder.cvClearFeature7.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardName, tvPriceUpgrade, tvMonths, tvName, tvPeriod, tvPrice, tvTotal, tvPriceTotal, tvPlanFeature, tvPlanFeature1, tvPlanFeature2, tvPlanFeature3, tvPlanFeature4, tvPlanFeature5, tvPlanFeature6, tvPlanFeature7;
        TextView btnDashboard;
        CardView cvCheckFeature, cvClearFeature, cvCheckFeature1, cvClearFeature1, cvCheckFeature2, cvClearFeature2, cvCheckFeature3, cvClearFeature3, cvCheckFeature4, cvClearFeature4, cvCheckFeature5, cvClearFeature5, cvCheckFeature6, cvClearFeature6, cvCheckFeature7, cvClearFeature7;


        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvCardName = itemView.findViewById(R.id.tvCardName);
            tvPriceUpgrade = itemView.findViewById(R.id.tvPriceUpgrade);
            tvMonths = itemView.findViewById(R.id.tvMonths);
            btnDashboard = itemView.findViewById(R.id.btnDashboard);
            tvPlanFeature = itemView.findViewById(R.id.tvPlanFearure);
            tvPlanFeature1 = itemView.findViewById(R.id.tvPlanFearure1);
            tvPlanFeature2 = itemView.findViewById(R.id.tvPlanFearure2);
            tvPlanFeature3 = itemView.findViewById(R.id.tvPlanFearure3);
            tvPlanFeature4 = itemView.findViewById(R.id.tvPlanFearure4);
            tvPlanFeature5 = itemView.findViewById(R.id.tvPlanFearure5);
            tvPlanFeature6 = itemView.findViewById(R.id.tvPlanFearure6);
            tvPlanFeature7 = itemView.findViewById(R.id.tvPlanFearure7);


            cvCheckFeature = itemView.findViewById(R.id.cvCheckFeature);
            cvClearFeature = itemView.findViewById(R.id.cvClearFeature);

            cvCheckFeature1 = itemView.findViewById(R.id.cvCheckFeature1);
            cvClearFeature1 = itemView.findViewById(R.id.cvClearFeature1);

            cvCheckFeature2 = itemView.findViewById(R.id.cvCheckFeature2);
            cvClearFeature2 = itemView.findViewById(R.id.cvClearFeature2);

            cvCheckFeature3 = itemView.findViewById(R.id.cvCheckFeature3);
            cvClearFeature3 = itemView.findViewById(R.id.cvClearFeature3);

            cvCheckFeature4 = itemView.findViewById(R.id.cvCheckFeature4);
            cvClearFeature4 = itemView.findViewById(R.id.cvClearFeature4);

            cvCheckFeature5 = itemView.findViewById(R.id.cvCheckFeature5);
            cvClearFeature5 = itemView.findViewById(R.id.cvClearFeature5);

            cvCheckFeature6 = itemView.findViewById(R.id.cvCheckFeature6);
            cvClearFeature6 = itemView.findViewById(R.id.cvClearFeature6);

            cvCheckFeature7 = itemView.findViewById(R.id.cvCheckFeature7);
            cvClearFeature7 = itemView.findViewById(R.id.cvClearFeature7);


        }
    }
}
