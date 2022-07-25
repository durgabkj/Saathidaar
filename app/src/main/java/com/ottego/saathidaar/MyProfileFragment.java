package com.ottego.saathidaar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.ProfileViewPager;
import com.ottego.saathidaar.Fragment.FamilyInfoFragment;
import com.ottego.saathidaar.Fragment.PersonalInfoFragment;
import com.ottego.saathidaar.Fragment.ProfessionalInfoFragment;
import com.ottego.saathidaar.Model.MemberProfileModel;

import org.json.JSONException;
import org.json.JSONObject;


public class MyProfileFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView tvUserName, tvUserEmail,tvUserDetailsReadLess,tvUserDetailsReadMore,tvAboutUs;
    SessionManager sessionManager;
    ImageView profilePic;
    AppCompatImageView ivClear;
    Context context;
    ScrollView MyProfileDetail;

    public static String url = Utils.memberUrl + "my-profile/";

    MemberProfileModel model;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        context = getContext();
        sessionManager=new SessionManager(context);
        sessionManager = new SessionManager(context);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserName = view.findViewById(R.id.tvUserName);
        tabLayout = view.findViewById(R.id.tlProfile);
        viewPager = view.findViewById(R.id.vpMyProfile);
        tvUserDetailsReadMore=view.findViewById(R.id.tvUserDetailsReadMore);
        tvUserDetailsReadLess=view.findViewById(R.id.tvUserDetailsReadLess);
        tvAboutUs=view.findViewById(R.id.tvAboutUs);
        profilePic=view.findViewById(R.id.profilePic);
       // MyProfileDetail=view.findViewById(R.id.MyProfileDetail);
       tvAboutUs.setText(sessionManager.getKeyProAboutus());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        listener();
        getMemberData();
        setPreLoadData();
        return view;

    }

    private void listener() {
        tvUserDetailsReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAboutUs.setMaxLines(100);
                tvUserDetailsReadMore.setVisibility(View.GONE);
                tvUserDetailsReadLess.setVisibility(View.VISIBLE);
            }
        });


        tvUserDetailsReadLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvAboutUs.setMaxLines(6);
                tvUserDetailsReadMore.setVisibility(View.VISIBLE);
                tvUserDetailsReadLess.setVisibility(View.GONE);
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),GalleryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {

        ProfileViewPager adapter = new ProfileViewPager(getChildFragmentManager());
        adapter.addFragment(new PersonalInfoFragment(), "Personal Info");
        adapter.addFragment(new FamilyInfoFragment(), "Family Info");
        adapter.addFragment(new ProfessionalInfoFragment(), "Professional Info");
        viewPager.setAdapter(adapter);
    }

    private void setPreLoadData() {
        tvUserName.setText(sessionManager.getName()+" "+sessionManager.getLastName());
        tvUserEmail.setText(sessionManager.getEmail());
    }



    private void getMemberData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url+sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
               // binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
                        setData();
                    }else {

                        Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    private void setData() {
//        Glide.with(context)
//                .load(Utils.imageUrl+model.profile_photo)
//                .into(profilePic);
    }
}