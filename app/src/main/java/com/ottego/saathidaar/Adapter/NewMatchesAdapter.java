package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.NewMatchesFragment;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Utils;

import java.util.List;

public class NewMatchesAdapter extends RecyclerView.Adapter<NewMatchesAdapter.ViewHolder> {
    private AdapterView.OnItemClickListener onItemClickListener;
    Context context;
    List<NewMatchesModel> list;

    public NewMatchesAdapter(Context context, List<NewMatchesModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_inbox_invitation_sent, parent, false);
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
        ImageView imageListViewMess;
        TextView tvNewMatchName, tvNewMatchAge, tvNewMatchHeight, tvNewMatchCity, tvNewMatchWorkAs;
        LinearLayout llMess;
        ImageView ivLike;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvNewMatchAge = itemView.findViewById(R.id.tvNewMatchAge);
            tvNewMatchName = itemView.findViewById(R.id.tvNewMatchName);
            tvNewMatchHeight = itemView.findViewById(R.id.tvNewMatchHeight);
            tvNewMatchCity = itemView.findViewById(R.id.tvNewMatchCity);
            tvNewMatchWorkAs = itemView.findViewById(R.id.tvNewMatchWorkAs);
            ivLike = itemView.findViewById(R.id.ivLike);
        }
    }
}
