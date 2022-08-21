package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.NewMatchesAdapter;
import com.ottego.saathidaar.Model.DataModelInbox;
import com.ottego.saathidaar.Model.DataModelNewMatches;
import com.ottego.saathidaar.databinding.FragmentMyMatchBinding;
import com.ottego.saathidaar.viewmodel.NewMatchViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class MyMatchFragment extends Fragment implements ApiListener {
    Context context;
    FragmentMyMatchBinding b;
    SessionManager sessionManager;
    DataModelNewMatches data;
    NewMatchViewModel viewModel;
    public String MyMatchUrl = Utils.memberUrl + "my/matches/";
static  final int ALARM_REQ_CODE=100;
    AlertDialog dialog;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyMatchFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyMatchFragment newInstance(String param1, String param2) {
        MyMatchFragment fragment = new MyMatchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        b = FragmentMyMatchBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(context);
        viewModel = new ViewModelProvider(requireActivity()).get(NewMatchViewModel.class);
        getData("");
        listener();
       // addNotification();


       // setNotification();
        return b.getRoot();
    }




    private void setNotification() {
//        AlarmManager objAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Calendar objCalendar = Calendar.getInstance();
//        objCalendar.setTimeInMillis(System.currentTimeMillis());
//        //objCalendar.set(Calendar.YEAR, objCalendar.get(Calendar.YEAR));
//        objCalendar.set(Calendar.HOUR_OF_DAY, 23);
//        objCalendar.set(Calendar.MINUTE, 7);
//        objCalendar.set(Calendar.SECOND, 0);
//        objCalendar.set(Calendar.MILLISECOND, 0);
//        objCalendar.set(Calendar.AM_PM, Calendar.PM);
//
//        Intent alamShowIntent = new Intent(context,AlarmReceiver.class);
//        PendingIntent alarmPendingIntent = PendingIntent.getActivity(context, 0,alamShowIntent,0 );
//
//        objAlarmManager.set(AlarmManager.RTC_WAKEUP,objCalendar.getTimeInMillis(), alarmPendingIntent);
//            Log.e("TAG","hello:-"+objCalendar.getTimeInMillis());

//        Intent intent=new Intent(getContext(),MainActivity.class);
//        String CHANNEL_ID="MYCHANNEL";
//        NotificationChannel notificationChannel= null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
//        }
//        PendingIntent pendingIntent=PendingIntent.getActivity(getContext(),1,intent,0);
//        Notification notification= null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            notification = new Notification.Builder(getContext(),CHANNEL_ID)
//                    .setContentText("Heading")
//                    .setContentTitle("subheading")
//                    .setContentIntent(pendingIntent)
//                    .addAction(android.R.drawable.sym_action_chat,"Title",pendingIntent)
//                    .setChannelId(CHANNEL_ID)
//                    .setSmallIcon(android.R.drawable.sym_action_chat)
//                    .build();
//        }
//
//        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.createNotificationChannel(notificationChannel);
//        notificationManager.notify(1,notification);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 54);
        calendar.set(Calendar.SECOND, 0);
        if (calendar.getTime().compareTo(new Date()) < 0) calendar.add(Calendar.HOUR_OF_DAY, 0);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }

    }
    void listener() {
        b.srlRecycleViewMyMatches.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData("");
            }
        });
    }

    public void getData(String id) {
        //final ProgressDialog progressDialog = ProgressDialog.show(context, null, "Data Loading...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                MyMatchUrl + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                // progressDialog.dismiss();
                b.srlRecycleViewMyMatches.setRefreshing(false);
                Log.e("My Matches response", String.valueOf(response));
                Gson gson = new Gson();
                try {
                    if (response.getInt("results")==1) {
                        data = gson.fromJson(String.valueOf(response), DataModelNewMatches.class);
                        viewModel._list.postValue(data.data);
                        setRecyclerView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  progressDialog.dismiss();
                b.srlRecycleViewMyMatches.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        b.rvMyMatches.setLayoutManager(layoutManager);
        b.rvMyMatches.setHasFixedSize(true);
        b.rvMyMatches.setNestedScrollingEnabled(true);
        NewMatchesAdapter adapter = new NewMatchesAdapter(context, data.data, this);
        b.rvMyMatches.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataMatch.setVisibility(View.GONE);
            b.rvMyMatches.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataMatch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccess(int position) {
        getData("");
    }

    @Override
    public void onFail(int position) {

    }
}