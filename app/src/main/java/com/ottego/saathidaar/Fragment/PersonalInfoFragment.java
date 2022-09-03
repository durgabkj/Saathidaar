package com.ottego.saathidaar.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.HoroscopeModel;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.MySingleton;
import com.ottego.saathidaar.ProfileEditPersonalActivity;
import com.ottego.saathidaar.R;
import com.ottego.saathidaar.SessionManager;
import com.ottego.saathidaar.Utils;
import com.ottego.saathidaar.databinding.FragmentPersonalInfoBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;


public class PersonalInfoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static String url = Utils.memberUrl + "my-profile/";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public String urlGetHoroscope = Utils.memberUrl + "horoscope/get/";
    FragmentPersonalInfoBinding binding;
    Context context;
    MemberProfileModel model;
    SessionManager sessionManager;
    HoroscopeModel horoscopeModel;
    String id = "";
    Address address;
    int count = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PersonalInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PersonalInfoFragment newInstance(String param1, String param2) {
        PersonalInfoFragment fragment = new PersonalInfoFragment();
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
        binding = FragmentPersonalInfoBinding.inflate(getLayoutInflater());
        context = getContext();
        sessionManager = new SessionManager(context);
        listener();
        getMemberData();
        getData();

        if (sessionManager.getUserGender().equalsIgnoreCase("male")) {
            binding.tvLocatonOfPartner.setText("Location of Groom");
            binding.tvLocatonOfPartner.setPaintFlags(binding.tvLocatonOfPartner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            binding.tvLocatonOfPartner.setText("Location of Bride");
            binding.tvLocatonOfPartner.setPaintFlags(binding.tvLocatonOfPartner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

        return binding.getRoot();
    }


    private void getData() {
        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                urlGetHoroscope + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //  progressDialog.dismiss();
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
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
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    @SuppressLint("SetTextI18n")
    private void setHoroData() {

        if (horoscopeModel != null && !horoscopeModel.equals("")) {
            if ((horoscopeModel.hours != null && !horoscopeModel.hours.isEmpty()) && (horoscopeModel.minutes != null && !horoscopeModel.minutes.isEmpty()) && (horoscopeModel.time != null && !horoscopeModel.time.isEmpty()) && (horoscopeModel.time_status != null && !horoscopeModel.time_status.isEmpty())) {
                binding.tvUserTimeofBirth.setText(horoscopeModel.hours + ":" + horoscopeModel.minutes + " " + horoscopeModel.time + " , " + horoscopeModel.time_status);
            }

            if ((horoscopeModel.country_of_birth != null && !horoscopeModel.country_of_birth.isEmpty()) && (horoscopeModel.city_of_birth != null && !horoscopeModel.city_of_birth.isEmpty())) {
                binding.tvUserHPlaceofBirth.setText(horoscopeModel.country_of_birth + ", " + horoscopeModel.city_of_birth);
            }

        }

    }

    private void listener() {

        binding.srlRecycleViewPersonalDetails.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMemberData();
                getData();
            }
        });

        binding.ivCameraEditPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditPersonalActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                context.startActivity(intent);
//                startActivity(intent);
            }
        });
        binding.ivAddDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditPersonalActivity.class);
                intent.putExtra("data", new Gson().toJson(model));
                context.startActivity(intent);
