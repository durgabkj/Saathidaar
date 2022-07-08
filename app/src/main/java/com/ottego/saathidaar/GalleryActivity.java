package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ottego.saathidaar.Adapter.GalleryAdapter;
import com.ottego.saathidaar.Model.SessionModel;
import com.ottego.saathidaar.databinding.ActivityGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity  {
    ActivityGalleryBinding b;
SessionManager sessionManager;
    int PICK_IMAGE_REQUEST = 111;
    String URL = Utils.memberUrl+"upload/photo";
    Bitmap bitmap;
    ProgressDialog progressDialog;
    Context context;
     String imageString;
    String imageEncoded;
    List<String> imagesEncodedList;
    String[] arr=new String[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

          context=GalleryActivity.this;
          sessionManager=new SessionManager(context);
          listener();

        }

    private void listener() {
        //opening image chooser option
        b.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
            }

        });

        b.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0;  i <= imagesEncodedList.size(); i++) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imagesEncodedList.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //converting image to base64 string
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);




                    submit();
                }


//                progressDialog = new ProgressDialog(context);
//                progressDialog.setMessage("Uploading, please wait...");
//                progressDialog.show();
//
//                //converting image to base64 string
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                byte[] imageBytes = baos.toByteArray();
//
//
//                final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//
//                //sending image to server
//                StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
//                    @Override
//                    public void onResponse(String s) {
//                        progressDialog.dismiss();
//                        if(s.equals("true")){
//                            Toast.makeText(context, "Uploaded Successful", Toast.LENGTH_LONG).show();
//                        }
//                        else{
//                            Toast.makeText(context, "Some error occurred!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                },new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        Toast.makeText(context, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
//                    }
//                }) {
//                    //adding parameters to send
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> parameters = new HashMap<String, String>();
//                        parameters.put("image", imageString);
//                        return parameters;
//                    }
//                };
//
//                RequestQueue rQueue = Volley.newRequestQueue(context);
//                rQueue.add(request);
            }
        });
    }

    private void submit() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("member_id", sessionManager.getMemberId());
        params.put("image_name", ".jpg");
        params.put("image_base_urls", String.valueOf(arr));

        Log.e("params", String.valueOf(params));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response", String.valueOf((response)));
                        try {
                            String code = response.getString("results");
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
                        progressDialog.dismiss();
                        if (null != error.networkResponse) {
                            Toast.makeText(context,"Try again......",Toast.LENGTH_LONG).show();
                            Log.e("Error response", String.valueOf(error));
                        }
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(request);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();


                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                        }
                        Log.e("LOG_TAG", "Selected Images" + mArrayUri.size());
                        Log.e("LOG_TAG", "Selected Images" + mArrayUri);
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }


//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            Uri filePath = data.getData();
//
//            try {
//                //getting image from gallery
//                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//
//
//               Log.e("image", String.valueOf(data));
//
//                //Setting image to ImageView
//                b.image.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
    }
}