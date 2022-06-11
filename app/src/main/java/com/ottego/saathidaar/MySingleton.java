package com.ottego.saathidaar;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingleton;
    private static Context myContext;
    private RequestQueue requestQueue;

    private MySingleton(Context context) {
        myContext = context;
        requestQueue = myGetRequestQueue();
    }

    public static synchronized MySingleton myGetMySingleton(Context context) {
        if (mySingleton == null) {
            mySingleton = new MySingleton(context);
        }
        return mySingleton;
    }

    public RequestQueue myGetRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(myContext.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void myAddToRequest(Request<T> request) {
        requestQueue.getCache().clear();
        requestQueue.add(request);
    }
}
