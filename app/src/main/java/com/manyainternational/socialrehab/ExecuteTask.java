package com.manyainternational.socialrehab;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ExecuteTask {
    //    private static final String url ="http://www.google.com";
    private static final String TOKEN = "https://www.indiashopps.com/api/android/get-token";
    private static final String URL = "https://www.indiashopps.com/api/android/insert-feedback";
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
                    params.put("device_id", Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    params.put("feedback", message);
                    return params;
                }
            };

            requestQu.add(stringRequest);
        } catch (Exception e) {

        }
    }
}