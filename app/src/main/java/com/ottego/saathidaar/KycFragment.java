package com.ottego.saathidaar;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;
import com.ottego.saathidaar.Model.DataModelImage;
import com.ottego.saathidaar.databinding.FragmentKycBinding;

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


public class KycFragment extends Fragment implements PickiTCallbacks {
    FragmentKycBinding b;
    SessionManager sessionManager;
    DataModelImage dataModelImage;
    String URL = "http://103.150.186.33:8080/saathidaar_backend/api/member/uploads/photo";
    ProgressDialog progressDialog;
    Context context;
    PickiT pickiT;
    List<String> imagePathList = new ArrayList<>();
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    private static final int PICK_FILE_REQUEST = 1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KycFragment() {
        // Required empty public constructor
    }

    public static KycFragment newInstance(String param1, String param2) {
        KycFragment fragment = new KycFragment();
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
        // Inflate the layout for this fragment
        b = FragmentKycBinding.inflate(getLayoutInflater());
        listener();
        pickiT = new PickiT(context, this, requireActivity());
        context = getContext();
        return b.getRoot();
    }

    private void listener() {
        b.mcvChooseKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_FILE_REQUEST);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_FILE_REQUEST);

            }
        });

        b.mcvUploadKyc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < imagePathList.size(); i++) {
                    uploadInThread(imagePathList.get(i));

                }
            }

        });
    }


    // Function to check and request permission
    public boolean checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{permission}, requestCode);
        } else {
            Snackbar snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), "Permission already granted", Snackbar.LENGTH_LONG);
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
                Toast.makeText(context, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Storage Permission Denied", Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                Log.e("durga", "upload start: " + path);
                String result = multipartRequest(URL, params, path, "image", "image/jpeg");

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

                Toast.makeText(context,
                        "File Upload Complete.", Toast.LENGTH_SHORT).show();

//                runOnUiThread(new Runnable() {
//                    public void run() {
//
//                        //  tv.setText("Upload Complete");
//                        Toast.makeText(context,
//                                        "File Upload Complete.", Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                });
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
        b.ivKyc.setImageURI(Uri.parse(path));
        imagePathList.add(path);
    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {
        Log.e("durga", "paths multiple : " + paths.toString());
        imagePathList.clear();
        imagePathList.addAll(paths);
    }



}

