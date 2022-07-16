package com.ottego.saathidaar;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberPreferenceModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.FragmentMatchDetailsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MatchDetailsFragment extends Fragment {
    FragmentMatchDetailsBinding b;
    Animation animation;
    // NewMatchesModel model;
    MemberPreferenceModel memberPreferenceModel;
    SessionManager sessionManager;
    MemberProfileModel model;
    public String memberDetail = Utils.memberUrl + "get-details/";
    public String PreferenceDetailUrl = Utils.memberUrl + "match/preference/";
    Context context;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MatchDetailsFragment newInstance(String param1, String param2) {
        MatchDetailsFragment fragment = new MatchDetailsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b = FragmentMatchDetailsBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);
        listener();
        getData();
        setData();
        getMemberPreferenceData();
        return b.getRoot();
    }

    private void getMemberPreferenceData() {

        //   Map<String, String> params = new HashMap<String, String>();
//        params.put("member_ID",mParam1);
        Log.e("params", String.valueOf(mParam1));
        Log.e("prefParams", PreferenceDetailUrl + mParam1 + "/" + sessionManager.getMemberId());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, PreferenceDetailUrl + mParam1 + "/" + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    if (response != null) {
                        Gson gson = new Gson();
                        memberPreferenceModel = gson.fromJson(String.valueOf(response), MemberPreferenceModel.class);
                        setMemberPrefData();
                    } else {
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
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }

    private void setMemberPrefData() {

        b.matchPreferenceCount.setText("You Match " + memberPreferenceModel.match_count + "/" + memberPreferenceModel.total_preference + " of " + " " + memberPreferenceModel.gender_preference);
        b.tvDetailAgeMatch.setText(memberPreferenceModel.partner_age);
        b.tvDetailHeightMatch.setText(memberPreferenceModel.partner_height);
        b.tvDetailMaritalStatusMatch.setText(memberPreferenceModel.partner_marital_status);
        b.tvDetailCountryMatch.setText(memberPreferenceModel.partner_country);

        b.tvDetailStateMatch.setText(memberPreferenceModel.partner_state);
        b.tvDetailEducationField.setText(memberPreferenceModel.partner_qualification);
        b.tvDetailWorkingWithMatch.setText(memberPreferenceModel.partner_working_with);
        b.tvDetailAnnualIncome.setText(memberPreferenceModel.partner_annual_income);


        b.tvDetailReligionMatch.setText(memberPreferenceModel.partner_religions);
        b.tvDetailMotherTongueMatch.setText(memberPreferenceModel.partner_mother_tongue);
        b.tvDetailcityMatch.setText(memberPreferenceModel.partner_city);
        b.tvDetailAnnualIncome.setText(memberPreferenceModel.partner_annual_income);


        if (memberPreferenceModel.my_age.equalsIgnoreCase("Yes")) {
            b.cvcheck1.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_age.equalsIgnoreCase("NO")) {
            b.cvClear1.setVisibility(View.VISIBLE);
        } else {
            b.cvDot1.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_height.equalsIgnoreCase("Yes")) {
            b.cvcheck2.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_height.equalsIgnoreCase("NO")) {
            b.cvClear2.setVisibility(View.VISIBLE);
        } else {
            b.cvDot2.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_marital_status.equalsIgnoreCase("Yes")) {
            b.cvcheck3.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_marital_status.equalsIgnoreCase("NO")) {
            b.cvClear3.setVisibility(View.VISIBLE);
        } else {
            b.cvDot3.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_country.equalsIgnoreCase("Yes")) {
            b.cvcheck4.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_country.equalsIgnoreCase("NO")) {
            b.cvClear4.setVisibility(View.VISIBLE);
        } else {
            b.cvDot4.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_state.equalsIgnoreCase("Yes")) {
            b.cvcheck5.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_state.equalsIgnoreCase("NO")) {
            b.cvClear5.setVisibility(View.VISIBLE);
        } else {
            b.cvDot5.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_qualification.equalsIgnoreCase("Yes")) {
            b.cvcheck6.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_qualification.equalsIgnoreCase("NO")) {
            b.cvClear6.setVisibility(View.VISIBLE);
        } else {
            b.cvDot6.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_working_with.equalsIgnoreCase("Yes")) {
            b.cvcheck7.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_working_with.equalsIgnoreCase("NO")) {
            b.cvClear7.setVisibility(View.VISIBLE);
        } else {
            b.cvDot7.setVisibility(View.VISIBLE);
        }


//        if(memberPreferenceModel.my_working_with.equalsIgnoreCase("Yes")){
//            b.cvcheck8.setVisibility(View.VISIBLE);
//        }else if(memberPreferenceModel.my_working_with.equalsIgnoreCase("NO"))
//        {
//            b.cvClear8.setVisibility(View.VISIBLE);
//        }else
//        {
//            b.cvDot8.setVisibility(View.VISIBLE);
//        }


        if (memberPreferenceModel.my_annual_income.equalsIgnoreCase("Yes")) {
            b.cvcheck9.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_annual_income.equalsIgnoreCase("NO")) {
            b.cvClear9.setVisibility(View.VISIBLE);
        } else {
            b.cvDot9.setVisibility(View.VISIBLE);
        }


        if (memberPreferenceModel.my_religions.equalsIgnoreCase("Yes")) {
            b.cvcheck10.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_religions.equalsIgnoreCase("NO")) {
            b.cvClear10.setVisibility(View.VISIBLE);
        } else {
            b.cvDot10.setVisibility(View.VISIBLE);
        }

        if (memberPreferenceModel.my_mother_tongue.equalsIgnoreCase("Yes")) {
            b.cvcheck11.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_mother_tongue.equalsIgnoreCase("NO")) {
            b.cvClear11.setVisibility(View.VISIBLE);
        } else {
            b.cvDot11.setVisibility(View.VISIBLE);
        }

//        if(memberPreferenceModel.my_m.equalsIgnoreCase("Yes")){
//            b.cvcheck12.setVisibility(View.VISIBLE);
//        }else if(memberPreferenceModel.my_mother_tongue.equalsIgnoreCase("NO"))
//        {
//            b.cvClear11.setVisibility(View.VISIBLE);
//        }else
//        {
//            b.cvDot11.setVisibility(View.VISIBLE);
//        }

        if (memberPreferenceModel.my_city.equalsIgnoreCase("Yes")) {
            b.cvcheck13.setVisibility(View.VISIBLE);
        } else if (memberPreferenceModel.my_city.equalsIgnoreCase("NO")) {
            b.cvClear13.setVisibility(View.VISIBLE);
        } else {
            b.cvDot13.setVisibility(View.VISIBLE);
        }

    }

    private void listener() {
        b.llBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        b.tvViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvAboutUserDetails.setMaxLines(20);
                b.tvViewLess.setVisibility(View.VISIBLE);
                b.tvViewMore.setVisibility(View.GONE);

            }
        });

        b.tvViewLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvAboutUserDetails.setMaxLines(4);
                b.tvViewLess.setVisibility(View.GONE);
                b.tvViewMore.setVisibility(View.VISIBLE);
            }
        });


