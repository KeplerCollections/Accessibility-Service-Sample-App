package com.socialrehab.android.module.settings.password_;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kepler.projectsupportlib.BaseActivity;
import com.socialrehab.R;
import com.socialrehab.android.module.ExecuteTask;
import com.socialrehab.android.support.SharedPref;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.socialrehab.android.module.ExecuteTask.MESSAGE;
import static com.socialrehab.android.module.ExecuteTask.STATUS;
import static com.socialrehab.android.module.ExecuteTask.postStringRequest;

public class ChangePassword extends BaseActivity {

    @BindView(R.id.pd)
    EditText pd;
    @BindView(R.id.c_pd)
    EditText c_pd;
    @BindView(R.id.r_eid)
    EditText r_eid;
    @BindView(R.id.change_password)
    Button change_password;
    private SharedPref shardPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableBackButton();
        shardPref = SharedPref.getInstance(getApplicationContext());
        r_eid.setText(shardPref.getEmail());
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pd.getText().toString().length() != 4) {
                    showToast(R.string.err_valid_password);
                    return;
                }
                if (!pd.getText().toString().equals(c_pd.getText().toString())) {
                    showToast(R.string.err_password_not_matched);
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(r_eid.getText().toString()).matches()) {
                    showToast(R.string.err_valid_email_id);
                    return;
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put(ExecuteTask.DEVICE_ID, ExecuteTask.getDeviceId(getApplicationContext()));
                params.put(ExecuteTask.EMAIL, r_eid.getText().toString());
                params.put(ExecuteTask.PASSWORD, pd.getText().toString());
                final ProgressDialog progressDialog = ProgressDialog.show(ChangePassword.this, "", getString(R.string.loading));
                postStringRequest(getApplicationContext(), params, ExecuteTask.SET_PASSWORD, new Response.Listener<String>() {
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
                            shardPref.savePE(pd.getText().toString(), r_eid.getText().toString());
                            showToast("Password saved successfully");
                            onBackPressed();
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
//
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
        return R.layout.activity_change_password;
    }
}
