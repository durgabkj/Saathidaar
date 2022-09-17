package com.ottego.saathidaar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.MemberProfileModel;
import com.ottego.saathidaar.databinding.ActivityProfessionalDetailEditBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfessionalDetailEditActivity extends AppCompatActivity {
    ActivityProfessionalDetailEditBinding b;
    Context context;
    MemberProfileModel model;
    String higherEducation = "";
    String ug = "";
    String collegeAttends = "";
    String income = "";
    String workingWith = "";
    String workingAs = "";
    String country = "";
    String state = "";
    String city = "";
    String origin = "";
    String pinCode = "";
    Dialog dialog;
    public String url = Utils.memberUrl + "app/professional-details/update/";


    String memberId;

    ArrayList<String> workingAslist = new ArrayList<>();

    SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityProfessionalDetailEditBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = ProfessionalDetailEditActivity.this;
        sessionManager = new SessionManager(context);
        memberId=sessionManager.getMemberId();

        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        model = new Gson().fromJson(data, MemberProfileModel.class);


        String[] workingAs = getResources().getStringArray(R.array.workingAs);

        for (String string : workingAs) {
            workingAslist.add(string);
        }

        dialog = new Dialog(context);
        userAnnualIncome();

        UserWorkingWith();
        listener();

        setData();

    }

    private void setData() {
        if (model != null) {
            b.etAddUserPGEducation.setText(model.highest_qualification);
            b.etAddUserCollegeAttended.setText(model.college_attended);
            b.etAddUserUGEducation.setText(model.ug_education);
            b.etIncome.setText(model.annual_income);
            //   b.etAddUserCollegeAttended.setText(model.college_attended);
            b.etWorkingWith.setText(model.working_with);
            b.etWorkingAs.setText(model.working_as);


        }
    }


    private void userAnnualIncome() {
        final int[] checkedItem = {-1};
        b.etIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // AlertDialog builder instance to build the alert dialog
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                // set the custom icon to the alert dialog
                alertDialog.setIcon(R.drawable.money);

                // title of the alert dialog
                alertDialog.setTitle("Choose Income");

                // list of the items to be displayed to
                // the user in the form of list
                // so that user can select the item from
                // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                String[] income = getResources().getStringArray(R.array.Income);
                // the function setSingleChoiceItems is the function which builds
                // the alert dialog with the single item selection
                alertDialog.setSingleChoiceItems(income, checkedItem[0], new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // update the selected item which is selected by the user
                        // so that it should be selected when user opens the dialog next time
                        // and pass the instance to setSingleChoiceItems method
                        checkedItem[0] = which;

                        // now also update the TextView which previews the selected item
                        b.etIncome.setText(income[which]);

                        // when selected an item the dialog should be closed with the dismiss method
                        dialog.dismiss();
                    }
                });

                // set the negative button if the user
                // is not interested to select or change
                // already selected item
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // create and build the AlertDialog instance
                // with the AlertDialog builder instance
                AlertDialog customAlertDialog = alertDialog.create();

                // show the alert dialog when the button is clicked
                customAlertDialog.show();
                Button buttonbackground = customAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                buttonbackground.setBackgroundColor(Color.BLACK);

            }
        });

    }

    private void UserWorkingWith() {
            final int[] checkedItem = {-1};
            b.etWorkingWith.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // AlertDialog builder instance to build the alert dialog
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

                    // set the custom icon to the alert dialog
                    alertDialog.setIcon(R.drawable.work);

                    // title of the alert dialog
                    alertDialog.setTitle("Choose Working with");

                    // list of the items to be displayed to
                    // the user in the form of list
                    // so that user can select the item from
                    // final String[] listItems = new String[]{"Android Development", "Web Development", "Machine Learning"};
                    String[] workingWith = getResources().getStringArray(R.array.workingWith);
                    // the function setSingleChoiceItems is the function which builds
                    // the alert dialog with the single item selection
                    alertDialog.setSingleChoiceItems(workingWith, checkedItem[0], new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // update the selected item which is selected by the user
                            // so that it should be selected when user opens the dialog next time
                            // and pass the instance to setSingleChoiceItems method
                            checkedItem[0] = which;

                            // now also update the TextView which previews the selected item
                            b.etWorkingWith.setText(workingWith[which]);

                            // when selected an item the dialog should be closed with the dismiss method
                            dialog.dismiss();
                        }
                    });

                    // set the negative button if the user
                    // is not interested to select or change
                    // already selected item
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    // create and build the AlertDialog instance
                    // with the AlertDialog builder instance
                    AlertDialog customAlertDialog = alertDialog.create();

                    // show the alert dialog when the button is clicked
                    customAlertDialog.show();
                    Button buttonbackground = customAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                    buttonbackground.setBackgroundColor(Color.BLACK);

                }
            });

        }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void listener() {
        b.btnSaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                hideKeyboard(view);
            }
        });

        b.etWorkingAs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set custom dialog
                dialog.setContentView(R.layout.searchable_dropdown_item);

                // set custom height and width
                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, 700);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editText = dialog.findViewById(R.id.edit_text);
                ListView listView = dialog.findViewById(R.id.list_view);

                // Initialize array adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.dropdown_item, workingAslist);
                // set adapter
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        adapter.getFilter().filter(s);
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
                        b.etWorkingAs.setText(adapter.getItem(position));
                        // Dismiss dialog
                        dialog.dismiss();


                    }
                });

            }
        });


    }
    public void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View layout_dialog = LayoutInflater.from(context).inflate(R.layout.alert_sucess_dialog, null);
        builder.setView(layout_dialog);

        TextView btnokSuccess = layout_dialog.findViewById(R.id.btnokSuccess);
        // show dialog


        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);

        dialog.getWindow().setGravity(Gravity.CENTER);

        btnokSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent=new Intent(context,ProfessionalDetaisShowActivity.class);
//                intent.addFlags(Intent.F | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });
    }
    private void submitForm() {
        higherEducation = b.etAddUserPGEducation.getText().toString().trim();
        ug = b.etAddUserUGEducation.getText().toString().trim();
        collegeAttends = b.etAddUserCollegeAttended.getText().toString().trim();
        income = b.etIncome.getText().toString().trim();
        workingWith = b.etWorkingWith.getText().toString().trim();
        workingAs = b.etWorkingAs.getText().toString().trim();


        Map<String, String> params = new HashMap<String, String>();
        params.put("highest_qualification", higherEducation);
        params.put("ug_education", ug);
        params.put("college_attended", collegeAttends);
        params.put("working_with", workingWith);
        params.put("working_as", workingAs);
        params.put("annual_income", income);
        Log.e("params", String.valueOf(params));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url + memberId, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
                            if (code.equals("1")) {
                                successDialog();
                             //   Toast.makeText(context, "", Toast.LENGTH_SHORT).show();  // sessionManager.createSessionLogin(userId);
                                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                        if (null != error.networkResponse) {
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }
}




