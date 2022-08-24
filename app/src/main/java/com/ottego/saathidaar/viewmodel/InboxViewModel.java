package com.ottego.saathidaar.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ottego.saathidaar.Model.DataModelDashboard;
import com.ottego.saathidaar.Model.InboxModel;
import com.ottego.saathidaar.Model.NewMatchesModel;
import com.ottego.saathidaar.MySingleton;
import com.ottego.saathidaar.SessionManager;

import org.json.JSONObject;

import java.util.List;

public class InboxViewModel extends AndroidViewModel {
    public MutableLiveData<List<InboxModel>> _list = new MutableLiveData<>();
    public LiveData<List<InboxModel>> list1 = _list;

    public MutableLiveData<DataModelDashboard> _count = new MutableLiveData<>();
    public LiveData<DataModelDashboard> count = _count;

    public InboxViewModel(@NonNull Application application) {
        super(application);
    }

    public void getDataCount() {
        String url = "http://103.174.102.195:8080/saathidaar_backend/api/request/count/accept-request/";
//      b.srlMatches.setRefreshing(false);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url + new SessionManager(getApplication()).getMemberId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response count inbox", String.valueOf((response)));
                Gson gson = new Gson();
                DataModelDashboard model = gson.fromJson(String.valueOf(response), DataModelDashboard.class);
                _count.postValue(model);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//            b.srlMatches.setRefreshing(false);
                error.printStackTrace();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.myGetMySingleton(getApplication()).myAddToRequest(jsonObjectRequest);
    }


}