//


    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
//        params.put("member_ID",mParam1);
//        Log.e("params", String.valueOf(params));
        Log.e("dataParams", memberDetail + mParam1);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, memberDetail + mParam1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        model = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
                        setData();
                    } else {
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
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);

    }
    private void setData() {
        if (model != null) {
            b.tvNewMatchName.setText(model.first_name + " " + model.last_name);
            b.tvNewMatchAge.setText(model.age + " yrs");
            b.tvNewMatchHeight.setText(model.height + " feet");
            b.tvMatchCityDetail.setText(model.city);
            b.tvNewMatchWorkAsDetail.setText(model.working_as);
            b.tvNameUserDetails.setText("About" + "  " + model.first_name);
            b.tvAboutUserDetails.setText(model.about_ourself);

            b.tvCreatedBy.setText("Profile CreateBy" + " " + model.profilecreatedby);
            b.tvProfileID.setText("Profile ID" + " " + model.profile_id);
            b.tvDetailAge.setText(model.age + " yrs old");
            b.tvDetailHeight.setText("Height - " + model.height);
            b.tvDetailDob.setText("Born on" + " " + model.date_of_birth);
            b.tvDetailMaritalS.setText(model.marital_status);
            b.tvDetailLiveIn.setText("Live in" + " " + model.city + "." + model.state_name + "," + model.country_name);
            b.tvDetailReligionMotherTongue.setText(model.religion_name + "," + model.mother_tounge);
            b.tvDetailCommunity.setText(model.caste_name + "," + model.sub_caste_name);
            b.tvDetailDiet.setText(model.lifestyles);
            b.tvDetailProfession.setText(model.working_as);
            b.tvDetailCompanyName.setText(model.working_with);
            b.tvDetailAnnualIncome.setText("Earn " + model.annual_income);
            b.tvDetailEducationField.setText(model.highest_qualification);
            b.tvDetailCollege.setText(model.college_attended);
        }


    }
}