package com.ottego.saathidaar;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.ottego.saathidaar.Adapter.ImageAdapter;
import com.ottego.saathidaar.Model.DataModelImage;
import com.ottego.saathidaar.databinding.ActivityGalleryBinding;
import com.ottego.saathidaar.viewmodel.GalleryViewModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity implements PickiTCallbacks {
    ActivityGalleryBinding b;
    SessionManager sessionManager;
    DataModelImage dataModelImage;
    String URL = "http://103.150.186.33:8080/saathidaar_backend/api/member/uploads/photo";
    ProgressDialog progressDialog;
    String getImageURL = Utils.memberUrl + "app/get/photo/";
    Context context;
    List<String> imagePathList = new ArrayList<>();
    GalleryViewModel viewModel;
    int count=0;
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int PICK_FILE_REQUEST = 1;

    PickiT pickiT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        pickiT = new PickiT(this, this, this);
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        context = GalleryActivity.this;
       sessionManager = new SessionManager(context);


        // Now we will call setSelected() method
        // and pass boolean value as true
        b.marqueeText.setSelected(true);
        getData();
        listener();
    }

    private void listener() {
        b.srlRecycleViewGallery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });


        b.mtGalleryToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        b.mtGalleryToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_FILE_REQUEST);
                if (item.getItemId()==R.id.menu_top_add){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);
                }
                return false;
            }

        });



        b.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < imagePathList.size(); i++) {
                    uploadInThread(imagePathList.get(i));

                }
            }

        });
        b.mcvImageGuidLinesGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                View layout_dialog1= LayoutInflater.from(context).inflate(R.layout.layout_image_guid_lines,null);
                builder.setView(layout_dialog1);

                AppCompatButton ok =layout_dialog1.findViewById(R.id.buttonOk);
                // show dialog

                AlertDialog dialog=builder.create();
                dialog.show();
                dialog.setCancelable(false);

                dialog.getWindow().setGravity(Gravity.CENTER);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    // Function to check and request permission
    public boolean checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(GalleryActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(GalleryActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Permission already granted", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking whether user granted the permission or not.
         if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(GalleryActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(GalleryActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                imagePathList.clear();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                   if(count >3) {
                       Toast.makeText(context, "You can Only upload three Images", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       for (int i = 0; i < count; i++) {
                           Uri imageUri = data.getClipData().getItemAt(i).getUri();
                           pickiT.getPath(imageUri, Build.VERSION.SDK_INT);

                       }
                   }
                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();
                    pickiT.getPath(imagePath, Build.VERSION.SDK_INT);
                }
            }
        }else
        {
            Toast.makeText(context, "You haven't pick any image", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveBitmapToFile(String path){
        try {
            File file = new File(path);
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            String extr = Environment.getExternalStorageDirectory().toString();
            File mFolder = new File(extr + "/Download/Sathidaar/");
            if (!mFolder.exists()) {
                mFolder.mkdir();
            }

            String name = path.substring(0,path.lastIndexOf("."));
            String s = "tmp.png";
            File f = new File(mFolder.getAbsolutePath(), s);
            String strMyImagePath = f.getAbsolutePath();

            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return strMyImagePath;
        } catch (Exception e) {
            return null;
        }
    }

    void uploadInThread(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", sessionManager.getMemberId());
                Log.e("durga", "upload start: "+path);
                String result = multipartRequest(URL, params, path, "image", "image/jpeg");

                 Log.e("durga",result);
            }
        }).start();
    }

    public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) {
        Log.e("params", String.valueOf(parmas));
        Log.e("params1", filepath);
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);
            Log.e("file", String.valueOf(file));
            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                outputStream.flush();
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            Log.e("hey-keys", String.valueOf(keys));
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);
                Log.e("durga", "response: " + key + " value");
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            Log.e("Connection response", String.valueOf(connection.getResponseCode()));
            Log.e("durga", "response: " + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {

                runOnUiThread(new Runnable() {
                    public void run() {

                      //  tv.setText("Upload Complete");
                        Toast.makeText(GalleryActivity.this,
                                "File Upload Complete.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();

            return result;
        } catch (Exception e) {
//            logger.error(e);
//            throw new CustomException(e)
        }
        return "error";
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        Log.e("durga", "path single: " + path);
        imagePathList.add(path);

    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {
        Log.e("durga", "paths multiple : " + paths.toString());
        imagePathList.clear();
        imagePathList.addAll(paths);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onBackPressed() {
        pickiT.deleteTemporaryFile(this);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            pickiT.deleteTemporaryFile(this);
        }
    }


    private void getData() {
        count++;
        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getImageURL + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //   progressDialog.dismiss();
                b.srlRecycleViewGallery.setRefreshing(false);
                // Log.e("image response", String.valueOf(response));
                Gson gson = new Gson();
                dataModelImage = gson.fromJson(String.valueOf(response), DataModelImage.class);
                if (dataModelImage.results == 1) {
                    viewModel._list.postValue(dataModelImage.data);
                    setRecyclerView();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlRecycleViewGallery.setRefreshing(false);
                // progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
refresh(1000);
    }
    private void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        b.rvMyImage.setLayoutManager(layoutManager);
        b.rvMyImage.setHasFixedSize(true);
        b.rvMyImage.setNestedScrollingEnabled(true);
        ImageAdapter adapter = new ImageAdapter(context, dataModelImage.data);
        b.rvMyImage.setAdapter(adapter);
        if (adapter.getItemCount() != 0) {
            b.llNoDataImage.setVisibility(View.GONE);
            b.llCard.setVisibility(View.VISIBLE);

        } else {
            b.llNoDataImage.setVisibility(View.VISIBLE);
        }
    }


    private void refresh(int millisecond) {

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getData();
            }
        };

        handler.postDelayed(runnable, millisecond);

    }



//
//    android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//    layoutParams.width = 80;
//    layoutParams.height = 80;
//imageView.setLayoutParams(layoutParams);

}