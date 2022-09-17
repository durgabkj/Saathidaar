package com.ottego.saathidaar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.Model.MemberPreferenceModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.FragmentInboxDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class InboxDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String memberDetail = Utils.memberUrl + "get-details/";
    public String PreferenceDetailUrl = Utils.memberUrl + "match/preference/";
    public String urlGetHoroscope = Utils.memberUrl + "horoscope/get/";
    FragmentInboxDetailBinding b;
    Animation animation;
    // NewMatchesModel model;
    MemberPreferenceModel memberPreferenceModel;
    SessionManager sessionManager;
    MemberProfileModel model;
    Context context;
    HoroscopeModel horoscopeModel;
    private String mParam1;
    private String mParam2;
    public InboxDetailFragment() {
        // Required empty public constructor
    }

    public static InboxDetailFragment newInstance(String param1, String param2) {
        InboxDetailFragment fragment = new InboxDetailFragment();
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
        b = FragmentInboxDetailBinding.inflate(inflater, container, false);
        context = getContext();
        sessionManager = new SessionManager(context);
        Log.e("params", mParam1);
        listener();
        getData();
         setData();
         //getLoginMemberData();
         getMemberPreferenceData();
         getHoroscopeData();


        b.ivInboxDot.setVisibility(View.VISIBLE);
        b.ivInboxDot1.setVisibility(View.VISIBLE);
        b.ivInboxDot2.setVisibility(View.VISIBLE);
        return b.getRoot();
    }

    private void getHoroscopeData() {
        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                urlGetHoroscope + mParam1, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  progressDialog.dismiss();
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
                        horoscopeModel = gson.fromJson(String.valueOf(response), HoroscopeModel.class);
                        setHoroData();

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
                // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    private void setHoroData() {
        if(horoscopeModel!=null){
            if ((horoscopeModel.hours != null && !horoscopeModel.hours.equals("")) && (horoscopeModel.minutes != null && !horoscopeModel.minutes.equals("")) && (horoscopeModel.time != null && !horoscopeModel.time.equals("")) && (horoscopeModel.country_of_birth != null || !horoscopeModel.country_of_birth.equals("")) && (horoscopeModel.city_of_birth != null || !horoscopeModel.city_of_birth.equals(""))) {
                b.tvInvboxDetailPlaceOfBirth.setText(horoscopeModel.country_of_birth + " ," + horoscopeModel.city_of_birth);
                b.tvInboxDetailTime.setText(horoscopeModel.hours + " :" + horoscopeModel.minutes + "," + horoscopeModel.time + "," + horoscopeModel.time_status);

            }

            if (!horoscopeModel.manglik.equalsIgnoreCase("") && !horoscopeModel.manglik.equalsIgnoreCase(null) && !horoscopeModel.manglik.isEmpty()) {

                b.tvInboxDetailManglik.setText(horoscopeModel.manglik);
            } else {
                b.tvInboxDetailManglik.setText("Not Specified");
            }
        }
    }

    private void getMemberPreferenceData() {

        //   Map<String, String> params = new HashMap<String, String>();
//        params.put("member_ID",mParam1);
        Log.e("params", String.valueOf(mParam1));
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
        if (memberPreferenceModel != null) {

            b.matchPreferenceCount.setText("You Match " + Utils.nullToBlank(memberPreferenceModel.match_count) + "/" + Utils.nullToBlank(memberPreferenceModel.app_total_preference) + " of " + " " + Utils.nullToBlank(memberPreferenceModel.gender_preference));
            b.tvDetailAgeMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_age));
            b.tvDetailHeightMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_height));
            b.tvDetailMaritalStatusMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_marital_status));
            b.tvDetailCountryMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_country));

            b.tvDetailStateMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_state));
            b.tvDetailEducationMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_qualification));
            b.tvDetailWorkingWithMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_working_with));


            b.tvDetailReligionMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_religions));
            b.tvDetailMotherTongueMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_mother_tongue));
            b.tvDetailcityMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_city));
            b.tvDetailIncomeMatch.setText(Utils.nullToBlank(memberPreferenceModel.partner_annual_income));

            if (memberPreferenceModel.my_age != null) {
                if (memberPreferenceModel.my_age.equalsIgnoreCase("Yes")) {
                    b.cvcheck1.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_age.equalsIgnoreCase("NO")) {
                    b.cvClear1.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot1.setVisibility(View.VISIBLE);
                }
            } else {
                b.llAgeMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_height != null) {
                if (memberPreferenceModel.my_height.equalsIgnoreCase("Yes")) {
                    b.cvcheck2.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_height.equalsIgnoreCase("NO")) {
                    b.cvClear2.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot2.setVisibility(View.VISIBLE);
                }
            } else {
                b.llHeightMatch.setVisibility(View.GONE);
            }

            if (memberPreferenceModel.my_marital_status != null) {
                if (memberPreferenceModel.my_marital_status.equalsIgnoreCase("Yes")) {
                    b.cvcheck3.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_marital_status.equalsIgnoreCase("NO")) {
                    b.cvClear3.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot3.setVisibility(View.VISIBLE);
                }
            } else {
                b.llMaritalMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_country != null) {
                if (memberPreferenceModel.my_country.equalsIgnoreCase("Yes")) {
                    b.cvcheck4.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_country.equalsIgnoreCase("NO")) {
                    b.cvClear4.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot4.setVisibility(View.VISIBLE);
                }
            } else {
                b.llCountryMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_state != null) {
                if (memberPreferenceModel.my_state.equalsIgnoreCase("Yes")) {
                    b.cvcheck5.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_state.equalsIgnoreCase("NO")) {
                    b.cvClear5.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot5.setVisibility(View.VISIBLE);
                }
            } else {
                b.llStateMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_qualification != null) {
                if (memberPreferenceModel.my_qualification.equalsIgnoreCase("Yes")) {
                    b.cvcheck6.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_qualification.equalsIgnoreCase("NO")) {
                    b.cvClear6.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot6.setVisibility(View.VISIBLE);
                }
            } else {
                b.llEducationMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_working_with != null) {
                if (memberPreferenceModel.my_working_with.equalsIgnoreCase("Yes")) {
                    b.cvcheck7.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_working_with.equalsIgnoreCase("NO")) {
                    b.cvClear7.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot7.setVisibility(View.VISIBLE);
                }
            } else {
                b.llWorkingMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_annual_income != null) {
                if (memberPreferenceModel.my_annual_income.equalsIgnoreCase("Yes")) {
                    b.cvcheck9.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_annual_income.equalsIgnoreCase("NO")) {
                    b.cvClear9.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot9.setVisibility(View.VISIBLE);
                }
            } else {
                b.llIncomeMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_religions != null) {
                if (memberPreferenceModel.my_religions.equalsIgnoreCase("Yes")) {
                    b.cvcheck10.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_religions.equalsIgnoreCase("NO")) {
                    b.cvClear10.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot10.setVisibility(View.VISIBLE);
                }
            } else {
                b.llReligionMatch.setVisibility(View.GONE);
            }


            if (memberPreferenceModel.my_mother_tongue != null) {
                if (memberPreferenceModel.my_mother_tongue.equalsIgnoreCase("Yes")) {
                    b.cvcheck11.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_mother_tongue.equalsIgnoreCase("NO")) {
                    b.cvClear11.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot11.setVisibility(View.VISIBLE);
                }
            } else {
                b.llMotherToungeMatch.setVisibility(View.GONE);
            }

            if (memberPreferenceModel.my_city != null) {
                if (memberPreferenceModel.my_city.equalsIgnoreCase("Yes")) {
                    b.cvcheck13.setVisibility(View.VISIBLE);
                } else if (memberPreferenceModel.my_city.equalsIgnoreCase("NO")) {
                    b.cvClear13.setVisibility(View.VISIBLE);
                } else {
                    b.cvDot13.setVisibility(View.VISIBLE);
                }
            } else {
                b.llCityMatch.setVisibility(View.GONE);
            }

        }

    }

    private void listener() {




        b.tvPremiumContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpgradeOnButtonActivity.class);
                context.startActivity(intent);
            }
        });

        b.tvPremiumBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpgradeOnButtonActivity.class);
                context.startActivity(intent);
            }
        });


        b.llShowMemberImageInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MemberGalleryActivity.class);
                intent.putExtra("Member_id",mParam1);
                context.startActivity(intent);
            }
        });

