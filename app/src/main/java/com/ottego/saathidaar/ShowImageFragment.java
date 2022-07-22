package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.ottego.saathidaar.Model.ImageModel;
import com.ottego.saathidaar.databinding.FragmentShowImageBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ShowImageFragment extends Fragment {
FragmentShowImageBinding b;
    Context context;
    SessionManager sessionManager;
    ImageModel model;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ShowImageFragment() {
        // Required empty public constructor
    }

    public static ShowImageFragment newInstance(String param1, String param2) {
        ShowImageFragment fragment = new ShowImageFragment();
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
        b = FragmentShowImageBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);

        Log.e("image_path", mParam1);
        Log.e("image_id", mParam2);
        setData();
        listener();
        return b.getRoot();
    }


    private void listener() {

        b.mtbImage.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_top_add) {
                    deleteImage();
                }
                return false;
            }
        });

        b.tvMakeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfile();
            }
        });


    }

    private void setProfile() {
        String ProfileSetUrl = Utils.memberUrl + "profile/photo";
        HashMap<String, Integer> params = new HashMap<>();

        int memberId = Integer.parseInt(sessionManager.getMemberId());
        int image1 = Integer.parseInt(mParam2);
        params.put("member_id", memberId);
        params.put("image_id", image1);
        Log.e("params image id", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ProfileSetUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" set image response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(getActivity(), "Profile picture set  Successfully", Toast.LENGTH_LONG).show();

//                                //if success then set profile pic...
//                                Glide.with(context)
//                                        .load(mParam2)
//                                        .placeholder(R.drawable.man)
//                                        .into(b.ivProfilePic);

                            } else {
                                Toast.makeText(getActivity(), "Try after sometime....", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
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
    private void deleteImage() {
        String DeleteUrl = Utils.memberUrl + "delete/photo/";
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", mParam2);
        Log.e("params image id", String.valueOf(params));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, DeleteUrl, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(" request sent response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equalsIgnoreCase("1")) {
                                Toast.makeText(getActivity(), "Image Deleted Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Try Again....", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
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
    private void setData() {
        Glide.with(getActivity())
                .load(Utils.imageUrl + mParam1)
                .into(b.ivProfilePic);
    }
}