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
import com.ottego.saathidaar.NewMatchesFragment;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

        Context context;
        List<NewMatchesModel> list;

        public InvitationAdapter(Context context, List<NewMatchesModel> list) {
            this.context = context;
            this.list = list;
        }


        @NonNull
        @Override
        public InvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inbox_invitation_sent, parent, false);
            return new InvitationAdapter.ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull InvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
            NewMatchesModel item = list.get(position);
            Log.e(" invitation model", new Gson().toJson(item));

            holder.tvInvNewMatchName.setText(item.first_name + " " + item.last_name);
            holder.tvInvNewMatchAge.setText(item.mage);
            holder.tvInvNewMatchHeight.setText(item.religion);
            holder.tvInvNewMatchCity.setText(item.maritalStatus);


            holder.llAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.acceptRequest(context, item.member_id);
                    NewMatchesFragment newMatchesFragment=new NewMatchesFragment();
                    newMatchesFragment.getData("");
                }
            });



        }


        @Override
        public int getItemCount() {
            return list.size();
        }



        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvInvNewMatchName, tvInvNewMatchAge, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAs;
            LinearLayout llAccept,llDelete;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
                tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
                tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
                tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
                tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
                llAccept = itemView.findViewById(R.id.llAccept);
                llDelete = itemView.findViewById(R.id.llDelete);
            }
        }
    }

