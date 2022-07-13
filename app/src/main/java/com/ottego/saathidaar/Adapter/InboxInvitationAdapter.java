package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.InboxPagerFragment;
import com.ottego.saathidaar.MatchPagerFragment;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class InboxInvitationAdapter extends RecyclerView.Adapter<InboxInvitationAdapter.ViewHolder>{

    private AdapterView.OnItemClickListener onItemClickListener;
    Context context;
    List<InboxModel> list;

    public InboxInvitationAdapter(Context context, List<InboxModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InboxInvitationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invitation_inbox, parent, false);
        return new InboxInvitationAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull InboxInvitationAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        InboxModel item = list.get(position);
        Log.e(" Inbox model", new Gson().toJson(item));

        holder.tvInvNewMatchName.setText(item.first_name + " " + item.last_name);
        holder.tvInvNewMatchAge.setText(item.mage);
        holder.tvInvNewMatchHeight.setText(item.religion);
        holder.tvInvNewMatchCity.setText(item.maritalStatus);
holder.tvInvNewMatchWorkAs.setText(item.country);

        holder.llAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.acceptRequest(context, item.member_id);
                 holder.llAccept.setVisibility(View.GONE);
                 holder.llAccepted.setVisibility(View.VISIBLE);
                 holder.llDelete.setVisibility(View.GONE);
            }
        });


        holder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.deleteRequest(context, item.member_id);
                holder.llAccept.setVisibility(View.GONE);
                holder.llDelete.setVisibility(View.GONE);
                holder.llDeleted.setVisibility(View.VISIBLE);

            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("position Inbox", String.valueOf(position));
                InboxPagerFragment.newInstance(String.valueOf(position), "").show(((FragmentActivity) context).getSupportFragmentManager(), "Inbox_pager_fragment");

            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvInvNewMatchName, tvInvNewMatchAge, tvInvNewMatchHeight, tvInvNewMatchCity, tvInvNewMatchWorkAs;
        LinearLayout llAccept,llDelete,llAccepted,llDeleted;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvInvNewMatchAge = itemView.findViewById(R.id.tvInvNewMatchAge);
            tvInvNewMatchName = itemView.findViewById(R.id.tvInvNewMatchName);
            tvInvNewMatchHeight = itemView.findViewById(R.id.tvInvNewMatchHeight);
            tvInvNewMatchCity = itemView.findViewById(R.id.tvInvNewMatchCity);
            tvInvNewMatchWorkAs = itemView.findViewById(R.id.tvInvNewMatchWorkAs);
            llAccepted = itemView.findViewById(R.id.llAccepted);
            llAccept = itemView.findViewById(R.id.llAccept);
            llDelete = itemView.findViewById(R.id.llDelete);
            llDeleted = itemView.findViewById(R.id.llDeleted);
        }
    }
}