//        b.ivConnectInbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Utils.sentRequest(context,mParam1);
//                b.ivConnectInbox.setVisibility(View.GONE);
//                b.ivDetailsConnectedInbox.setVisibility(View.VISIBLE);
//            }
//        });
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

        b.tvViewMoreFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvAboutUserFamilyDetails.setMaxLines(20);
                b.tvViewLessFamily.setVisibility(View.VISIBLE);
                b.tvViewMoreFamily.setVisibility(View.GONE);

            }
        });

        b.tvViewLessFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvAboutUserFamilyDetails.setMaxLines(2);
                b.tvViewLessFamily.setVisibility(View.GONE);
                b.tvViewMoreFamily.setVisibility(View.VISIBLE);
            }
        });

        b.tvViewLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tvAboutUserDetails.setMaxLines(2);
                b.tvViewLess.setVisibility(View.GONE);
                b.tvViewMore.setVisibility(View.VISIBLE);
            }
        });


        b.tvAboutUserFamilyDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String details = b.tvAboutUserFamilyDetails.getText().toString().trim();
                if (details.equals("")) {
                    b.mcvFamilyDetailInbox.setVisibility(View.GONE);
                } else {
                    b.mcvFamilyDetailInbox.setVisibility(View.VISIBLE);
                }


                int detailsCount =  b.tvAboutUserFamilyDetails.getText().toString().length();
                if (detailsCount<=50) {
                    b.tvViewMoreFamily.setVisibility(View.GONE);
                }else
                {
                    b.tvViewMoreFamily.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        b.tvAboutUserDetails.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String status = b.tvAboutUserDetails.getText().toString().trim();
                if (status.equalsIgnoreCase("")) {
                    b.mcvAboutUsDetailInbox.setVisibility(View.GONE);
                } else {
                    b.mcvAboutUsDetailInbox.setVisibility(View.VISIBLE);
                }

                int AboutDetailsCount =  b.tvAboutUserDetails.getText().toString().length();
                if (AboutDetailsCount<=50) {
                    b.tvViewMore.setVisibility(View.GONE);
                }else
                {
                    b.tvViewMore.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getData() {
        Map<String, String> params = new HashMap<String, String>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, memberDetail + mParam1+ "/" + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
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
            b.llPrivateMatchesDetailPhoto.setVisibility(View.GONE);
            b.ivInboxDot.setVisibility(View.VISIBLE);
            b.ivInboxDot2.setVisibility(View.VISIBLE);

            b.tvNewMatchName.setText(Utils.nullToBlank((model.first_name).substring(0, 1).toUpperCase() + model.first_name.substring(1)) + " " + Utils.nullToBlank(model.last_name).substring(0, 1).toUpperCase().charAt(0));

            if ( model.age!=null && !model.age.equalsIgnoreCase("") && !model.age.equalsIgnoreCase(null) && !model.age.isEmpty()) {
                b.tvNewMatchAge.setText(Utils.nullToBlank(model.age) + " " + "yrs");
            } else {
                b.tvNewMatchAge.setText("Age:-Not Specified");
            }

            if ( model.city!=null && !model.city.equalsIgnoreCase("") && !model.city.equalsIgnoreCase(null) && !model.city.isEmpty()) {
                b.tvMatchCityDetail.setText(Utils.nullToBlank(model.city));
            } else {
                b.tvMatchCityDetail.setText("Not Specified");
            }


            b.tvNewMatchHeight.setText(Utils.nullToBlank(model.height));

            b.tvNewMatchWorkAsDetail.setText(Utils.nullToBlank(model.working_as));
            b.tvNameUserDetails.setText("About" + "  " + Utils.nullToBlank(model.first_name) + " " + Utils.nullToBlank(model.last_name));
            b.tvAboutUserDetails.setText(Utils.nullToBlank(model.about_ourself));
            b.tvNameUserFamilyDetails.setText("About " + " Family");
            b.tvAboutUserFamilyDetails.setText(Utils.nullToBlank(model.FamilyDetails));
            b.tvCreatedBy.setText("Create by:-" + " " + Utils.nullToBlank(model.profilecreatedby));
            b.tvProfileID.setText("Profile ID:-" + " " + Utils.nullToBlank(model.profile_id));

            b.tvDetailHeight.setText("Height:-" + Utils.nullToBlank(model.height));

            if ( model.date_of_birth!=null && !model.date_of_birth.equalsIgnoreCase("") && !model.date_of_birth.equalsIgnoreCase(null) && !model.date_of_birth.isEmpty()) {

                b.tvDetailDob.setText(Utils.nullToBlank(model.date_of_birth));
            } else {
                b.tvDetailDob.setText("Not Specified");
            }


            if ( model.marital_status!=null && !model.marital_status.equalsIgnoreCase("") && !model.marital_status.equalsIgnoreCase(null) && !model.marital_status.isEmpty()) {
                b.tvDetailMaritalS.setText(Utils.nullToBlank(model.marital_status));
            } else {
                b.tvDetailMaritalS.setText("Not Specified");
            }

            String livingIn="";

            if(!Utils.nullToBlank(model.country_name).equals("")){
                livingIn=model.country_name;
            }

            if(!Utils.nullToBlank(model.country_name).equals("") && !Utils.nullToBlank(model.state_name).equals((""))){
                livingIn=livingIn+","+model.state_name;
            }

            if(!Utils.nullToBlank(model.country_name).equals("") && !Utils.nullToBlank(model.state_name).equals(("")) && !Utils.nullToBlank(model.city).equals((""))){
                livingIn=livingIn+","+model.city;
            }

            b.tvDetailLiveIn.setText(Utils.nullToBlank(livingIn));




            String religion="";

            if(!Utils.nullToBlank(model.religion_name).equals("")){
                religion=model.religion_name;
            }

            if(!Utils.nullToBlank(model.religion_name).equals("") && !Utils.nullToBlank(model.mother_tounge).equals((""))){
                religion=religion+","+model.mother_tounge;
            }

            b.tvDetailReligionMotherTongue.setText(religion);

            if (model.caste_name!=null &&!model.caste_name.equalsIgnoreCase("") && !model.caste_name.equalsIgnoreCase(null) && !model.caste_name.isEmpty()) {
                b.tvDetailCommunity.setText(Utils.nullToBlank(model.caste_name) + " " + Utils.nullToBlank(model.sub_caste_name));
            } else {
                b.tvDetailCommunity.setText("Not Specified");
            }


            // b.tvDetailCommunity.setText(Utils.nullToBlank(model.caste_name) + " " + Utils.nullToBlank(model.sub_caste_name));

            if (model.lifestyles!=null && !model.lifestyles.equalsIgnoreCase("") && !model.lifestyles.equalsIgnoreCase(null) && !model.lifestyles.isEmpty()) {
                b.tvDetailDiet.setText(Utils.nullToBlank(model.lifestyles));
            } else {
                b.tvDetailDiet.setText("Not Specified");
            }


            if (model.working_as!=null && !model.working_as.equalsIgnoreCase("") && !model.working_as.equalsIgnoreCase(null) && !model.working_as.isEmpty()) {
                b.tvDetailProfession.setText(Utils.nullToBlank(model.working_as));
            } else {
                b.tvDetailProfession.setText("Not Specified");
            }

            if ( model.working_with!=null && !model.working_with.equalsIgnoreCase("") && !model.working_with.equalsIgnoreCase(null) && !model.working_with.isEmpty()) {
                b.tvDetailCompanyName.setText(Utils.nullToBlank(model.working_with));
            } else {
                b.tvDetailCompanyName.setText("Not Specified");
            }

            if ( model.annual_income!=null && !model.annual_income.equalsIgnoreCase("") && !model.annual_income.equalsIgnoreCase(null) && !model.annual_income.isEmpty()) {
                b.tvDetailAnnualIncome.setText(Utils.nullToBlank(model.annual_income));
            } else {
                b.tvDetailAnnualIncome.setText("Not Specified");
            }

            if ( model.education!=null && !model.education.equalsIgnoreCase("") && !model.education.equalsIgnoreCase(null) && !model.education.isEmpty()) {
                b.tvDetailEducationField.setText(Utils.nullToBlank(model.education));
            } else {
                b.tvDetailEducationField.setText("Not Specified");
            }

            if ( model.college_attended!=null && !model.college_attended.equalsIgnoreCase("") && !model.college_attended.equalsIgnoreCase(null) && !model.college_attended.isEmpty()) {
                b.tvDetailCollege.setText(Utils.nullToBlank(model.college_attended));
            } else {
                b.tvDetailCollege.setText("Not Specified");
            }

            if ( model.highest_qualification!=null && !model.highest_qualification.equalsIgnoreCase("") && !model.highest_qualification.equalsIgnoreCase(null) && !model.highest_qualification.isEmpty()) {
                b.tvDetailHQualification.setText(Utils.nullToBlank(model.highest_qualification));
            } else {
                b.tvDetailHQualification.setText("Not Specified");
            }
            b.tvDetailEmailID.setText(Utils.nullToBlank(model.email_id));
            b.tvDetailCall.setText(Utils.nullToBlank(model.contact_number));
            b.tvImageCountInbox.setText(Utils.nullToBlank(model.images_count));

            if (model.my_profile_photo != null && !model.my_profile_photo.isEmpty()) {
                Glide.with(context)
                        .load(Utils.imageUrl + model.my_profile_photo)
                        .into(b.profileDetailPic);
            } else {
                if (sessionManager.getUserGender().equalsIgnoreCase("male")) {
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__male_)
                            .into(b.profileDetailPic);

                } else {
                    Glide.with(context)
                            .load(R.drawable.ic_no_image__female_)
                            .into(b.profileDetailPic);

                }
            }


            if (model.photo_privacy.equalsIgnoreCase("1")) {
                b.llShowMemberImageInbox.setEnabled(true);
              //  b.flPremiumInboxDetails.setVisibility(View.GONE);
                b.llPremiumMsgInboxDetails.setVisibility(View.GONE);
               // b.tvLevelPremiumInboxDetails.setVisibility(View.GONE);

                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
//                        .transform(!model.profile_photo.equals("")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                        .into(b.ivDetailUserImage);

            } else if (model.photo_privacy.equalsIgnoreCase("3")) {
                b.llShowMemberImageInbox.setEnabled(false);
               // b.flPremiumInboxDetails.setVisibility(View.VISIBLE);
                b.llPremiumMsgInboxDetails.setVisibility(View.GONE);
                b.llPrivateMatchesDetailPhoto.setVisibility(View.VISIBLE);
               // b.tvLevelPremiumInboxDetails.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(b.ivDetailUserImage);
            } else if (model.photo_privacy.equalsIgnoreCase(model.my_premium_status)) {
               // b.flPremiumInboxDetails.setVisibility(View.GONE);
                b.llPremiumMsgInboxDetails.setVisibility(View.GONE);
                b.llShowMemberImageInbox.setEnabled(true);
               // b.tvLevelPremiumInboxDetails.setVisibility(View.GONE);
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .into(b.ivDetailUserImage);
            } else {
                b.llShowMemberImageInbox.setEnabled(false);
             //   b.flPremiumInboxDetails.setVisibility(View.VISIBLE);
                b.llPremiumMsgInboxDetails.setVisibility(View.VISIBLE);
              //  b.tvLevelPremiumInboxDetails.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(b.ivDetailUserImage);
            }


            //preference image set...
            if (model.photo_privacy.equalsIgnoreCase("1")) {
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
//                        .transform(!model.profile_photo.equals("")?new BlurTransformation(20, 8):new BlurTransformation(1, 1))
                        .into(b.profileDetailPic1Partner);

            } else if (model.photo_privacy.equalsIgnoreCase("3")) {
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(b.profileDetailPic1Partner);
            } else if (model.photo_privacy.equalsIgnoreCase(model.my_premium_status)) {
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .into(b.profileDetailPic1Partner);
            } else {
                Glide.with(context)
                        .load(Utils.imageUrl + model.profile_photo)
                        .placeholder(model.gender.equalsIgnoreCase("male") ? R.drawable.ic_no_image__male_ : R.drawable.ic_no_image__female_)
                        .transform(new BlurTransformation(20, 8))
                        .into(b.profileDetailPic1Partner);
            }

            //upgrade premium plans condition

            if (model.premium_status.equalsIgnoreCase("1") && model.my_premium_status.equalsIgnoreCase("2")) {
                b.flPremiumInboxDetails.setVisibility(View.GONE);
                b.tvLevelPremiumInboxDetails.setVisibility(View.GONE);
                b.  tvPremiumBirth.setVisibility(View.GONE);
                b.tvbirth.setVisibility(View.GONE);
                b.tvcontacts.setVisibility(View.GONE);
//            b.tvPremiumBirth.setVisibility(View.GONE);
//            b.tvPremiumCollegeAndCompany.setVisibility(View.GONE);
            }else if(model.premium_status.equalsIgnoreCase("1") && model.my_premium_status.equalsIgnoreCase("0")){
                b.flPremiumInboxDetails.setVisibility(View.VISIBLE);
                b.tvLevelPremiumInboxDetails.setVisibility(View.VISIBLE);
                b.  tvPremiumBirth.setVisibility(View.VISIBLE);
                b.tvbirth.setVisibility(View.VISIBLE);
                b.tvcontacts.setVisibility(View.VISIBLE);
            }
//image count condition
            if (model.images_count.equalsIgnoreCase("0")) {
                b.tvLevelPremiumInboxDetails.setVisibility(View.GONE);
            }


            //dots show condition
//            if((model.age.equalsIgnoreCase(""))  || (model.age.equalsIgnoreCase("null")))
//            {
//                 b.ivInboxDot.setVisibility(View.GONE);
//            }

//            if((model.height.equalsIgnoreCase(""))  || (model.height.equalsIgnoreCase("null")))
//            {
//                b.ivInboxDot1.setVisibility(View.GONE);
//            }

            if((model.city.equalsIgnoreCase(""))  || (model.city.equalsIgnoreCase("null")))
            {
                b.ivInboxDot2.setVisibility(View.GONE);
            }

            if((model.working_as.equalsIgnoreCase(""))  || (model.working_as.equalsIgnoreCase("null")))
            {
                b.ivInboxDot2.setVisibility(View.GONE);
            }


            //show badge of premium

            if (model.premium_status.equalsIgnoreCase("1")) {
                b.flPremiumInboxDetails.setVisibility(View.VISIBLE);
                b.tvLevelPremiumInboxDetails.setVisibility(View.VISIBLE);

            }
        }
        }

}