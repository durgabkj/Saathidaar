package com.ottego.saathidaar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.Model.DataModelReligion;
import com.ottego.saathidaar.databinding.ActivityProfileEditPersonalBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProfileEditPersonalActivity extends AppCompatActivity {
    // String currentDate = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
// Permissions for accessing the storage
    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "SelectImageActivity";
    public String ReligionUrl = "http://192.168.1.40:9094/api/get/religion-name";
    ActivityProfileEditPersonalBinding b;
    Context context;
    ArrayList<String> AgeList = new ArrayList<>();
    ArrayList<String> minList = new ArrayList<>();
    ArrayAdapter<String> minAdapter;
    DataModelReligion data;
    ArrayList<String> motherTongueList;
    Dialog dialog;
    //For MaritalStatus....
    ArrayList<String> maritalList = new ArrayList<>();
    String date = "";
    String image = "";
    Calendar calendar = Calendar.getInstance();
    ArrayList<String> religionList = new ArrayList<>();
    ArrayAdapter<String> religionAdapter;
    EditText editText;
    ListView listView;
    private String format = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfileEditPersonalBinding.inflate(getLayoutInflater());
        context = ProfileEditPersonalActivity.this;
        setContentView(b.getRoot());

        // Initialize dialog
        dialog = new Dialog(context);


        // b.mbDatePicker.setText(currentDate);

        listener();
        ageDropDown();
        maritalStatus();
        userHeight();
        bloodGroup();
        motherTongueSpinner();
        limitWord();
        HealthDetails();
        religionList();
        handlePermission();


    }

    private void religionList() {
        b.tvUserReligion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getReligionList(ReligionUrl);

                // Initialize dialog
                dialog = new Dialog(context);

                // set custom dialog
                dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                dialog.getWindow().setLayout(800, 900);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                listView = dialog.findViewById(R.id.list_view);
                EditText editText = dialog.findViewById(R.id.edit_text);
                // Initialize array adapter
                // ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);


                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        religionAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        b.tvUserReligion.setText(religionAdapter.getItem(position));
                        religionList.clear();
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    private void getReligionList(String url) {
        Log.e("url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response", String.valueOf(response));
                try {
                    String code = response.getString("results");
                    if (code.equalsIgnoreCase("1")) {
                        JSONArray jsonArray = response.getJSONArray("religion");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String religion = jsonObject1.getString("religion_name");
                            Log.e("Religion", religion);
                            religionList.add(religion);
                            Log.e("Religion-list", String.valueOf(religionList));
                        }
                    }
                    religionAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, religionList);
                    // set adapter
                    listView.setAdapter(religionAdapter);
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

    private void listener() {

        b.mbDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker v, int year, int month, int day) {
                Calendar userAge = new GregorianCalendar(year, month, day);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                String currentDateString = SimpleDateFormat.getDateInstance(DateFormat.FULL).format(userAge.getTime());
                date = year + "-" + month + "-" + day;
                if (minAdultAge.before(userAge)) {
                    Toast.makeText(context, "Please Select valid date", Toast.LENGTH_LONG).show();
                } else {
                    b.mbDatePicker.setText(currentDateString);
                }
            }
        };

        b.ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

    }

    private void handlePermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    SELECT_PICTURE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case SELECT_PICTURE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /* Choose an image from Gallery */
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (resultCode == RESULT_OK) {
                    if (requestCode == SELECT_PICTURE) {
                        // Get the url from data
                        final Uri selectedImageUri = data.getData();
                        if (null != selectedImageUri) {
                            // Get the path from the Uri
                            String path = getPathFromURI(selectedImageUri);
                            Log.e(TAG, "Image Path : " + path);
                            // Set the image in ImageView
                            findViewById(R.id.profilePic).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((ImageView) findViewById(R.id.profilePic)).setImageURI(selectedImageUri);
                                }
                            });

                        }
                    }
                }
            }
        }).start();

    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(ProfileEditPersonalActivity.this);
                    }
                });
        alertDialog.show();
    }

    private void HealthDetails() {
        String[] healthList = getResources().getStringArray(R.array.Health);
        ArrayAdapter healthAdapter = new ArrayAdapter(context, R.layout.dropdown_item, healthList);
        //Setting the ArrayAdapter data on the Spinner
        b.spUserHealthDetail.setAdapter(healthAdapter);
    }

    private void limitWord() {
        b.etAddUserDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                b.etAddUserDescription.length();

                String s = b.etAddUserDescription.getText().toString().trim();
                int num = s.length();
                b.tvCountWord.setText("" + (int) num);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void motherTongueSpinner() {
        //motl.clear
        //for loop
        //motl.add(model.religion_name);
        // initialize array list
        motherTongueList = new ArrayList<>();
        motherTongueList.add("Hindi");
        motherTongueList.add("Marathi");
        motherTongueList.add("Punjabi");
        motherTongueList.add("Bengali");
        motherTongueList.add("Gujarati");
        motherTongueList.add("Telugu");
        motherTongueList.add("Urdu");
        motherTongueList.add("Kannada");
        motherTongueList.add("English");
        motherTongueList.add("Tamil");
        motherTongueList.add("Odia");
        motherTongueList.add("Marwari");
        motherTongueList.add("Arunachali");
        motherTongueList.add("Assamese");
        motherTongueList.add("Awadhi");
        motherTongueList.add("Bhojpuri");
        motherTongueList.add("Chattisgarhi");
        motherTongueList.add("Haryanavi");
        motherTongueList.add("Himachali/Pahari");
        motherTongueList.add("Kashmiri");
        motherTongueList.add("Malayalam");
        motherTongueList.add("Khandesi");
        motherTongueList.add("Manipuri");
        motherTongueList.add("Rajasthani");
        motherTongueList.add("Sanskrit");
        motherTongueList.add("Sindhi");
        motherTongueList.add("Other");

        b.tvMotherTongue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // set custom dialog
                dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                dialog.getWindow().setLayout(800, 900);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, motherTongueList);

                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        b.tvMotherTongue.setText(adapter.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void bloodGroup() {
        String[] bloodGroup = getResources().getStringArray(R.array.BloodGroup);
        ArrayAdapter blood = new ArrayAdapter(context, R.layout.dropdown_item, bloodGroup);
        //Setting the ArrayAdapter data on the Spinner
        b.spUserBloodGroup.setAdapter(blood);
    }

    private void userHeight() {
        String[] Height = getResources().getStringArray(R.array.Height);
        ArrayAdapter height = new ArrayAdapter(context, R.layout.dropdown_item, Height);
        //Setting the ArrayAdapter data on the Spinner
        b.spUserHeight.setAdapter(height);
    }

    private void maritalStatus() {
        maritalList.add("Never Married");
        maritalList.add("Divorce");
        maritalList.add("Widowed");
        maritalList.add("Awaiting Divorce");
        maritalList.add("Annulled");

        // Initialize adapter
        ArrayAdapter<String> maritalAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, maritalList);
        b.tvEditMaritalStatus.setAdapter(maritalAdapter);
    }

    private void ageDropDown() {
        // use for loop
        for (int i = 18; i <= 70; i++) {
            // add values in price list
            AgeList.add(" " + i + " Years");
            // check condition
            if (i > 1) {
                // Not include first value  in max list
                minList.add(i + " Years");
            }

        }

        // Initialize adapter
        minAdapter = new ArrayAdapter<>(context, R.layout.dropdown_item, minList);

        // set adapter
        b.multiSelectionAge.setAdapter(minAdapter);

    }

}