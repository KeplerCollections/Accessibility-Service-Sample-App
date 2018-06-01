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

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

import static com.socialrehab.android.support.Constanta.email;
import static com.socialrehab.android.support.Constanta.password;

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
                try {
                    final ProgressDialog pDialog = ProgressDialog.show(ChangePassword.this, "", "updating");
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(email,r_eid.getText().toString());
                    jsonObject.put(password,pd.getText().toString());
                    ExecuteTask.postJsonObjectRequest(getApplicationContext(), "", jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            shardPref.savePE(pd.getText().toString(), r_eid.getText().toString());
                            showToast(R.string.password_changed);
                            onBackPressed();
                            pDialog.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pDialog.dismiss();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
