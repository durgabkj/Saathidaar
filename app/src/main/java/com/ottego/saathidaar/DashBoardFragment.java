package com.ottego.saathidaar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;

import org.json.JSONObject;


public class DashBoardFragment extends Fragment {
    SessionManager sessionManager;
    ImageView ivPremiumImage;
    TextView tvPremiumText, tvLogout, RequestAccept, Visitors, RequestSent;
    int position = 0;
    DataModelDashboard model;
    Animation animation;
    CountDownTimer countDownTimer;
    Context context;
    public String url = "http://192.168.1.37:9094/api/request/count/accept-request/22";
    int[] images = {R.drawable.smartphone, R.drawable.documents, R.drawable.global};
    String[] text = {"phone Number to Connect Instantly", "100% Verified Biodatas", "Find Common connections"};
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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
        View view = inflater.inflate(R.layout.fragment_dash_board, container, false);
        ivPremiumImage = view.findViewById(R.id.ivPremiumImage);
        tvPremiumText = view.findViewById(R.id.tvPremiumText);
        tvLogout = view.findViewById(R.id.tvLogout);

        RequestAccept = view.findViewById(R.id.RequestAccept);
        Visitors = view.findViewById(R.id.Visitors);
        RequestSent = view.findViewById(R.id.RequestSent);
        context = getContext();
        sessionManager = new SessionManager(context);
        set();

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        //  animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = ivPremiumImage.getWidth();
                final float translationX = width * progress;
                ivPremiumImage.setTranslationX(translationX);
                ivPremiumImage.setTranslationX(translationX - width);
                tvPremiumText.setTranslationX(translationX);
                tvPremiumText.setTranslationX(translationX - width);


            }
        });
        animator.start();
        getData();
        //  setData();
        listener();
        return view;

    }


    private void listener() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });

    }

    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                setData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);


    }

    private void setData() {
        RequestAccept.setText(model.data.get(0).accept_request_count);
        RequestSent.setText(model.data.get(0).sent_request_count);
        Visitors.setText(model.data.get(0).recent_visitors_count);

    }

    private void set() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(2 * 3000, 2000) {
            public void onTick(long millisUntilFinished) {
                // Text_t_speech();
                if (position < text.length) {
                    ivPremiumImage.setImageResource(images[position]);
                    tvPremiumText.setText(text[position]);
                    position = position + 1;
                } else {
                    position = 0;
                }
            }

            @Override
            public void onFinish() {
                start();
            }

        }.start();
    }

}