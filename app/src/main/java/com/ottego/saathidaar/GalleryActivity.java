package com.ottego.saathidaar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.databinding.ActivityGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
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
            public void onClick(View v)   {
                for(int i = 0;  i <= mArrayUri.size(); i++) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imagesEncodedList.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  //  submit();
                }

            }
        });
    }

    private void submit() {
        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
        Map<String, String> params = new HashMap<String, String>();
        params.put("member_id", sessionManager.getMemberId());
        params.put("image_name", ".jpg");
        params.put("image_base_urls", imageString);

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

//        Log.e("data", String.valueOf(data));
//        Uri imageUri = data.getData();
//        Log.e("imageUri", String.valueOf(imageUri));
        //getting image from gallery
//        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


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
                        ArrayList<String> mArrayUri = new ArrayList<>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

//                            Log.e("clip data ", String.valueOf(mClipData.getItemAt(i)));
//                            Log.e("array list ", String.valueOf(mArrayUri.get(i)));

//                            FileInputStream fis = null;
//                            try {
//                                fis = new FileInputStream(String.valueOf(mClipData.getItemAt(i)));
//                            } catch (FileNotFoundException e) {
//                                Log.e("error- ",Log.getStackTraceString(e));
//                                e.printStackTrace();
//                            }
//
//                            Bitmap bm = BitmapFactory.decodeStream(fis);
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            bm.compress(Bitmap.CompressFormat.PNG, 10 , baos);
//                            byte[] img = baos.toByteArray();
//                            String s= Base64.encodeToString(img , Base64.DEFAULT);
//                            Log.e("base 64 - ",s+"");

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            // convert uri to bitmap
                            final InputStream imageStream = getContentResolver().openInputStream(uri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            // convert bitmap to base64
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
                            byte[] b = baos.toByteArray();
                            String encImage ="data:image/jpeg;base64," +Base64.encodeToString(b, Base64.DEFAULT);
                            Log.e("base64",encImage+"");

                            mArrayUri.add(encImage);


                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            Log.e("column index",columnIndex+"");
                            imageEncoded  = cursor.getString(columnIndex);
                            Log.e("column index",imageEncoded+"");
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


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            try {
                //getting image from gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


               Log.e("image", String.valueOf(data));

                //Setting image to ImageView
                b.image.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}