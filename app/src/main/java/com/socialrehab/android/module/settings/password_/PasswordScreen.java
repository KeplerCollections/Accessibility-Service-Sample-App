package com.socialrehab.android.module.settings.password_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kepler.projectsupportlib.BaseActivity;
import com.socialrehab.R;
import com.socialrehab.android.module.ExecuteTask;
import com.socialrehab.android.module.settings.SettingsActivity;
import com.socialrehab.android.support.SharedPref;

import butterknife.BindView;

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
                ExecuteTask.postJsonObjectRequest(getApplicationContext(),"",null,null, null);
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
