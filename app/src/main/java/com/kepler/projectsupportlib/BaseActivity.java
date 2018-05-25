package com.kepler.projectsupportlib;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.manyainternational.socialrehab.R;

import java.util.ArrayDeque;
import java.util.Queue;

import butterknife.ButterKnife;

import static com.kepler.projectsupportlib.Logger.DIALOG_CONFIRM;
import static com.kepler.projectsupportlib.Logger.DIALOG_ERROR;

public abstract class BaseActivity extends AppCompatActivity implements FragmentCommunicator {

    //in your Activity
    private final Queue<DeferredFragmentTransaction> deferredFragmentTransactions = new ArrayDeque<>();
//    private DividerItemDecoration divider;
    private ProgressDialog dialog;
    private SharedPreferences srdPrf;
    private boolean isRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
    }

    protected void openInBrowser(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {

        }
    }


    protected void hideToolbar() {
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().hide();
    }

    protected SharedPreferences getDefaultSharedPreferences() {
        if (srdPrf == null)
//            srdPrf = PreferenceManager.getDefaultSharedPreferences(this);
            srdPrf = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return srdPrf;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    public void dismissHorizontalProgress() {
        if (getHorizontalProgressBar() != null && getHorizontalProgressBar().getVisibility() == View.VISIBLE)
            getHorizontalProgressBar().setVisibility(View.GONE);
    }

    protected void enableBackButton() {
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.home, menu);
//        hideMenu(menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    protected void hideMenu(Menu menu) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
//            case R.id.search:
//                startActivity(SearchActivity.class);
//                break;

        }
        return super.onOptionsItemSelected(item);
    }

    protected void setPageTitle(int title) {
        if (getSupportActionBar() == null)
            return;
        getSupportActionBar().setTitle(title);
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, int flags) {
        Intent intent = new Intent(this, aClass);
        intent.setFlags(flags);
        startActivity(intent);
    }


    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, Bundle bundle) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void startActivityForResult(@NonNull Class<? extends BaseActivity> aClass, Bundle bundle, int request_code) {
        Intent intent = new Intent(this, aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, request_code);
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, @NonNull String action, @NonNull String query, String group) {
        Intent intent = new Intent(this, aClass);
        intent.setAction(action);
        intent.putExtra(SearchManager.QUERY, query);
//        intent.putExtra(GROUP, group);
        startActivity(intent);
    }

    protected void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    private void showProgressDialog(int msg) {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
            dialog.setCancelable(false);
            dialog.setMessage(getResources().getString(msg));
            dialog.show();
        } else if (!dialog.isShowing()) {
            dialog.setMessage(getResources().getString(msg));
            dialog.show();
        }

    }

    private void dismiss() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    private void replaceFragment(@NonNull Fragment fragment, int contentFrameId, final boolean addToStack) {
        if (!isRunning) {
            DeferredFragmentTransaction deferredFragmentTransaction = new DeferredFragmentTransaction() {
                @Override
                public void commit() {
                    replaceFragmentInternal(getContentFrameId(), getReplacingFragment(), addToStack);
                }
            };

            deferredFragmentTransaction.setContentFrameId(contentFrameId);
            deferredFragmentTransaction.setReplacingFragment(fragment);

            deferredFragmentTransactions.add(deferredFragmentTransaction);
        } else {
            replaceFragmentInternal(contentFrameId, fragment, addToStack);
        }
    }

    private void addFragment(@NonNull Fragment fragment, int contentFrameId, final boolean addToStack) {

        if (!isRunning) {
            DeferredFragmentTransaction deferredFragmentTransaction = new DeferredFragmentTransaction() {
                @Override
                public void commit() {
                    addFragmentInternal(getContentFrameId(), getReplacingFragment(), addToStack);
                }
            };
//            R.id.fragment_container
            deferredFragmentTransaction.setContentFrameId(contentFrameId);
            deferredFragmentTransaction.setReplacingFragment(fragment);

            deferredFragmentTransactions.add(deferredFragmentTransaction);
        } else {
            addFragmentInternal(contentFrameId, fragment, addToStack);
        }
    }

    private void addFragmentInternal(int contentFrameId, Fragment fragment, final boolean addToStack) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(contentFrameId, fragment);
        if (addToStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }


    private void replaceFragmentInternal(int contentFrameId, Fragment replacingFragment, boolean addToStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(contentFrameId, replacingFragment);
        if (addToStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        dialog = null;
        Logger.print("onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        while (!deferredFragmentTransactions.isEmpty()) {
            deferredFragmentTransactions.remove().commit();
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void setFragmentTitle(int title) {
        if (title != 0)
            setTitle(title);
    }

    @Override
    public void setFragmentTitle(String title) {
        if (title != null)
            setTitle(title);
    }

    @Override
    public void showProgressBar(int message) {
        showProgressDialog(message);
    }

    @Override
    public void dismissProgressBar() {
        dismiss();
    }

    @Override
    public void showDialog(String title, String message, DialogInterface.OnClickListener onClickListener, int dialogType) {
        switch (dialogType) {
            case DIALOG_ERROR:
                showErrorDialog(message, onClickListener);
                break;
            case DIALOG_CONFIRM:
                showConfirmDialog((title == null) ? getString(R.string.confirm) : title, message, R.string.okay, onClickListener, R.string.cancel, null);
                break;
            default:
                showAlertDialog(message, onClickListener);
        }
    }

    @Override
    public void showDialog(String title, String message, int positiveBtn, DialogInterface.OnClickListener positiveBtnListner, int negativeBtn, DialogInterface.OnClickListener negativeBtnListener, int dialogType) {
        switch (dialogType) {
            case DIALOG_ERROR:
                showErrorDialog(message, positiveBtnListner);
                break;
            case DIALOG_CONFIRM:
                showConfirmDialog((title == null) ? getString(R.string.confirm) : title, message, positiveBtn, positiveBtnListner, negativeBtn, negativeBtnListener);
                break;
            default:
                showAlertDialog(message, positiveBtnListner);
        }
    }

    protected void showAlertDialog(String message, DialogInterface.OnClickListener listener) {
        showSimpleAlert(getString(R.string.alert), message, R.string.okay, listener, 0, null, false);
    }

    protected void showConfirmDialog(String title, String message, int positiveBtnTextId, DialogInterface.OnClickListener positiveBtnListener, int negativeBtnTextId, DialogInterface.OnClickListener negativeBtnListener) {
        showSimpleAlert(title, message, positiveBtnTextId, positiveBtnListener, negativeBtnTextId, negativeBtnListener, false);
    }

    protected void showErrorDialog(String message, DialogInterface.OnClickListener listener) {
        showSimpleAlert(getString(R.string.error), message, R.string.okay, listener, 0, null, false);
    }

    private void showSimpleAlert(String title, String message, int positiveBtnTextId, DialogInterface.OnClickListener positiveBtnListener, int negativeBtnTextId, DialogInterface.OnClickListener negativeBtnListener, boolean isCancelable) {
        if (negativeBtnTextId == 0) {
            new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(positiveBtnTextId, positiveBtnListener).setCancelable(isCancelable).create().show();
        } else {
            new AlertDialog.Builder(this).setTitle(title).setMessage(message).setPositiveButton(positiveBtnTextId, positiveBtnListener).setNegativeButton(negativeBtnTextId, negativeBtnListener).setCancelable(isCancelable).create().show();
        }
    }


    //    @Override
//    public void replaceFragment(Fragment fragment, boolean addTo) {
//        replaceFragment(fragment, getFragmentContainerId(), addTo);
//
//    }
//
//    @Override
//    public void addFragment(Fragment fragment, boolean addTo) {
//        addFragment(fragment, getFragmentContainerId(), addTo);
//    }

    @Override
    public void replaceFragment(Fragment fragment, Bundle bundle, boolean addTo) {
        if (bundle != null)
            fragment.setArguments(bundle);
        replaceFragment(fragment, getFragmentContainerId(), addTo);
    }

    @Override
    public void addFragment(Fragment fragment, Bundle bundle, boolean addTo) {
        if (bundle != null)
            fragment.setArguments(bundle);
        addFragment(fragment, getFragmentContainerId(), addTo);
    }

    protected boolean isInternetAvailable() {
        return CheckInterNetNetwork.isInternetAvailable(getApplicationContext());
    }

//    protected DividerItemDecoration getHorizonatalDivider() {
//        divider = new DividerItemDecoration(this, GridLayoutManager.HORIZONTAL);
//        divider.setDrawable(getResources().getDrawable(R.drawable.divider_drawable));
//        return divider;
//    }
//
//    protected DividerItemDecoration getVerticalDivider() {
//        divider = new DividerItemDecoration(this, GridLayoutManager.VERTICAL);
//        divider.setDrawable(getResources().getDrawable(R.drawable.divider_drawable));
//        return divider;
//    }

    protected abstract ProgressBar getHorizontalProgressBar();

    protected abstract int getFragmentContainerId();

    protected abstract int getContentView();

    protected void filterActivity(Bundle intentBundle) {
        if (intentBundle == null) {
            return;
        }
//        String action = intentBundle.getString(Intent.ACTION_VIEW,"");
//        String data = intentBundle.getString(DATA,null);
//        Bundle bundle;
//        if (Intent.ACTION_VIEW.equals(action) && data != null) {
//            bundle = new Bundle();
//            try {
//                if(data.endsWith(".html")) {
//                    bundle.putString(URL,data);
//                    startActivity(BrowserActivity.class,bundle);
//                    return;
//                }
//                data = data.replace(BrowserActivity.BASE_URL, "");
//                Logger.e(data);
//                final String[] arr = data.split("-");
//                if (data.contains("/")) {
//                    bundle.putString(PRODUCT_ID, arr[arr.length - 2] + "-" + arr[arr.length - 1]);
//                    if (data.startsWith("books"))
//                        bundle.putInt(IS_BOOK, 1);
//                    startActivity(ProductDetailActivity.class, bundle);
//                    onBackPressed();
//                    return;
//                } else {
//                    bundle.putString(PRODUCT_ID, arr[arr.length - 1]);
//                    startActivity(ProductDetailActivity.class, bundle);
//                    onBackPressed();
//                    return;
//                }
//            } catch (Exception e) {
//            }
//        } else {
//            switch ((intentBundle == null) ? -1 : intentBundle.getInt(NOTIFICATION_TYPE, -1)) {
//                case WEB_ACTIVITY:
//                    bundle = new Bundle();
//                    bundle.putString(URL, (intentBundle == null) ? null : intentBundle.getString(DATA, null));
//                    startActivity(BrowserActivity.class, bundle);
//                    break;
//                case PRODUCT_LIST_ACTIVITY:
//                    bundle = new Bundle();
//                    bundle.putString(QUERY, (intentBundle == null) ? null : intentBundle.getString(DATA, null));
//                    startActivity(ProductListActivity.class, bundle);
//                    break;
//                case BOOK_LIST_ACTIVITY:
//                    bundle = new Bundle();
//                    startActivity(com.indiashopps.android.view_module.book_list.ProductListActivity.class, (intentBundle == null) ? null : parseBundle(bundle, intentBundle.getString(DATA, "").split("&")));
//                    break;
//                case COUPON_LIST_ACTIVITY:
//                    bundle = new Bundle();
//                    startActivity(CouponListActivity.class, (intentBundle == null) ? null : parseBundle(bundle, intentBundle.getString(DATA, "").split("&")));
//                    break;
//                case LOAN_LIST_ACTIVITY:
//                    bundle = new Bundle();
//                    bundle.putString(QUERY, (intentBundle == null) ? null : intentBundle.getString(DATA, null));
//                    startActivity(LoanListActivity.class, bundle);
//                    break;
////            default:
////                startActivity(HomeActivity.class);
//            }
//        }


    }

    public Bundle parseBundle(Bundle bundle, String[] data) {
        try {
            for (int i = 0; i < data.length; i++) {
                bundle.putString(data[i].split("=")[0], data[i].split("=")[1]);
            }
        } catch (Exception e) {

        }
        return bundle;
    }
}
