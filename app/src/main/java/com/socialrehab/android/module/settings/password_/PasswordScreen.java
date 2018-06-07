package com.socialrehab.android.module.settings.password_;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kepler.projectsupportlib.BaseActivity;
import com.socialrehab.R;
import com.socialrehab.android.module.ExecuteTask;
import com.socialrehab.android.module.settings.SettingsActivity;
import com.socialrehab.android.support.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.socialrehab.android.module.ExecuteTask.MESSAGE;
import static com.socialrehab.android.module.ExecuteTask.STATUS;
import static com.socialrehab.android.module.ExecuteTask.postStringRequest;

public class PasswordScreen extends BaseActivity {

    @BindView(R.id.pass_code_view)
    PassCodeView pass_code_view;
    @BindView(R.id.forgot_password)
    TextView forgot_password;
    private SharedPref shardPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        shardPref = SharedPref.getInstance(getApplicationContext());
        bindEvents();
    }

    private void bindEvents() {
        pass_code_view.setOnTextChangeListener(new PassCodeView.TextChangeListener() {
            public boolean passwordMatched;

            @Override
            public void onTextChanged(String text) {
                if (text.length() == 4 && !passwordMatched) {
                    if (text.equals(shardPref.getPassword())) {
                        passwordMatched = true;
                        startActivity(new Intent(PasswordScreen.this, SettingsActivity.class));
                        onBackPressed();
                    } else {
                        pass_code_view.setError(true);
                    }
                }
            }

        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shardPref.getEmail()==null){
                    showToast("Recovery email id not found. You should try default password.");
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put(ExecuteTask.DEVICE_ID, ExecuteTask.getDeviceId(getApplicationContext()));
                params.put(ExecuteTask.EMAIL, shardPref.getEmail());
                params.put(ExecuteTask.PASSWORD, shardPref.getPassword());
                final ProgressDialog progressDialog = ProgressDialog.show(PasswordScreen.this, "", getString(R.string.loading));
                postStringRequest(getApplicationContext(), params, ExecuteTask.FORGOT_PASSWORD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response == null || response.isEmpty()) {
                            showToast(R.string.err_network);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.optBoolean(STATUS)) {
                                showToast(jsonObject.optString(MESSAGE));
                                return;
                            }
                            showToast("Password sent to your recovery email id");
                        } catch (Exception e) {

                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        showToast(R.string.err_network);
                    }
                });
            }
        });
    }

    @Override
    protected ProgressBar getHorizontalProgressBar() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_password_screen;
    }
}