//                startActivity(intent);
            }
        });

        binding.tvDob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String dob = binding.tvDob.getText().toString().trim();
                if (dob.equalsIgnoreCase("")) {
                    binding.tvGender.setVisibility(View.GONE);
                } else {
                    binding.tvGender.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        binding.tvUserReligion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String religion = binding.tvUserReligion.getText().toString().trim();
                if (religion.equalsIgnoreCase("Hindu")) {
                    binding.llGotra.setVisibility(View.VISIBLE);
                } else {
                    binding.llGotra.setVisibility(View.GONE);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.tvGender.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                String marital = binding.tvUserMaritalStatus.getText().toString().trim();
                if (marital.equalsIgnoreCase("Never Married")) {
                    binding.llChild.setVisibility(View.GONE);
                }


                if (marital.equalsIgnoreCase("Divorced")) {
                    binding.llChild.setVisibility(View.VISIBLE);

                }

                if (marital.equalsIgnoreCase("Widowed")) {
                    binding.llChild.setVisibility(View.VISIBLE);

                }

                if (marital.equalsIgnoreCase("Awaiting Divorce")) {
                    binding.llChild.setVisibility(View.VISIBLE);
                }

                if (marital.equalsIgnoreCase("Married")) {
                    binding.llChild.setVisibility(View.VISIBLE);

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void getMemberData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                //  Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        Gson gson = new Gson();
//                        binding.llNoDataPersonal.setVisibility(View.VISIBLE);
//                        binding.scrvPersonalData.setVisibility(View.GONE);
                        model = gson.fromJson(String.valueOf(response.getJSONObject("data")), MemberProfileModel.class);
                        // SessionProfileDetailModel model = gson.fromJson(String.valueOf(response.getJSONObject("data")), SessionProfileDetailModel.class);
//                        sessionManager.CreateProfileSession(model);
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
                binding.srlRecycleViewPersonalDetails.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    @SuppressLint("ResourceAsColor")
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                AlertDialog.Builder builder
                        = new AlertDialog
                        .Builder(context);
                builder.setTitle(R.string.title_location_permission);
                builder.setMessage(R.string.text_location_permission);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                });
                AlertDialog alertDialog = builder.create();
//                        .create();
                alertDialog.show();
                Button buttonbackground1 = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                buttonbackground1.setTextColor(R.color.colorPrimary);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {


                        // do your stuff here
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                }
                return;
            }

        }
    }


    public LatLng getCityLatitude(Context context, String city) {
        Geocoder geocoder = new Geocoder(context, context.getResources().getConfiguration().locale);
        List<Address> addresses = null;
        LatLng latLng = null;
        try {
            addresses = geocoder.getFromLocationName(city, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0);
            }
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            Log.e("co-ordinates", "dev" + address.getLatitude());
            DecimalFormat df = new DecimalFormat("###.#####");
            String formatted = df.format((address.getLatitude()));
            String formatted1 = df.format((address.getLongitude()));
            binding.tvCoordinate.setText(String.valueOf("Latitude " + formatted) + "° N");
            binding.tvCoordinateLong.setText(String.valueOf("Longitude " + formatted1) + "° E");
        } catch (Exception e) {
            e.printStackTrace();

        }
        return latLng;

    }


    private void setData() {
        if (model != null) {
            binding.tvDob.setText(model.date_of_birth);
            binding.tvUserAge.setText(model.age);
            binding.tvUserMaritalStatus.setText(model.marital_status);
            binding.tvUseNoOfChild.setText(model.no_of_children);
            binding.tvUserHeight.setText(model.height);
            binding.tvBloodGroup.setText(model.blood_group);
            binding.tvUserDiet.setText(model.lifestyles);
            binding.tvUserMotherTongue.setText(model.mother_tounge);
            binding.tvHealthDetail.setText(model.health_info);
            binding.tvUserReligion.setText(model.religion_name);
            binding.tvUserCommunity.setText(model.caste);
            binding.tvGender.setText(new StringBuilder().append(model.gender.substring(0, 1).toUpperCase()).append(model.gender.substring(1)).toString());
            binding.tvUserSubCommunity.setText(model.sub_caste_name);
            binding.tvUserGotra.setText(model.gothra);


            binding.tvUserCurrentResi.setText(model.country_name);
            binding.tvUserStateOfResidence.setText(model.state);
            binding.tvUserResidenceStatus.setText(model.city);
            binding.tvUserPinCode.setText(model.pincode);
            binding.tvUserorigin.setText(model.ethnic_corigin);


            binding.tvCountryOfBirth.setText(model.country_of_birth);
            binding.tvCityofBirth.setText(model.city_of_birth);
            binding.tvDateofBirth.setText(model.date_of_birth);
            if ((model.hours != null && !model.hours.equals("")) && (model.minutes != null && !model.minutes.equals("")) && (model.time != null && !model.time.equals("")) && (model.time_status != null || !model.time_status.equals(""))) {
                binding.tvTimeofBirth.setText(Utils.nullToBlank(model.hours) + ":" + Utils.nullToBlank(model.minutes) + " " + Utils.nullToBlank(model.time) + ", " + Utils.nullToBlank(model.time_status));

            }
            binding.tvManglik.setText(model.manglik);


            if (checkLocationPermission()) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff here
                    getCityLatitude(context, model.city_of_birth);
                }
            }
        }

    }

}
