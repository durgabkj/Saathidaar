package com.ottego.saathidaar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
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
import com.ottego.saathidaar.Model.DataModelKyc;
import com.ottego.saathidaar.databinding.ActivityKycactivityBinding;
import com.ottego.saathidaar.viewmodel.GalleryViewModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class KYCActivity extends AppCompatActivity implements PickiTCallbacks {
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int PICK_FILE_REQUEST = 1;
    ActivityKycactivityBinding b;
    SessionManager sessionManager;
    DataModelKyc model;
    String URL = "http://103.174.102.195:8080/saathidaar_backend/api/member/app/uploads/kyc/photo";
    ProgressDialog progressDialog;
    String getDocumentURL = Utils.memberUrl + "app/get/kyc/photo/";
    Context context;
    String document = "";
    List<String> imagePathList = new ArrayList<>();
    GalleryViewModel viewModel;
    int count = 0;
    PickiT pickiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityKycactivityBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        pickiT = new PickiT(this, this, this);
        viewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        context = KYCActivity.this;
        sessionManager = new SessionManager(context);
        getData();
        listener();
    }

    private void listener() {
b.srlKyc.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        getData();
    }
});

        b.llDocument.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void onClick(View view) {
//        WebView mWebView=new WebView(KYCActivity.this);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl(model.data.get(0).document_path);
//        setContentView(mWebView);

                Intent intent = new Intent(context, WebViewActivity.class);
                if (model.data != null && model.data.size() > 0) {
                    intent.putExtra("data", model.data.get(0).document_path);
                    context.startActivity(intent);
                }
            }
        });

        b.rgDocumentType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.mrbAadhar:
                        document = "Aadhaar Card";
                        break;

                    case R.id.mrbPanCard:
                        document = "Pan-Card";
                        break;
                }
            }
        });


        b.mcvUploadKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForm()) {
                    submitForm();
                }
            }
        });

        b.mtbKYC.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        b.mcvChooseKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_FILE_REQUEST);
                if (Build.VERSION.SDK_INT >= 30) {
                    if (!Environment.isExternalStorageManager()) {
                        Intent getPermission = new Intent();
                        getPermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivity(getPermission);
                    }
                }
                Intent intent = new Intent();
                // set type
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);
            }

        });

//        b.mcvUploadKyc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int i = 0; i < imagePathList.size(); i++) {
//                    uploadInThread(imagePathList.get(i));
//
//                }
//            }
//
//        });

    }

    private void submitForm() {
        for (int i = 0; i < imagePathList.size(); i++) {
            uploadInThread(imagePathList.get(i));

        }
    }

    private boolean checkForm() {
        if (document.isEmpty()) {
            Toast.makeText(context, "First  select your document type", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (imagePathList.isEmpty()) {
            Toast.makeText(context, "Please choose document ", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    // Function to check and request permission
    public boolean checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(KYCActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(KYCActivity.this, new String[]{permission}, requestCode);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Permission already granted", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Checking whether user granted the permission or not.
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(KYCActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(KYCActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

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
                    if (count > 3) {
                        Toast.makeText(context, "You can Only upload three Images", Toast.LENGTH_SHORT).show();
                    } else {
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
        } else {
            Toast.makeText(context, "You haven't pick any image", Toast.LENGTH_SHORT).show();
        }
    }


    void uploadInThread(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//
                Map<String, String> params = new HashMap<String, String>();
                params.put("member_id", sessionManager.getMemberId());
                params.put("document_type", document);
                Log.e("durga", "upload start: " + path);
                String result = multipartRequest(URL, params, path, "document", "application/pdf");

                Log.e("durga", result);
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
        String boundary = "*****" + System.currentTimeMillis() + "*****";
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
            java.net.URL url = new URL(urlTo);
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
                        Toast.makeText(KYCActivity.this,
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
            e.printStackTrace();
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

        //  final ProgressDialog progressDialog = ProgressDialog.show(context, null, "processing...", false, false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                getDocumentURL + sessionManager.getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                b.srlKyc.setRefreshing(false);
                //   progressDialog.dismiss();
                // Log.e("image response", String.valueOf(response));
                Gson gson = new Gson();
                model = gson.fromJson(String.valueOf(response), DataModelKyc.class);
                if (model.results.equalsIgnoreCase("1")) {
                    setData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                b.srlKyc.setRefreshing(false);
                //progressDialog.dismiss();
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(context).myAddToRequest(jsonObjectRequest);
    }

    @SuppressLint("ResourceAsColor")
    private void setData() {
        if (model.data != null && model.data.size() > 0) {
            b.tvImagePath.setText(model.data.get(0).document_name);
            b.tvKycType.setText(model.data.get(0).document_type);
            if (model.data.get(0).kyc_status.equalsIgnoreCase("0")) {
                b.tvKycStatus.setText("Pending");
                b.tvKycStatus.setTextColor(Color.BLUE);
            } else if (model.data.get(0).kyc_status.equalsIgnoreCase("1")) {
                b.tvKycStatus.setText("Accepted");
                b.tvKycStatus.setTextColor(Color.GREEN);
            } else {
                b.tvKycStatus.setText("Rejected");
                b.tvKycStatus.setTextColor(Color.RED);
            }


            if (model.data.get(0).kyc_status.equalsIgnoreCase("0") || model.data.get(0).kyc_status.equalsIgnoreCase("1")) {
                b.mcvUploadKyc.setVisibility(View.GONE);
                b.mcvChooseKyc.setVisibility(View.GONE);
            } else {
                b.mcvUploadKyc.setVisibility(View.VISIBLE);
                b.mcvChooseKyc.setVisibility(View.VISIBLE);
            }


        }

    }

}

