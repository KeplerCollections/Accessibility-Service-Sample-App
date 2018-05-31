package com.kepler.projectsupportlib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.socialrehab.R;

import butterknife.ButterKnife;


/**
 * Created by special on 21/11/17.
 */

public abstract class BaseFragment extends Fragment {

    private static final String TAG = BaseFragment.class.getSimpleName();
    protected FragmentCommunicator fragmentCommunicator;
    private View view;
    private Bundle bundle;

    protected void openInBrowser(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.view != null) {
            this.view = null;
        }
        if (this.fragmentCommunicator != null) {
            this.fragmentCommunicator = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentCommunicator = (FragmentCommunicator) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setFragmentTitle(getFragmentTitle());
    }

    protected abstract int getFragmentTitle();

    private void setFragmentTitle(int title) {
        fragmentCommunicator.setFragmentTitle(title);
    }

    protected void setFragmentTitle(String title) {
        fragmentCommunicator.setFragmentTitle(title);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getViewResource(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    protected abstract int getViewResource();


    protected void showToast(int message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

//    protected void showSnack(int message) {
//        showSnack(getResources().getString(message));
//    }
//
//    protected void showSnack(String message) {
//        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_SHORT).show();
//    }
//
//    protected void showSnack(int message, int action, View.OnClickListener onClickListener) {
//        showSnack(getResources().getString(message), action, onClickListener);
//    }
//
//    protected void showSnack(String message, int action, View.OnClickListener onClickListener) {
//        Snackbar.make(view, Html.fromHtml("<font color=\"#ffffff\">" + message + "</font>"), Snackbar.LENGTH_LONG).setAction(action, onClickListener).show();
//    }

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void hideInputKeyboard(View view) {
        if (view.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void showInputKeyboard(View view) {
        if (view.hasFocus()) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, 0);
        }
    }

    protected int getInt(String value) {
        try {
            return (value.length() == 0) ? 0 : Integer.parseInt(value);
        } catch (Exception e) {
        }
        return 0;
    }

    protected void replaceFragment(Fragment fragment) {
        replaceFragment(fragment, null, true);
    }

    protected void replaceFragment(Fragment fragment, Bundle bundle) {
        replaceFragment(fragment, bundle, true);
    }

    private void replaceFragment(Fragment fragment, Bundle bundle, boolean addTo) {
        fragmentCommunicator.replaceFragment(fragment, bundle, addTo);
    }

    protected void addFragment(Fragment fragment) {
        addFragment(fragment, null, false);

    }

    protected void addFragment(Fragment fragment, boolean addToStack) {
        addFragment(fragment, null, addToStack);
    }

    private void addFragment(Fragment fragment, Bundle bundle, boolean addToStack) {
        fragmentCommunicator.addFragment(fragment, bundle, addToStack);
    }

//    protected View getVerticalSeperator() {
//        View view = new View(getActivity());
//        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
//        view.setBackgroundResource(R.color.colorSeprator);
//        return view;
//    }


    protected void showAlert(String message) {
        new AlertDialog.Builder(getActivity()).setCancelable(true).setMessage(message).setPositiveButton(R.string.okay, null).create().show();
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass) {
        Intent intent = new Intent(getActivity(), aClass);
        startActivity(intent);
    }

    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, int flags) {
        Intent intent = new Intent(getActivity(), aClass);
        intent.setFlags(flags);
        startActivity(intent);
    }


    protected void startActivity(@NonNull Class<? extends BaseActivity> aClass, Bundle bundle) {
        Intent intent = new Intent(getActivity(), aClass);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }


//    protected void startActivity(int key, Bundle intentBundle) {
//        Bundle bundle;
//        switch (key) {
//            case WEB_ACTIVITY:
//                bundle = new Bundle();
//                bundle.putString(URL, (intentBundle == null) ? null : intentBundle.getString(DATA, null));
//                startActivity(BrowserActivity.class, bundle);
//                break;
//            case PRODUCT_LIST_ACTIVITY:
//                bundle = new Bundle();
//                bundle.putString(QUERY, (intentBundle == null) ? null : intentBundle.getString(DATA, null));
//                startActivity(ProductListActivity.class, bundle);
//                break;
//            case BOOK_LIST_ACTIVITY:
//                bundle = new Bundle();
//                startActivity(com.indiashopps.android.view_module.book_list.ProductListActivity.class, (intentBundle == null) ? null : parseBundle(bundle, intentBundle.getString(DATA, "").split("&")));
//                break;
//            case COUPON_LIST_ACTIVITY:
//                bundle = new Bundle();
//                startActivity(CouponListActivity.class, (intentBundle == null) ? null : parseBundle(bundle, intentBundle.getString(DATA, "").split("&")));
//                break;
//            case LOAN_LIST_ACTIVITY:
//                bundle = new Bundle();
//                startActivity(LoanListActivity.class, (intentBundle == null) ? null : parseBundle(bundle, intentBundle.getString(DATA, "").split("&")));
//                break;
//            default:
//                startActivity(HomeActivity.class);
//        }
//    }
//    protected void startActivity(int key, String data) {
//        switch (key) {
//            case WEB_ACTIVITY:
//                bundle = new Bundle();
//                bundle.putString(URL, data);
//                startActivity(BrowserActivity.class, bundle);
//                break;
//            case PRODUCT_LIST_ACTIVITY:
//                bundle = new Bundle();
//                bundle.putString(QUERY, data);
//                startActivity(ProductListActivity.class, bundle);
//                break;
//            case BOOK_LIST_ACTIVITY:
//                bundle = new Bundle();
//                startActivity(com.indiashopps.android.view_module.book_list.ProductListActivity.class, (data == null || data.isEmpty()) ? null : parseBundle(bundle, data.split("&")));
//                break;
//            case COUPON_LIST_ACTIVITY:
//                bundle = new Bundle();
//                startActivity(CouponListActivity.class, (data == null || data.isEmpty()) ? null : parseBundle(bundle, data.split("&")));
//                break;
//            case LOAN_LIST_ACTIVITY:
//                bundle = new Bundle();
//                bundle.putString(QUERY, data);
//                startActivity(LoanListActivity.class, bundle);
//                break;
////            default:
////                startActivity(HomeActivity.class);
//        }
//    }

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
