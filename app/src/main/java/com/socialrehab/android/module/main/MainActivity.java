package com.socialrehab.android.module.main;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.kepler.projectsupportlib.BaseActivity;
import com.kepler.projectsupportlib.Logger;
import com.socialrehab.R;
import com.socialrehab.android.module.accessibility.MyAccessibilityService;
import com.socialrehab.android.module.settings.SettingsActivity;
import com.socialrehab.android.module.settings.password_.PasswordScreen;
import com.socialrehab.android.module.showcase.ShowCase;
import com.socialrehab.android.support.SharedPref;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_FB_KATANA;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_FB_KATANA_INT;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_IG;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_IG_INT;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_TWITTER;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_TWITTER_INT;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_WHATS_APP;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_WHATS_APP_INT;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_YOUTUBE;
import static com.socialrehab.android.module.accessibility.MyAccessibilityService.PACKAGE_YOUTUBE_INT;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 999;
    private static final int MY_PERMISSIONS_REQUEST_ACCESSBILITY_SERVICE = 888;
    private static final int MY_PERMISSIONS_REQUEST_FOR_ACTION_USAGE_ACCESS_SETTINGS = 777;
    @BindView(R.id.accessibility_setting)
    ToggleButton accessibility_setting;
    @BindView(R.id.graph)
    GraphView graph;
    @BindView(R.id.header_view)
    View header_view;
    @BindView(R.id.apps)
    Spinner apps;
    @BindView(R.id.lin_permission_user_stat)
    LinearLayout lin_permission_user_stat;
    @BindView(R.id.permission_user_stat)
    TextView permission_user_stat;
    private WindowManager windowManager;
    private TextView image;
    private Map<String, Integer> refinedList;
    private SharedPref shrdPref;
    private String package_name = PACKAGE_WHATS_APP;
    private int color = Color.rgb(0, 216, 88);

    public static boolean hasAccessbilityPermission(Context mContext) {
        int accessibilityEnabled = 0;
        final String service = mContext.getPackageName() + "/" + MyAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
            Logger.d("accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Logger.d("Err   or finding setting, default accessibility to not found: "
                    + e.getMessage());

        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Logger.d("***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();

                    Logger.d("-------------    > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Logger.d("We've found the correct setting     accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Logger.d("***ACCESSIBILITY IS DISABLED***");
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shrdPref = SharedPref.getInstance(this);
        setTitle(R.string.main);
        updateAccessibilityBtnUi();
        checkUsageStagPermission();
        accessibility_setting.setOnClickListener(this);
        permission_user_stat.setOnClickListener(this);
        apps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case PACKAGE_WHATS_APP_INT:
                        package_name = PACKAGE_WHATS_APP;
                        color = Color.rgb(0, 216, 88);
                        break;
                    case PACKAGE_FB_KATANA_INT:
                        package_name = PACKAGE_FB_KATANA;
                        color = Color.rgb(82, 87, 171);
                        break;
                    case PACKAGE_IG_INT:
                        package_name = PACKAGE_IG;
                        color = Color.rgb(181, 0, 159);
                        break;
                    case PACKAGE_TWITTER_INT:
                        package_name = PACKAGE_TWITTER;
                        color = Color.rgb(44, 145, 245);
                        break;
                    case PACKAGE_YOUTUBE_INT:
                        package_name = PACKAGE_YOUTUBE;
                        color = Color.rgb(255, 0, 0);
                        break;
                }
                header_view.setBackgroundColor(color);
                initGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void checkUsageStagPermission() {
        if (!checkForPermission(this)) {
            lin_permission_user_stat.setVisibility(View.VISIBLE);
        } else {
            lin_permission_user_stat.setVisibility(View.GONE);
            initGraph();
        }
    }


    private void initGraph() {
        try {
//            UStats.printCurrentUsageStatus(this);
            refinedList = UStats.getRefindUsageStatsList(this, package_name);
            graph.removeAllSeries();
            // use static labels for horizontal and vertical labels
            StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
            List<String> level = new ArrayList<>();
            level.add("X");
            Iterator<String> stringIterator = refinedList.keySet().iterator();
            while (stringIterator.hasNext()) {
//                String key = stringIterator.next().substring(0, 2);
                level.add(stringIterator.next());
            }
            level.add("X");

//            staticLabelsFormatter.setHorizontalLabels(refinedList.keySet().toArray(new String[refinedList.size()]));
            staticLabelsFormatter.setHorizontalLabels(level.toArray(new String[level.size()]));
            graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(4);
//            graph.getViewport().setYAxisBoundsManual(true);
//            graph.getViewport().setMinY(0);
//            graph.getViewport().setMaxY(3);
            BarGraphSeries<DataPoint> series = getSeries();
            graph.addSeries(series);
// styling
            series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                @Override
                public int get(DataPoint data) {
                    return color;
                }
            });


// draw values on top
            series.setDrawValuesOnTop(true);
            series.setValuesOnTopSize(30);
            series.setAnimated(true);
            series.setSpacing(20);
            series.setValuesOnTopColor(getResources().getColor(R.color.colorPrimary));

        } catch (Exception e) {
//            showErrorDialog(e.getMessage(), null);
        }
    }

    public BarGraphSeries<DataPoint> getSeries() {
        List<DataPoint> list = new ArrayList<>();
        String key;
        double index = 0.5;
        Iterator<String> iterator = refinedList.keySet().iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            list.add(new DataPoint(Double.parseDouble(String.format("%.1f", index)), refinedList.get(key)));
            index = index + 0.5;
        }
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(list.toArray((new DataPoint[list.size()])));
        return series;
    }

    private boolean checkForPermission(Context context) {
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), context.getPackageName());
        return mode == MODE_ALLOWED;
    }

    private boolean updateAccessibilityBtnUi() {
        if (hasAccessbilityPermission(this)) {
            accessibility_setting.setChecked(true);
            accessibility_setting.setVisibility(View.GONE);
            if (!shrdPref.isShowCaseShowed()) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                Bundle bundle = new Bundle();
                bundle.putIntArray(ShowCase.DATA, new int[]{R.drawable.one, R.drawable.two});
                bundle.putIntArray(ShowCase.DATA, new int[]{R.drawable.one, R.drawable.two});
                startActivity(ShowCase.class, bundle);
            }
            return true;
        } else {
            accessibility_setting.setChecked(false);
        }
        return false;
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
        return R.layout.activity_main;
    }

    public boolean checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this);
    }

    public void requestPermission() {

        // If not, form up an Intent to launch the permission request
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

        // Launch Intent, with the supplied request code
        startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        switch (requestCode) {
            case ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:
                // Double-check that the user granted it, and didn't just dismiss the request
                if (checkDrawOverlayPermission()) {
                    openAccessibilitySetting();
                } else {
                    showToast("This permission is required to restrict your usage if you wish so");
                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESSBILITY_SERVICE:
                updateAccessibilityBtnUi();
                break;
            case MY_PERMISSIONS_REQUEST_FOR_ACTION_USAGE_ACCESS_SETTINGS:
                checkUsageStagPermission();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_user_stat:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawView();
                    }
                }, 1000);
                startActivityForResult(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS), MY_PERMISSIONS_REQUEST_FOR_ACTION_USAGE_ACCESS_SETTINGS);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        removeInstructionView();
                    }
                }, 5000);

                break;
            case R.id.accessibility_setting:
