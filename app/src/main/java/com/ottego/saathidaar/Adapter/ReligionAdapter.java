package com.ottego.saathidaar.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ottego.saathidaar.R;
import com.ottego.saathidaar.Model.ReligionModel;

import java.util.ArrayList;

public class ReligionAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ReligionModel> arrayList;
    private TextView name;

    public ReligionAdapter(Context context, ArrayList<ReligionModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

}