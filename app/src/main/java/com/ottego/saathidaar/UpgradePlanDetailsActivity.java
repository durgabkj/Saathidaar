package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.SessionModel;
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
    public String payment = Utils.memberUrl+"upgrade/plan";
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

    private void updatePayment(){
          //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
            Map<String, String> params = new HashMap<String, String>();
            params.put("member_id", sessionManager.getMemberId());
            params.put("plan_name", model.plan_name);
        params.put("plan_amount", "model.plan_price");
        params.put("paymentId", "dtfyghjghg");
            Log.e("params", String.valueOf(params));
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, payment, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //progressDialog.dismiss();
                            Log.e("response", String.valueOf((response)));
                            try {
                                String code = response.getString("results");
                                if (code.equalsIgnoreCase("1")) {
                                    Gson gson = new Gson();
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                         //   progressDialog.dismiss();
                            if (null != error.networkResponse) {
                                Toast.makeText(context,"Try again......",Toast.LENGTH_LONG).show();
                                Log.e("Error response", String.valueOf(error));
                            }
                        }
                    });

            request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.myGetMySingleton(context).myAddToRequest(request);
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
            object.put("amount", 1*100);
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
        updatePayment();

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

    @SuppressLint("ResourceAsColor")
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
        Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setTextColor(R.color.colorPrimary);
    }

}