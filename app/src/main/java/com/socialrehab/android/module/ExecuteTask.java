package com.socialrehab.android.module;

import android.content.Context;
import android.provider.Settings;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.projectsupportlib.Logger;
import com.socialrehab.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExecuteTask {
    //    private static final String url ="http://www.google.com";
    private static final String TOKEN = "https://www.indiashopps.com/api/android/get-token";
    private static final String URL = "https://www.indiashopps.com/api/android/insert-feedback";
    private static final String BASE = "http://app.mybestprice.my/social/";
    public static final String FORGOT_PASSWORD = BASE+"social_reset_password.php";
    public static final String SET_PASSWORD = BASE+"social_forgot_password.php";
    public static final String DEVICE_ID="device_id";
    public static final String STATUS="status";
    public static final String MESSAGE="message";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    private static String cookies;

    public static void saveFeedBack(final Context context, final String message) {
        try {
            final RequestQueue requestQu = Volley.newRequestQueue(context);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, TOKEN, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Logger.print(response);
                            saveFeedBack(requestQu, context, message, (response != null) ? response.optString("_token", null) : null);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            Logger.print(error.getMessage());
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "HbB0jot2dS0ZYvVfVAjn");
                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                     since we don't know which of the two underlying network vehicles
                    // will Volley use, we have to handle and store session cookies manually
                    Logger.i("response", response.headers.toString());
                    Map<String, String> responseHeaders = response.headers;
                    String rawCookies = responseHeaders.get("Set-Cookie");
                    Logger.i("cookies", rawCookies);
                    cookies = rawCookies;
                    return super.parseNetworkResponse(response);
                }
            };

// Access the RequestQueue through your singleton class.
            requestQu.add(jsonObjectRequest);
        } catch (Exception e) {

        }
    }

    private static void saveFeedBack(RequestQueue requestQu, final Context context, final String message, final String token) {
        if (token == null)
            return;
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Logger.print(response);
                            Logger.showToast(context, R.string.feedback_submitted_message);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "tAMLL6Xm7xWYFYfK57Mo");
                    params.put("Cookie", cookies);
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("_token", token);
                    params.put("device_id", getDeviceId(context));
                    params.put("feedback", message);
                    return params;
                }
            };

            requestQu.add(stringRequest);
        } catch (Exception e) {

        }
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void getJsonObjectRequest(Context context, String url, Response.Listener<JSONObject> jsonObjectListener) {
        try {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, jsonObjectListener,null);


// Access the RequestQueue through your singleton class.
            Volley.newRequestQueue(context).add(jsonObjectRequest);
        } catch (Exception e) {

        }
    }

    public static void postJsonObjectRequest(Context context, String url, JSONObject jsonRequest, Response.Listener<JSONObject> jsonObjectListener,Response.ErrorListener errorListener) {
        try {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, url, jsonRequest, jsonObjectListener,errorListener);


// Access the RequestQueue through your singleton class.
            Volley.newRequestQueue(context).add(jsonObjectRequest);
        } catch (Exception e) {

        }
    }public static void postStringRequest(Context context, final Map<String, String> params, String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        try {
            final StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    url, responseListener,errorListener){
                @Override
                protected Map<String,String> getParams(){
                    return params;
                }

            };

// Access the RequestQueue through your singleton class.
            Volley.newRequestQueue(context).add(stringRequest);
        } catch (Exception e) {

        }
    }
}