//                checkUsageStagPermission();
                if (updateAccessibilityBtnUi()) {
                    return;
                }
                accessibility_setting.setChecked(false);
                if (checkDrawOverlayPermission()) {
                    openAccessibilitySetting();
                } else {
                    requestPermission();
                }
                break;
        }

    }

    private void openAccessibilitySetting() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                drawView();
            }
        }, 1000);
        startActivityForResult(new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS), MY_PERMISSIONS_REQUEST_ACCESSBILITY_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeInstructionView();
            }
        }, 3000);


    }

    private void drawView() {
        try {
            removeInstructionView();
            windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            image = new TextView(this);
            image.setPadding(10, 10, 10, 10);
            image.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            image.setTextColor(Color.WHITE);
            image.setText("Look for 'Social Rehab' in \nlist and then click");
            WindowManager.LayoutParams params;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.RGB_565);

            } else {
                params = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                        PixelFormat.RGB_565);

            }
            params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
            params.x = 0;
            params.y = 100;
            windowManager.addView(image, params);
        } catch (Exception e) {
//            showErrorDialog(e.getMessage(), null);
            showToastLong("Look for 'Social Rehab' in list and then click");
        }
    }


    public void removeInstructionView() {
        try {
            if (windowManager != null && image != null) {
                windowManager.removeViewImmediate(image);
                image = null;
                windowManager = null;
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // launch settings activity
            if(shrdPref.isPasswordEnabled()){
                startActivity(PasswordScreen.class);
            }else {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
