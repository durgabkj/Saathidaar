package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.ottego.saathidaar.NetworkUtil;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    if(!NetworkUtil.isConnectedToInternet(context)){ // internet is not connected
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View layout_dialog= LayoutInflater.from(context).inflate(R.layout.internetconnection,null);
        builder.setView(layout_dialog);

        AppCompatButton tvTryAgain =layout_dialog.findViewById(R.id.tvTryAgain);
        // show dialog

        AlertDialog dialog=builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getWindow().setGravity(Gravity.CENTER);

        tvTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onReceive(context ,intent);

            }
        });
    }
    }
}