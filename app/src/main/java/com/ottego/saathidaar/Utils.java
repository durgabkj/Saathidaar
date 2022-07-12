package com.ottego.saathidaar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.SessionModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class Utils {
    public static int SERVER_TIMEOUT = 30000;
    public static String userUrl ="http://192.168.1.38:9094/api/users/";
    public static String memberUrl ="http://192.168.1.38:9094/api/member/";
    public static  String location ="http://192.168.1.38:9094/api/get/";
    public static String privacy="http://192.168.1.38:9094/api/privacy/update/";
    public static String alert= "http://192.168.1.38/api/alert/";
    public static String role_user="USER";



    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public final static boolean isValidMobile(CharSequence target) {
        if (target == null || target.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    public static String utfString(String string) {
        try {
            return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




    public static void sendDeviceId(final Context context) {
        final String url_device = Utils.userUrl + "deviceidset.php";
        //final SessionManager sessionManager = new SessionManager(context);
        SharedPreferences pref = context.getSharedPreferences("firebase_sh", 0);
        final String firebaseId = pref.getString("firebaseid", null);

        class SendDeviceId extends AsyncTask<String, Void, String> {
            protected String doInBackground(String... urls) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url_device, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        // params.put("uid", sessionManager.());
                        params.put("deviceid", firebaseId);
                        return params;
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
               MySingleton.myGetMySingleton(context).myAddToRequest(stringRequest);
                return null;
            }
        }
        new SendDeviceId().execute();
    }


//    private void sendSms() {
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.e(" Enquiry response", response);
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String code = jsonObject.getString("result");
//                    if (code.equalsIgnoreCase("1")) {
//
////                       Intent intent = new Intent(context, OtpVerificationActivity.class);
//                        // intent.putExtra("id", jsonObject.getString("UserID"));
////                        intent.putExtra("mobile", mobile);
////                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        // startActivity(intent);
//                        Toast.makeText(context, "Mess Enquiry", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                        Toast.makeText(context, "Sorry, something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("apikey", Utils.API_KEY);
//                params.put("MessID", model.MemberID);
//                params.put("UserID", sessionManager.getId());
//                params.put("SendSMS", "1");
//                Log.e("Enquiry perms", String.valueOf(params));
//                return params;
//            }
//        };
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.myGetMySingleton(context).myAddToRequest(stringRequest);
//    }



    public static void removeShortList(Context context, String member_id) {
           String url = Utils.memberUrl + "remove-to-shortlist";
        Map<String, String> params = new HashMap<String, String>();
        params.put("shortlist_from_id",new SessionManager(context).getMemberId());
        params.put("shortlist_to_id",member_id);
        params.put("shortlist_status","remove");
        Log.e("params  remove shortlist", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" Shortlist remove response", String.valueOf((response)));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }
    public static void shortList(Context context, String member_id) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        String url = Utils.memberUrl + "add-to-shortlist";
        Map<String, String> params = new HashMap<String, String>();
        params.put("shortlist_from_id",new SessionManager(context).getMemberId());
        params.put("shortlist_to_id",member_id );
        params.put("shortlist_status","add");
        Log.e("params shortlist", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e(" Shortlist response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(context,"Short Listed",Toast.LENGTH_LONG).show();
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }
    public static void acceptRequest(Context context, String member_id) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        String url = Utils.memberUrl + "request-accept-reject";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id",member_id );
        params.put("request_to_id",new SessionManager(context).getMemberId());
        params.put("request_status","Accepted");
        Log.e("params request accept", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e(" request sent response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (code.equalsIgnoreCase("request are Accepted..")) {
                                Toast.makeText(context,"Request accepted ",Toast.LENGTH_LONG).show();
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void deleteRequest(Context context, String member_id) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        String url = Utils.memberUrl + "request-accept-reject";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id",member_id );
        params.put("request_to_id",new SessionManager(context).getMemberId());
        params.put("request_status","Rejected");
        Log.e("params request delete", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e(" request delete response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (code.equalsIgnoreCase("request are Deleted..")) {
                                Toast.makeText(context,"Request accepted ",Toast.LENGTH_LONG).show();
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void sentRequest(Context context, String member_id) {
         String url = Utils.memberUrl + "send-request";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id", new SessionManager(context).getMemberId());
        params.put("request_to_id", member_id);
        params.put("request_status","Pending");
        Log.e("params request sent", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" request sent response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(context,"Request Send Successfully",Toast.LENGTH_LONG).show();
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
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static String getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(Objects.requireNonNull(sdf.parse(date)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static Long getDateInMilliSecond(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        final long time = sdf.parse(date).getTime();

        return time;
    }


    public static String getTimeInMonth(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
        SimpleDateFormat myFormat = new SimpleDateFormat("hh:mm a", Locale.US);

        String reformattedStr = "";
        try {
            reformattedStr = myFormat.format(Objects.requireNonNull(sdf.parse(time)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return reformattedStr;
    }

    public static String getDateFromMilliSec(Long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(" HH:mm", Locale.US);
        String dateString = formatter.format(new Date(time));
        return dateString;
    }



}
