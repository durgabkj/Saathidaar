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
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.Model.RazorPayModel;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.Model.UpgradeModel;
import com.ottego.saathidaar.databinding.ActivityUpgradePlanDetailsBinding;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.Order;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpgradePlanDetailsActivity extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    ActivityUpgradePlanDetailsBinding b;
    UpgradeModel model;
    SessionManager sessionManager;
    Context context;
    Checkout checkout ;
    RazorPayModel razorPayModel;
    public String payment = Utils.memberUrl+"upgrade/plan";
    public String orderUrl = "http://103.174.102.195:8080/saathidaar_backend/api/createPayment";
    private static final String TAG = "Razorpay";
    private AlertDialog.Builder alertDialogBuilder;

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


        alertDialogBuilder = new AlertDialog.Builder(UpgradePlanDetailsActivity.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        Checkout.preload(getApplicationContext());
        setData();
        orderIdCreate();
        Checkout.clearUserData(context);
        setContentView(b.getRoot());
    }

    private void setData() {
        if(model!=null && model.features != null && model.features.size() > 0)
        {
            b.tvMembershipPlan.setText(model.plan_price);
            b.tvMembershipPlan1.setText(model.plan_price);
            b.tvPlanFearure.setText(model.features.get(0).features_name);
            b.tvPlanFearure1.setText(model.features.get(1).features_name);
            b.tvPlanFearure2.setText(model.features.get(2).features_name);
            b.tvPlanFearure3.setText(model.features.get(3).features_name);
            b.tvPlanFearure4.setText(model.features.get(4).features_name);
            b.tvPlanFearure5.setText(model.features.get(5).features_name);
            b.tvPlanFearure6.setText(model.features.get(6).features_name);
            b.tvPlanFearure7.setText(model.features.get(7).features_name);


            if (model.features.get(0).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature.setVisibility(View.VISIBLE);
            }

            if (model.features.get(1).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature1.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature1.setVisibility(View.VISIBLE);
            }

            if (model.features.get(2).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature2.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature2.setVisibility(View.VISIBLE);
            }

            if (model.features.get(3).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature3.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature3.setVisibility(View.VISIBLE);
            }

            if (model.features.get(4).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature4.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature4.setVisibility(View.VISIBLE);
            }

            if (model.features.get(5).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature5.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature5.setVisibility(View.VISIBLE);
            }

            if (model.features.get(6).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature6.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature6.setVisibility(View.VISIBLE);
            }

            if (model.features.get(7).features_valid.equalsIgnoreCase("1"))
            {
                b.cvCheckFeature7.setVisibility(View.VISIBLE);
            }else
            {
                b.cvClearFeature7.setVisibility(View.VISIBLE);
            }

        }

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

    private void orderIdCreate(){
          Map<String, String> params = new HashMap<String, String>();
        params.put("amount", "1");
        params.put("customer_name", sessionManager.getName());
        params.put("email", sessionManager.getEmail());
        Log.e("params", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, orderUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();
                        Log.e("order id", String.valueOf((response)));
                        try {
                            String code = response.getString("statusCode");
                            if (code.equalsIgnoreCase("200")) {
                                Gson gson = new Gson();
                               razorPayModel= gson.fromJson(String.valueOf(response), RazorPayModel.class);

                                b.tvPayAmountUpgrade.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startPayment();
                                    }
                                });
                            } else {
                                Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
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
    public void startPayment() {
        Checkout checkout = new Checkout();

        /**
         * Set your logo here
         */
        // set image
        checkout.setImage(R.drawable.logo1);
        checkout.setKeyID("rzp_test_QHOsVAlUo7NNwl");
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "Saathidaar.com");
            options.put("description", "Plan Purchase");

            options.put("order_id", razorPayModel.razorPay.razorpayOrderId);//from response of step 3.
            options.put("theme.color", "#742041");
            options.put("currency", "INR");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
//            options.put("amount", razorPayModel.razorPay.applicationFee/100);//pass amount in currency subunits
            options.put("prefill.email", razorPayModel.razorPay.customerEmail);
            options.put("prefill.contact",sessionManager.getPhone1());
            JSONObject retryObj = new JSONObject();

            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}