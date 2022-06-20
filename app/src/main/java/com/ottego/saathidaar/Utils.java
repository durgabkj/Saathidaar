package com.ottego.saathidaar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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
    public static String userUrl ="http://192.168.1.37:9094/api/users/";
    public static String memberUrl ="http://192.168.1.37:9094/api/member/";
    public static  String cityUrl="http://192.168.1.37:9094/api/get/";
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
