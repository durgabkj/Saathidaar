package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
    public String payment = "http://192.168.14.120:9094/upgrade/payment-pay/";
    private static final String TAG = "Razorpay";
    Checkout checkout;
    RazorpayClient razorpayClient;
    Order order;
    private String order_receipt_no = "Receipt No. " +  System.currentTimeMillis()/1000;
    private String order_reference_no = "Reference No. #" +  System.currentTimeMillis()/1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityUpgradePlanDetailsBinding.inflate(getLayoutInflater());
        context = UpgradePlanDetailsActivity.this;
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, UpgradeModel.class);

context=UpgradePlanDetailsActivity.this;
sessionManager=new SessionManager(context);
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


       // payUpgrade();

        Log.e("data", data);
        setData();
        listener();
        setContentView(b.getRoot());
    }



    private void listener() {
        b.tvPayAmountUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUpgrade();
            }
        });
    }

//    private void payAmount() {
//        payUpgrade();
////        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
////        Map<String, String> params = new HashMap<String, String>();
////        params.put("plan_amount", model.plan_price);
////        Log.e("params", String.valueOf(params));
////        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, payment + model.plan_name, new JSONObject(params),
////                new Response.Listener<JSONObject>() {
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        progressDialog.dismiss();
////                        Log.e("response", String.valueOf((response)));
////                        try {
////                            String code = response.getString("results");
////                            if (code.equalsIgnoreCase("1")) {
////                                Gson gson = new Gson();
////                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
////                            } else {
////                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
////                            }
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                            Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
////                        }
////                    }
////                },
////                new Response.ErrorListener() {
////                    @Override
////                    public void onErrorResponse(VolleyError error) {
////                        progressDialog.dismiss();
////                        if (null != error.networkResponse) {
////                            Toast.makeText(context, "Try again......", Toast.LENGTH_LONG).show();
////                            Log.e("Error response", String.valueOf(error));
////                        }
////                    }
////                });
////
////        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
////        MySingleton.myGetMySingleton(context).myAddToRequest(request);
//    }

    private void setData() {
        b.tvMembershipPlan.setText(model.plan_price);
        b.tvMembershipPlan1.setText(model.plan_price);
    }


    private void payUpgrade() {

        // Initialize client
        try {
            razorpayClient = new RazorpayClient(getResources().getString(R.string.razorpay_key_id), getResources().getString(R.string.razorpay_secret_key));
            Checkout.preload(getApplicationContext());
            checkout = new Checkout();
        } catch (RazorpayException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<String, String>();
        razorpayClient.addHeaders(headers);

 float amount = Float.parseFloat(model.plan_price);

        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // amount in the smallest currency unit
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", order_receipt_no);
            orderRequest.put("payment_capture", true);

            order = razorpayClient.Orders.create(orderRequest);

            startPayment(order);


        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void startPayment(Order order) {
        checkout.setKeyID(getResources().getString(R.string.razorpay_key_id));
        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        checkout.setImage(R.drawable.image);

        /**
         * Reference to current activity
         */

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "SaathiDaar.com");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", order_reference_no);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("order_id", order.get("id"));
            options.put("currency", "INR");
            options.put("prefill.contact", sessionManager.getPhone1());
            options.put("prefill.email",sessionManager.getEmail() );

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
           // options.put("amount", "500");

            checkout.open(UpgradePlanDetailsActivity.this, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();


        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(UpgradePlanDetailsActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("Payment ID: " + s +  "\nOrder ID: " + order.get("id")+"\n" + order_reference_no );

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
                                                int which)
                            {

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
                                                int which)
                            {

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
                                                int which)
                            {

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
                                                int which)
                            {

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