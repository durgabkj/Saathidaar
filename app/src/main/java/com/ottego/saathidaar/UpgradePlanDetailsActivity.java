package com.ottego.saathidaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.databinding.ActivityUpgradePlanDetailsBinding;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpgradePlanDetailsActivity extends AppCompatActivity implements PaymentResultListener {
    ActivityUpgradePlanDetailsBinding b;
    UpgradeModel model;
    SessionManager sessionManager;
    Context context;
    public String payment = "http://192.168.1.38:9094/upgrade/payment-pay/";
    private static final String TAG = "Razorpay";
    Checkout checkout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityUpgradePlanDetailsBinding.inflate(getLayoutInflater());
        context = UpgradePlanDetailsActivity.this;
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, UpgradeModel.class);

        context = UpgradePlanDetailsActivity.this;
        sessionManager = new SessionManager(context);
        setData();
        listener();
        Checkout.clearUserData(context);
        setContentView(b.getRoot());
    }


    private void listener() {
        b.tvPayAmountUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });
    }


    private void setData() {
        b.tvMembershipPlan.setText(model.plan_price);
        b.tvMembershipPlan1.setText(model.plan_price);
    }

    public void startPayment() {

        int amount = Math.round(Float.parseFloat(model.plan_price) * 100);

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_QHOsVAlUo7NNwl");
        checkout.setImage(R.drawable.image);

        JSONObject object = new JSONObject();
        try {
            object.put("name", "Saathidaar.com");
            object.put("description", model.plan_name+" Plan Payment");
            object.put("theme.color", "");
            object.put("amount", amount);
            object.put("prefill.contact", sessionManager.getPhone1());
            object.put("prefill.email", sessionManager.getEmail());
            checkout.open(UpgradePlanDetailsActivity.this, object);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(UpgradePlanDetailsActivity.this);

        // Set the message show for the Alert time

        // Set Alert Title
        builder.setTitle("Your Payment Details");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // When the user click yes button
                                // then app will close
                                finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Error: " + s, Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(UpgradePlanDetailsActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Error: " + s);

        // Set Alert Title
        builder.setTitle("Your Payment Details");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // When the user click yes button
                                // then app will close
                                finish();
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }
}