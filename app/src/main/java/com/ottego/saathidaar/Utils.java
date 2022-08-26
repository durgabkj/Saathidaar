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
    public static String userUrl = "http://103.174.102.195:8080/saathidaar_backend/api/users/";
    public static String memberUrl = "http://103.174.102.195:8080/saathidaar_backend/api/member/";
    public static String location = "http://103.174.102.195:8080/saathidaar_backend/api/get/";
    public static String privacy = "http://103.174.102.195:8080/saathidaar_backend/api/privacy/update/";
    public static String alert = "http://103.174.102.195:8080/saathidaar_backend/api/alert/";
    public static String imageUrl = "http://103.174.102.195:8080/";
    public static String role_user = "USER";



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


    public static String nullToBlank(String val){
        if(val!=null && !val.equals("null"))
            return val;
        return "";
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



//    public static void deleteImage(Context context,String photo_id) {
//        String DeleteUrl = Utils.memberUrl + "delete/photo/";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("id", photo_id);
//        Log.e("params image id", String.valueOf(params));
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DeleteUrl, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e(" delete image response", String.valueOf((response)));
//                        try {
//                            String code = response.getString("results");
//                            if (code.equalsIgnoreCase("1")) {
//                                Toast.makeText(context, "Image Deleted Successfully,Refresh Gallery", Toast.LENGTH_LONG).show();
////                                Intent intent=new Intent(context,GalleryActivity.class);
////                                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
////                                context.startActivity(intent);
//                            } else {
//                                Toast.makeText(context, "Try Again....", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        if (null != error.networkResponse) {
//                            Log.e("Error response", String.valueOf(error));
//                        }
//                    }
//                });
//
//        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.myGetMySingleton(context).myAddToRequest(request);
//
//    }

    public static void removeShortList(Context context, String member_id,ApiListener apiListener) {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
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
                       // progressDialog.dismiss();
                        Log.e(" Shortlist remove response", String.valueOf((response)));
                        apiListener.onSuccess(0);
                    }
                },
                error -> {
                    if (null != error.networkResponse) {
                      //  progressDialog.dismiss();
                        Log.e("Error response", String.valueOf(error));
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void shortList(Context context, String member_id,ApiListener listener) {
      //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
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
                      //  progressDialog.dismiss();
                        Log.e(" Shortlist response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                listener.onSuccess(0);
                                Toast.makeText(context," Profile Short Listed",Toast.LENGTH_LONG).show();
                            } else {
                                listener.onFail(0);
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
                       // progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void acceptRequest(Context context, String member_id,ApiListener apiListener) {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
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
                      //  progressDialog.dismiss();
                        Log.e(" request sent response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                apiListener.onSuccess(0);
                                Toast.makeText(context,"You accepted Request ",Toast.LENGTH_LONG).show();
                            } else {
                                apiListener.onFail(0);
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
                       // progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void deleteRequest(Context context, String member_id, ApiListener apiListener) {
       final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        String url = Utils.memberUrl + "request-accept-reject";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id",member_id);
        params.put("request_to_id",new SessionManager(context).getMemberId());
        params.put("request_status","Canceled");
        Log.e("params request delete", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                     progressDialog.dismiss();
                        Log.e(" request delete response", String.valueOf((response)));
                        try {
                            String code = response.getString("message");
                            if (code.equalsIgnoreCase("request are Rejected..")) {
                                apiListener.onSuccess(0);
                                Toast.makeText(context,"Request Rejected",Toast.LENGTH_LONG).show();
                            } else {
                                apiListener.onFail(0);
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
      //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
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
                     //   progressDialog.dismiss();
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
                       // progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }

    public static void blockMember(Context context, String member_id, ApiListener apiListener) {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "please wait....", false, false);
        String BlockUrl = Utils.memberUrl + "block-member";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id",(member_id));
        params.put("request_to_id",new SessionManager(context).getMemberId() );
        params.put("block_by_id", new SessionManager(context).getMemberId());
        params.put("block_status","Block");
        Log.e("params request sent", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BlockUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       progressDialog.dismiss();
                        Log.e(" block response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                apiListener.onSuccess(0);
                                Toast.makeText(context, "Profile Blocked",Toast.LENGTH_LONG).show();
                            } else {
                                apiListener.onFail(0);
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

    public static void UnblockMember(Context context, String member_id, ApiListener apiListener) {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        String BlockUrl = Utils.memberUrl + "block-member";
        Map<String, String> params = new HashMap<String, String>();
        params.put("request_from_id", member_id);
        params.put("request_to_id", new SessionManager(context).getMemberId());
        params.put("block_by_id", new SessionManager(context).getMemberId());
        params.put("block_status","Un-Block");
        Log.e("params request sent", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, BlockUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                     //   progressDialog.dismiss();
                        Log.e(" request sent response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                apiListener.onSuccess(0);
                                Toast.makeText(context, "Profile Unblocked",Toast.LENGTH_LONG).show();
                            } else {
                                apiListener.onFail(0);
                                Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();
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
                       // progressDialog.dismiss();
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
