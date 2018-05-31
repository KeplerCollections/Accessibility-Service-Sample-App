package com.socialrehab.android.settings;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import com.socialrehab.android.ExecuteTask;
import com.socialrehab.R;
import com.socialrehab.android.SharedPref;

public class SettingsActivity extends AppCompatPreferenceActivity {

//    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringValue = newValue.toString();
//
//            if (preference instanceof EditTextPreference) {
//                if (preference.getKey().equals("pref_key_send_feedback")) {
//                    ExecuteTask.executeStringRequest(SettingsActivity.this);
//                }
//            } else if (preference instanceof TimePreference) {
//                switch (preference.getKey()) {
//                    case "pref_whatsapp_start_time":
//                        preference.setSummary(stringValue);
//                        break;
//                    case "pref_whatsapp_end_time":
//                        preference.setSummary(stringValue);
//                        break;
//                    case "pref_facebook_start_time":
//                        preference.setSummary(stringValue);
//                        break;
//                    case "pref_facebook_end_time":
//                        preference.setSummary(stringValue);
//                        break;
//                    case "pref_instagram_start_time":
//                        preference.setSummary(stringValue);
//                        break;
//                    case "pref_instagram_end_time":
//                        preference.setSummary(stringValue);
//                        break;
//                }
//            }
//            return true;
//        }
//    };

//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
//
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MainPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
        private SharedPref shardPref;
        private Preference pref_whatsapp_start_time;
        private Preference pref_whatsapp_end_time;
        private Preference pref_facebook_start_time;
        private Preference pref_facebook_end_time;
        private Preference pref_instagram_start_time;
        private Preference pref_instagram_end_time;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            shardPref = SharedPref.getInstance(getActivity());
            addPreferencesFromResource(R.xml.pref_setting);
            pref_whatsapp_start_time = findPreference(getString(R.string.pref_whatsapp_start_time));
            pref_whatsapp_end_time = findPreference(getString(R.string.pref_whatsapp_end_time));
            pref_facebook_start_time = findPreference(getString(R.string.pref_facebook_start_time));
            pref_facebook_end_time = findPreference(getString(R.string.pref_facebook_end_time));
            pref_instagram_start_time = findPreference(getString(R.string.pref_instagram_start_time));
            pref_instagram_end_time = findPreference(getString(R.string.pref_instagram_end_time));

            pref_whatsapp_start_time.setSummary(getFormatedTime(shardPref.whatsAppStartTime()));
            pref_whatsapp_end_time.setSummary(getFormatedTime(shardPref.whatsAppEndTime()));
            pref_facebook_start_time.setSummary(getFormatedTime(shardPref.facebookStartTime()));
            pref_facebook_end_time.setSummary(getFormatedTime(shardPref.facebookEndTime()));
            pref_instagram_start_time.setSummary(getFormatedTime(shardPref.instagramStartTime()));
            pref_instagram_end_time.setSummary(getFormatedTime(shardPref.instagramEndTime()));
//           change listener
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key_send_feedback)));
//            bindPreferenceSummaryToValue(pref_whatsapp_start_time);
//            bindPreferenceSummaryToValue(pref_whatsapp_end_time);
//            bindPreferenceSummaryToValue(pref_facebook_start_time);
//            bindPreferenceSummaryToValue(pref_facebook_end_time);
//            bindPreferenceSummaryToValue(pref_instagram_start_time);
//            bindPreferenceSummaryToValue(pref_instagram_end_time);
            findPreference(getString(R.string.pref_key_send_feedback)).setOnPreferenceChangeListener(this);
            pref_whatsapp_start_time.setOnPreferenceChangeListener(this);
            pref_whatsapp_end_time.setOnPreferenceChangeListener(this);
            pref_facebook_start_time.setOnPreferenceChangeListener(this);
            pref_facebook_end_time.setOnPreferenceChangeListener(this);
            pref_instagram_start_time.setOnPreferenceChangeListener(this);
            pref_instagram_end_time.setOnPreferenceChangeListener(this);

            Preference pref_whatsapp_key_back = findPreference(getString(R.string.pref_whatsapp_key_back));
            Preference pref_facebook_key_back = findPreference(getString(R.string.pref_facebook_key_back));
            Preference pref_instagram_key_back = findPreference(getString(R.string.pref_instagram_key_back));
            pref_whatsapp_key_back.setOnPreferenceClickListener(this);
            pref_facebook_key_back.setOnPreferenceClickListener(this);
            pref_instagram_key_back.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "pref_whatsapp_key_back":
                    ((PreferenceScreen) findPreference(getString(R.string.whatsapp))).getDialog().dismiss();
                    return true;
                case "pref_facebook_key_back":
                    ((PreferenceScreen) findPreference(getString(R.string.facebook))).getDialog().dismiss();
                    return true;
                case "pref_instagram_key_back":
                    ((PreferenceScreen) findPreference(getString(R.string.instagram))).getDialog().dismiss();
                    return true;
            }
            return false;
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("pref_key_send_feedback")) {
                    ExecuteTask.saveFeedBack(getActivity(), stringValue);
                }
            } else if (preference instanceof TimePreference) {
                switch (preference.getKey()) {
                    case "pref_whatsapp_start_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                    case "pref_whatsapp_end_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                    case "pref_facebook_start_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                    case "pref_facebook_end_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                    case "pref_instagram_start_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                    case "pref_instagram_end_time":
                        preference.setSummary(getFormatedTime(stringValue));
                        break;
                }
            }
            return true;
        }

        private String getFormatedTime(String stringValue) {
            try {
                String[] val = stringValue.split(":");
                if (val[1].length() == 1)
                    stringValue = val[0] + ":0" + val[1];
                if (val[0].length() == 1)
                    stringValue = "0" + stringValue;
            } catch (Exception e) {

            }
            return stringValue;
        }
    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
//    private void sendFeedback() {

//        String body = null;
//        try {
//            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
//            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
//                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
//                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
//        } catch (PackageManager.NameNotFoundException e) {
//        }
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("message/rfc822");
//        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@androidhive.info"});
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
//        intent.putExtra(Intent.EXTRA_TEXT, body);
//        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
//    }

}
