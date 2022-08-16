package com.ottego.saathidaar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.ActivateModel;
import com.ottego.saathidaar.Model.HideUnHideModel;
import com.ottego.saathidaar.databinding.FragmentHideDeleteProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HideDeleteProfileFragment extends Fragment {
public  String hideUrl=Utils.memberUrl+"hide/";
    public  String activateDeacUrl=Utils.memberUrl+"activate/";
    public  String getHideUnhide=Utils.memberUrl+"get/hide/";
    public  String getActivate=Utils.memberUrl+"get/activate/";
    FragmentHideDeleteProfileBinding b;
   Context context;
   ActivateModel model1;
   HideUnHideModel model;
   SessionManager sessionManager;
   String Activate_deactivate;
   String hide;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public HideDeleteProfileFragment() {
        // Required empty public constructor
    }
    public static HideDeleteProfileFragment newInstance(String param1, String param2) {
        HideDeleteProfileFragment fragment = new HideDeleteProfileFragment();
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
       b=FragmentHideDeleteProfileBinding.inflate(getLayoutInflater());


       context=getContext();
       sessionManager=new SessionManager(context);
       listener();
        getData();
        getDataActivate();
       return b.getRoot();
    }
    private void getDataActivate() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getActivate+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                try {
                    if (response.getInt("results")==1) {
                        model1 = gson.fromJson(String.valueOf(response), ActivateModel.class);
                        setData1();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
    private void setData1() {
        if (model1.results!=null && model1.results.equals("1")){
            b.rbActivate.setChecked(true);
        }else if (model1.results!=null && model1.results.equals("0")){
            b.rbDeactivate.setChecked(true);
        }
    }
    private void getData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getHideUnhide+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf((response)));
                Gson gson = new Gson();
                try {
                    if (response.getInt("results")==1) {
                        model = gson.fromJson(String.valueOf(response), HideUnHideModel.class);
                        setData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
        if (model.months!=null && model.months.equals("1")){
            b.rbHideUnhideOneMonth.setChecked(true);
        }else if (model.months!=null && model.months.equals("3")){
            b.rbHideUnhideThreeMonth.setChecked(true);
        }else if (model.months!=null && model.months.equals("6")) {
            b.rbHideUnhideSixMonth.setChecked(true);
        }else if (model.months!=null && model.months.equalsIgnoreCase("unhide")) {
            b.rbHide.setChecked(true);
        }
    }
    private void listener() {
        b.tvCancelHideUnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                b.chkbUnHide.setVisibility(View.VISIBLE);
                b.radioGroupHide.setVisibility(View.VISIBLE);
                b.radioGroupHide.clearCheck();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        b.tvCancelHideUnhide1.performClick();
                    }
                }, 100);

            }
        });


        b.tvCancelHideUnhide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  b.rbHide.setVisibility(View.VISIBLE);
                b.v1.setVisibility(View.VISIBLE);
                b.rbHideUnhideThreeMonth.setVisibility(View.VISIBLE);
                b.rbHideUnhideOneMonth.setVisibility(View.VISIBLE);
                b.rbHideUnhideSixMonth.setVisibility(View.VISIBLE);
                b.v1.setVisibility(View.VISIBLE);
                b.chkbUnHide.setChecked(false);
            }
        });

        b.rbHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    b.chkbUnHide.setVisibility(View.GONE);
                    b.v1.setVisibility(View.GONE);
                    b.rbHideUnhideThreeMonth.setVisibility(View.GONE);
                    b.rbHideUnhideOneMonth.setVisibility(View.GONE);
                    b.rbHideUnhideSixMonth.setVisibility(View.GONE);
                    b.v1.setVisibility(View.GONE);
                }
            }

        });



        b.radioGroupHide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbHideUnhideOneMonth:
                        hide = "1";
                        b.chkbUnHide.setChecked(true);
                        b.rbHide.setVisibility(View.GONE);
                        break;

                    case R.id.rbHideUnhideThreeMonth:
                        hide = "3";
                        b.chkbUnHide.setChecked(true);
                        b.rbHide.setVisibility(View.GONE);
                        break;
                    case R.id.rbHideUnhideSixMonth:
                        hide = "6";
                        b.chkbUnHide.setChecked(true);
                        b.rbHide.setVisibility(View.GONE);
                        break;
                    case R.id.rbHide:
                        hide = "unhide";
                        break;

                }
            }
        });



        b.radioGroupActivateDea.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbActivate:
                        Activate_deactivate = "1";
                        break;

                    case R.id.rbDeactivate:
                        Activate_deactivate = "0";
                        break;
                }
            }
        });
        
        b.tvSaveHideUnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
        b.tvSaveAcDe.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                submitFormActivate();
            }
        });
    }
    private void submitFormActivate() {
       // final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("activate_id",Activate_deactivate);
        params.put("member_ID",sessionManager.getMemberId());
        Log.e("params ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, activateDeacUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Something went wrong...Try Again", Toast.LENGTH_SHORT).show();
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
    private void submitForm() {
     //   final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("hide_period_time_month",hide);
        Log.e("params ", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, hideUrl+sessionManager.getMemberId(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {String code = response.getString("result");
                            if (code.equalsIgnoreCase("1")) {
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
                       // progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);

    }


}