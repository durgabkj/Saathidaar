package com.ottego.saathidaar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ottego.saathidaar.databinding.ActivityGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends AppCompatActivity {
    ActivityGalleryBinding b;
    SessionManager sessionManager;

    String URL = Utils.memberUrl + "uploads/photo";
    ProgressDialog progressDialog;
    Context context;
    List<String> imageNameList = new ArrayList<>();
    List<String> imagePathList = new ArrayList<>();

    private static final int PICK_FILE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityGalleryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        context = GalleryActivity.this;
        sessionManager = new SessionManager(context);
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
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);
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
    }

//    private void submit(String name, String path) {
//        final ProgressDialog progressDialog = ProgressDialog.show(context, null, "checking credential please wait....", false, false);
//
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("member_id", sessionManager.getMemberId());
//      //  params.put("image_name", name);
////       params.put("image",path.toString());
//        Log.e("params", String.valueOf(params));
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        progressDialog.dismiss();
//                        Log.e("response", String.valueOf((response)));
//                        try {
//                            for(int i = 0; i < imagePathList.size(); i++) {
//
//                            }
//
//                            String code = response.getString("results");
//                            if (code.equalsIgnoreCase("1")) {
//
//                              //  Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
//                            } else {
//                            //    Toast.makeText(context, response.getString("message"), Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(context, "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        if (null != error.networkResponse) {
//                            Toast.makeText(context,"Try again......",Toast.LENGTH_LONG).show();
//                            Log.e("Error response", String.valueOf(error));
//                        }
//                    }
//                });
//
//        request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.myGetMySingleton(context).myAddToRequest(request);
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                imageNameList.clear();
                imagePathList.clear();
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        Log.e("imageUri1", String.valueOf(imageUri));
                        pathFile(imageUri);
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
                } else if (data.getData() != null) {
                    Uri imagePath = data.getData();
                    Log.e("imageUri1", "data path:  " + String.valueOf(imagePath));
                    pathFile(imagePath);
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }

            }
        }
    }


    public void pathFile(Uri selectedFileUri) {
        String filePath = FilePath.getPath(this, selectedFileUri);
        imagePathList.add(filePath);
        Log.e("imageUri1", "parse Path:" + filePath);

        if (filePath != null && !filePath.equals("")) {
            try {
                String fileName = getFileName(context, selectedFileUri);
                imageNameList.add(fileName);
                b.textViewFileName.setText(fileName);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Cannot upload attachment", Toast.LENGTH_SHORT).show();
        }
    }


    public static String getFileName(Context context, Uri uri) throws URISyntaxException {
        String temp = "";
        String[] projection = {OpenableColumns.DISPLAY_NAME};
        Cursor cursor = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                if (cursor.moveToFirst()) {
                    temp = cursor.getString(column_index);
                    return temp;
                }
            } catch (Exception e) {

            }
        } else if (uri.getScheme().equalsIgnoreCase("file")) {
            temp = uri.getLastPathSegment();
        }
        return temp;
    }

    void uploadInThread(final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                uploadFile(path);
                //setup params
                Map<String, String> params = new HashMap<String, String>(1);
                params.put("member_id", sessionManager.getMemberId());

                String result = multipartRequest(URL, params, path, "image", "image/jpeg");
//next parse result string
            }
        }).start();
    }

//    public int uploadFile(final String selectedFilePath) {
//        int serverResponseCode = 0;
//        HttpURLConnection connection;
//        DataOutputStream dataOutputStream;
//        String lineEnd = "\r\n";
//        String twoHyphens = "--";
//        String boundary = "*****";
//
//        int bytesRead, bytesAvailable, bufferSize;
//        byte[] buffer;
//        int maxBufferSize = 1 * 1024 * 1024;
//        File selectedFile = new File(selectedFilePath);
//
//        String[] parts = selectedFilePath.split("/");
//        final String fileName = parts[parts.length - 1];
//
//        if (!selectedFile.isFile()) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    b.textViewFileName.setText("Source file doesn't exist: " + selectedFilePath);
//                }
//            });
//            return 0;
//        } else {
//            try {
//                FileInputStream fileInputStream = new FileInputStream(selectedFile);
//                java.net.URL url = new URL(URL);
//                connection = (HttpURLConnection) url.openConnection();
//                connection.setDoInput(true);//Allow Inputs
//                connection.setDoOutput(true);//Allow Outputs
//                connection.setUseCaches(false);//Don't use a cached Copy
//                connection.setRequestMethod("POST");
//                connection.setRequestProperty("Connection", "Keep-Alive");
//                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
//                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
//                connection.setRequestProperty("member_id", sessionManager.getMemberId());
//                connection.setRequestProperty("image", selectedFilePath);
//
//                //creating new dataoutputstream
//                dataOutputStream = new DataOutputStream(connection.getOutputStream());
//
//                //writing bytes to data outputstream
//                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
//                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\"" + selectedFilePath + "\"" + lineEnd);
//
//                dataOutputStream.writeBytes(lineEnd);
//
//                //returns no. of bytes present in fileInputStream
//                bytesAvailable = fileInputStream.available();
//                //selecting the buffer size as minimum of available bytes or 1 MB
//                bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                //setting the buffer as byte array of size of bufferSize
//                buffer = new byte[bufferSize];
//
//
//                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
//                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//
//                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
//                while (bytesRead > 0) {
//                    //write the bytes read from inputstream
//                    dataOutputStream.write(buffer, 0, bufferSize);
//                    bytesAvailable = fileInputStream.available();
//                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
//                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
//                }
//
//                dataOutputStream.writeBytes(lineEnd);
//                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
//                serverResponseCode = connection.getResponseCode();
//                String serverResponseMessage = connection.getResponseMessage();
//                Log.i("Durga", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);
//                if (serverResponseCode == 200) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            finish();
//                            Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show();
//
//                            //  textViewFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + MyUrl.URL + "uploads/"+ fileName);
//                        }
//                    });
//                }
//
//                //closing the input and output streams
//                fileInputStream.close();
//                dataOutputStream.flush();
//                dataOutputStream.close();
//
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Please give permission for storage", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                Toast.makeText(context, "URL error!", Toast.LENGTH_SHORT).show();
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(context, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
//            }
//            return serverResponseCode;
//        }
//    }


    public String multipartRequest(String urlTo, Map<String, String> parmas, String filepath, String filefield, String fileMimeType) {
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String[] q = filepath.split("/");
        int idx = q.length - 1;

        try {
            File file = new File(filepath);
            FileInputStream fileInputStream = new FileInputStream(file);

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
            }

            outputStream.writeBytes(lineEnd);

            // Upload POST Data
            Iterator<String> keys = parmas.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                String value = parmas.get(key);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
                outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(value);
                outputStream.writeBytes(lineEnd);
            }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            Log.e("durga", String.valueOf(connection.getResponseCode()));

            if (200 != connection.getResponseCode()) {
//                throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
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
        return "error;";
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

}