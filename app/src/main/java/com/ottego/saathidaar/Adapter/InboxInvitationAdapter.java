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

        holder.tvInvDeleteName.setText(item.first_name + " " + item.last_name);
        holder.tvInvDeleteAge.setText(item.mage);
        holder.tvInvDeleteHeight.setText(item.religion);
        holder.tvInvDeleteCity.setText(item.maritalStatus);
holder.tvInvDeleteWorkAs.setText(item.country);

        holder.llBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.acceptRequest(context, item.member_id);
                 holder.llBlock.setVisibility(View.GONE);
                 holder.llBlocked.setVisibility(View.VISIBLE);
                 holder.llDelete1.setVisibility(View.GONE);
            }
        });


        holder.llDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.deleteRequest(context, item.member_id);
              //  holder.llAccept.setVisibility(View.GONE);
                holder.llDelete1.setVisibility(View.GONE);
                holder.llDeletedInvitation.setVisibility(View.VISIBLE);

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
        TextView tvInvDeleteWorkAs, tvInvDeleteCity, tvInvDeleteHeight, tvInvDeleteName, tvInvDeleteAge;
        LinearLayout llBlocked,llBlock,llDeletedInvitation,llDelete1;
        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvInvDeleteAge = itemView.findViewById(R.id.tvInvDeleteAge);
            tvInvDeleteName = itemView.findViewById(R.id.tvInvDeleteName);
            tvInvDeleteHeight = itemView.findViewById(R.id.tvInvDeleteHeight);
            tvInvDeleteCity = itemView.findViewById(R.id.tvInvDeleteCity);
            tvInvDeleteWorkAs = itemView.findViewById(R.id.tvInvDeleteWorkAs);
            llBlocked = itemView.findViewById(R.id.llBlocked);
            llBlock = itemView.findViewById(R.id.llBlock);
            llDelete1 = itemView.findViewById(R.id.llDelete1);
            llDeletedInvitation = itemView.findViewById(R.id.llDeletedInvitation);
        }
    }
}
